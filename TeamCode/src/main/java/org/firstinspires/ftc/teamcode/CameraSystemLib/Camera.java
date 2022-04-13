package org.firstinspires.ftc.teamcode.CameraSystemLib;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

public class Camera{
    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float mmTargetHeight   = 152.4f;// the height of the center of the target image above the floor
    private static final float fieldTile        = 609.6f;
    private static final float halfField        = 3 * fieldTile;
    private static final float halfTile         = 0.5f * fieldTile;
    private static final float oneAndHalfTile   = 1.5f * fieldTile;

    // Class Members
    private OpenGLMatrix lastLocation   = null;
    private VuforiaLocalizer vuforia    = null;

    public boolean targetVisible       = false;

    // Telemetry
    protected Telemetry.Item telemetry = null;

    // Trackables
    List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();

    // Parameters
    VuforiaLocalizer.Parameters parameters = null;

    // Pointer
    Servo pointer = null;

    double pointerPosition = 0;
    double pointerLastUpdate = System.currentTimeMillis();;
    double POINTER_SPEED = 1.14;

    //Default pointer angle (forwards)
    private double pointerAngle = 90;

    //Pointer servo constants
    double TOTAL_COUNTS_PER_ROUND = 0;
    double OFFSET = 0;

    //Navigation image index
    double pointingAt = 0;

    //Webcam id (1 or 2)
    String id = "";

    public void init(List<VuforiaTrackable> allTrackablesInit, VuforiaLocalizer vuforiaInit, VuforiaLocalizer.Parameters parametersInit, HardwareMap hardwareMap, String number, double TOTAL_COUNTS_PER_ROUND_INIT, double OFFSET_INIT, Telemetry.Item telemetryInit) {
        // Telemetry
        telemetry = telemetryInit;

        // Servo pointer
        id = number;
        pointer = hardwareMap.get(Servo.class, "cameraPointer" + number);
        TOTAL_COUNTS_PER_ROUND = TOTAL_COUNTS_PER_ROUND_INIT;
        OFFSET = OFFSET_INIT;

        // Init
        vuforia = vuforiaInit;
        parameters = parametersInit;
        allTrackables = allTrackablesInit;
    }

    public void setCameraPosition(float forward, float left, float height, float heading){
        /**  Create a matrix containing information about the camera location on the robot.  */
        OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
                .translation(forward, left, height)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, heading, 0));

        /**  Let all the trackable listeners know where the camera is.  */
        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(parameters.cameraName, cameraLocationOnRobot);
        }
    }

    public void update() {
        targetVisible = false;
        updateServoPosition();

        //Only update if pointer has the correct target position
        if (pointerPosition == pointer.getPosition()) {
            updateCamera();
        }
    }

    public void updateCamera(){
        // Check all the trackable targets to see which one (if any) is visible.
        targetVisible = false;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                targetVisible = true;

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
                break;
            }
        }
    }

    public void setPointerPosition(double x, double y, double heading, boolean activeCamera){
        double[] data = getBestScore(x, y, heading, activeCamera);
        pointingAt = data[2];
        double bestAngle = data[1];
        setPointerAngle(bestAngle, activeCamera);
    }

    public double[] calculateRobotCoordinates(double[] positionCamera, double heading) {
        double robotHeadingRadians = Math.toRadians(heading - 180);
        double robotX = positionCamera[0] * Math.cos(robotHeadingRadians) + positionCamera[1] * -Math.sin(robotHeadingRadians);
        double robotY = positionCamera[0] * Math.sin(robotHeadingRadians) + positionCamera[1] * Math.cos(robotHeadingRadians);
        return new double[]{robotX, robotY};
    }

    /* 
    Calculate scores for each possible active camera configuration by calculating
    -Distance to each navigation image
    -Best rotation for each navigation image 
    */
    public double[] getBestScore(double x, double y, double heading, boolean activeCamera){
        double[][] locations = {
                {-halfField, oneAndHalfTile}, // 0: blue storage
                {-halfField, -oneAndHalfTile}, // 1: red storage
                {halfTile, halfField}, // 2: blue warehouse
                {halfTile, -halfField} // 3: red warehouse
        };
        double bestScore = Double.MAX_VALUE;
        double bestPointerAngle = 90;
        double bestIndex = -1;
        for (int index = 0; index < locations.length; index++) {
            // Distance to target
            double dx = locations[index][0] - x;
            double dy = locations[index][1] - y;

            // Calculate servo angle and target position
            double pointerAngle = (Math.toDegrees(Math.atan2(dy, dx)) - heading + 90);
            double pointerTargetPosition = getPointerPosition(pointerAngle);

            // Score is distance
            double score = Math.hypot(dx, dy);

            // Outside of turning area of servo
            if (pointerTargetPosition < 0.0 || pointerTargetPosition > 1.0) {
                score *= 100000; // Very bad score
            }

            /* 
            Check wether it's the best option, only update best score if:
            -The current camera is not the active camera
            -The active camera is pointing at the same navigation image */
            if ((score < bestScore && !activeCamera) || (activeCamera && index == pointingAt)) {
                bestScore = score;
                bestPointerAngle = pointerAngle;
                bestIndex = index;
            }
        }
        return new double[] {bestScore, Math.round(bestPointerAngle), bestIndex};
    }

    public void updateServoPosition(){
        double currentTime = System.currentTimeMillis();
        double timePassed = (currentTime - pointerLastUpdate)/1000; //In seconds
        double movement = pointer.getPosition() - pointerPosition;
        if (movement > 0){
            pointerPosition += Math.min(POINTER_SPEED * timePassed, movement);
        } else if (movement < 0){
            pointerPosition += Math.max(-POINTER_SPEED * timePassed, movement);
        }

        pointerLastUpdate = currentTime;
    }

    // Set angle of servo (angle in degrees)
    public void setPointerAngle(double angle, boolean activeCamera) {
        //Get the pointer position in servo ticks
        double targetPointerPosition = getPointerPosition(angle);

        updateServoPosition();

        //Turn servo to this position
        pointer.setPosition(targetPointerPosition);

        //Update the camera's relative position on the robot, because servo has turned
        if (activeCamera) {
            setCameraPosition(0, 0, 222, (float) angle);
        }
    }

    // Get position of pointer in ticks for angle
    public double getPointerPosition(double angle){
        double targetPointerPosition = TOTAL_COUNTS_PER_ROUND / 360 * angle + OFFSET;
        double outsideServoSize = (TOTAL_COUNTS_PER_ROUND - 1) / 2;
        while (targetPointerPosition < -outsideServoSize) {
            targetPointerPosition += TOTAL_COUNTS_PER_ROUND;
        }
        while (targetPointerPosition > 1 + outsideServoSize) {
            targetPointerPosition -= TOTAL_COUNTS_PER_ROUND;
        }

        return targetPointerPosition;
    }

    // Get position of robot in array like: [x, y, heading]
    public double[] getPosition() {
        VectorF translation = lastLocation.getTranslation();
        Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
        double[] position = new double[]{translation.get(0), translation.get(1), rotation.thirdAngle};
        return position;
    }
}

