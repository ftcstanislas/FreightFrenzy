package org.firstinspires.ftc.teamcode.Location;

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
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

public class Camera{
    private static final String VUFORIA_KEY =
            "AYijR7b/////AAABmQXodEBY4E1gjxKsoSygzWsm4RFjj/z+nzPa0q3oo3vJNz51j477KysEdl4h1YfezCokKxkUeK3ARNjE1tH80M5gZbubu2bkdF6Ja8gINhJTAY/ZJrFkPGiiLfausXsCWAygHW7ufeu3FLIDp1DN2NHj4rzDP4vRv5z/0T0deRLucpRcv36hqUkJ1N6Duumo0se+BCmdAh7ycUW2wteJ3T1e/LxuO5sI6qtwnJW64fe2n6cehk5su76c9t45AcBfod6f0txGezdzpqY5NoHjz0G/gLvah0vAYW+/0x3yaWy8thEd64OVMVb2q37TsJ1UDjl5qupztdG7nXkRGYF5oaR8CGkm2lqPyugJuRFNMRcM";

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
    private TFObjectDetector tfod       = null;
//    private VuforiaTrackables targets   = null;

    public boolean targetVisible       = false;

    // Telemetry
    protected Telemetry.Item telemetry = null;
    protected Telemetry.Item telemetryDucks = null;

    // Trackables
    List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();

    // Paramerters
    VuforiaLocalizer.Parameters parameters = null;

    // Pointer
    Servo pointer = null;
    double pointerPosition = 0;
    double pointerLastUpdate = System.currentTimeMillis();;
    double POINTER_SPEED = 1.14;
    private double pointerAngle = 90;
    double TOTAL_COUNTS_PER_ROUND = 0;
    double OFFSET = 0;
    double angleMultiplier = 2;

    // Game element detection
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
      "Ball",
      "Cube",
      "Duck",
      "Marker"
    };

    private boolean checkDuck = false;

    public void init(List<VuforiaTrackable> allTrackablesInit, VuforiaLocalizer vuforiaInit, VuforiaLocalizer.Parameters parametersInit, HardwareMap hardwareMap, String number, double TOTAL_COUNTS_PER_ROUND_INIT, double OFFSET_INIT, Telemetry.Item telemetryInit, Telemetry.Item telemetryDucksInit) {
        // Telemetry
        telemetry = telemetryInit;
        telemetryDucks = telemetryDucksInit;

        // Servo pointer
        pointer = hardwareMap.get(Servo.class, "cameraPointer"+number);
        TOTAL_COUNTS_PER_ROUND = TOTAL_COUNTS_PER_ROUND_INIT;
        OFFSET = OFFSET_INIT;

        // Init
        vuforia = vuforiaInit;
        parameters = parametersInit;
        allTrackables = allTrackablesInit;

        //Duck
        if (false) {
            int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                    "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
            tfodParameters.minResultConfidence = 0.8f;
            tfodParameters.isModelTensorFlow2 = true;
            tfodParameters.inputSize = 320;
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);

            if (tfod != null) {
                tfod.activate();

            }
        }
    }

    public void setCameraPosition(float forward, float left, float height, float heading){
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

        if (pointerPosition == pointer.getPosition()) {
            updateCamera();
        }
    }

    public void updateCamera(){
        // check all the trackable targets to see which one (if any) is visible.
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

        // Provide feedback as to where the robot is located (if we know).
        String text = "";
        if (lastLocation != null) {
            // express position (translation) of robot in inches.
            VectorF translation = lastLocation.getTranslation();
            text += String.format("Pos (mm) {X, Y} = %.1f, %.1f",
                    translation.get(0), translation.get(1));

            // express the rotation of the robot in degrees.
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            text += String.format("\nRot (deg) {Heading} = %.0f", rotation.thirdAngle);
        }

//        telemetryDucks.setValue(text);
    }

    public void setPointerPosition(double x, double y, double heading, boolean updateTrackables){
        double[] data = getBestScore(x, y, heading);
        double bestAngle = data[1];
        setPointerAngle(bestAngle, updateTrackables);

//        telemetryDucks.setValue(String.format("heading %.1f newAngle %.1f",
//                heading, bestAngle));
    }

    public double[] calculateRobotCoordinates(double[] positionCamera, double heading) {
        double robotHeadingRadians = Math.toRadians(heading - 180);
        double robotX = positionCamera[0] * Math.cos(robotHeadingRadians) + positionCamera[1] * -Math.sin(robotHeadingRadians);
        double robotY = positionCamera[0] * Math.sin(robotHeadingRadians) + positionCamera[1] * Math.cos(robotHeadingRadians);
        return new double[]{robotX, robotY};
    }

    public double[] getBestScore(double x, double y, double heading){
        double[][] locations = {{-halfField, oneAndHalfTile, 180},{-halfField, -oneAndHalfTile, 180}, {halfTile, halfField, -90},{halfTile, -halfField, 90}};
        double bestScore = Double.MAX_VALUE;
        double bestAngle = 90;
        double bestIndex = -1;
        double index = 0;
        for (double[] location : locations) {
            double dx = location[0] - x;
            double dy = location[1] - y;
            double relativeHeading;
            double newAngle = 0;
            if (location[2] == 180) {
                relativeHeading = -Math.atan2(dx, dy) / Math.PI * 180;
                newAngle = 180 - heading + relativeHeading;
            } else if (location[2] == 90) {
                relativeHeading = Math.atan2(dy, dx) / Math.PI * 180;
                newAngle = 90 - heading + relativeHeading;
            } else if (location[2] == -90) {
                relativeHeading = -Math.atan2(dy, dx) / Math.PI * 180;
                newAngle = 270 - heading + relativeHeading;
            }
//            double newAngle = 180 - robotOrientation + relativeHeading ; // 270 was 180
            while (newAngle < 0) {
                newAngle += 360;
            }
            while (newAngle >= 360) {
                newAngle -= 360;
            }

            double newPointerAngle = newAngle;

            while (newPointerAngle < -180) {//IS DIT MOGELIJK???
                newPointerAngle += 360;
            }
            while (newPointerAngle >= 180) {
                newPointerAngle -= 360;
            }

            double targetPointerPosition = TOTAL_COUNTS_PER_ROUND / 360 * (180 - newPointerAngle) + OFFSET;
            double score;
            if (targetPointerPosition >= 0.0 && targetPointerPosition <= 1.0) {
                double distance = Math.hypot(dx, dy);
                angleMultiplier = 4;
                double angleScore = 1;//Math.abs(targetPointerPosition - 0.5) * angleMultiplier;
                score = angleScore * distance;
            } else {
                score = Double.MAX_VALUE;
            }

            if (score < bestScore) {
                bestIndex = index;
                bestScore = score;
                bestAngle = newPointerAngle;
            }
            index++;
        }
//        telemetryDucks.setValue(bestScore+" "+bestAngle+" "+bestIndex+" ");
        return new double[]{bestScore, bestAngle, bestIndex};
    }

    public void updateServoPosition(){
        double currentTime = System.currentTimeMillis();
        double timePassed = (currentTime - pointerLastUpdate)/1000;
        double movement = pointer.getPosition() - pointerPosition;
        if (movement > 0){
            pointerPosition += Math.min(POINTER_SPEED * timePassed, movement);
        } else if (movement < 0){
            pointerPosition += Math.max(-POINTER_SPEED * timePassed, movement);
        }

//        telemetryDucks.setValue(pointerPosition+" = "+pointer.getPosition());

        pointerLastUpdate = currentTime;
    }

    // Set angle of servo (angle in degrees)
    public void setPointerAngle(double angle, boolean updateTrackables) {
        double targetPointerPosition = TOTAL_COUNTS_PER_ROUND / 360 * (180 - angle) + OFFSET;
//        while (targetPointerPosition < -0.19){
//            targetPointerPosition+=2;
//        }
//        while (targetPointerPosition >= 1.19){
//            targetPointerPosition-=2;
//        }
        updateServoPosition();
        pointer.setPosition(targetPointerPosition);
        if (updateTrackables) {
            setCameraPosition(0, 0, 222, (float) angle);
        }
    }

    // Called when stopping script
    public void stop(){
        // Disable Tracking when we are done;
//        targets.deactivate();
//        stopDuckDetection();
    }

    public double[] getPosition() {
        double[] position;
        if (lastLocation != null) {
            VectorF translation = lastLocation.getTranslation();
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            position = new double[]{translation.get(0), translation.get(1), rotation.thirdAngle};
        } else {
            position = new double[]{0, 0, 0};
        }
        return position;
    }

    public void addZoomBox() {
        tfod.setZoom(2.5, 16.0/9.0);
    }

    public void removeZoomBox() {
        tfod.setZoom(1, 16.0/9.0);
    }

    public String detectDuck() {
        if (tfod != null) {
            String text = "";
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                text = "# Object Detected " + updatedRecognitions.size();
                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    text += String.format("label (%d)", i) + recognition.getLabel();
                    text += String.format("  left,top (%d)", i) + 
                        // "%.03f , %.03f" + 
                        recognition.getLeft() + recognition.getTop();
                    text += String.format("  right,bottom (%d)", i) + 
                        // "%.03f , %.03f" +
                        recognition.getRight() + recognition.getBottom();
                    i++;
                }
            } else {
                text = "No Objects Detected";
            }
            telemetryDucks.setValue(text);
        }
        return "none";//Need to be removed
    } 
}

