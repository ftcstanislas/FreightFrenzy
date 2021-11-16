package org.firstinspires.ftc.teamcode.Location;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.RADIANS;
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
    private VuforiaTrackables targets   = null;
    private WebcamName webcamName       = null;

    private boolean targetVisible       = false;

    // Telemetry
    protected Telemetry.Item telemetry = null;
    protected Telemetry.Item telemetryDucks = null;

    // Trackables
    List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();

    // Paramerters
    VuforiaLocalizer.Parameters parameters = null;

    // Pointer
    Servo pointer = null;
    private double angle = 90;

    // Game element detection
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
      "Ball",
      "Cube",
      "Duck",
      "Marker"
    };

    private boolean checkDuck = false;

    public void init(HardwareMap hardwareMap, Telemetry.Item telemetryInit, Telemetry.Item telemetryDucksInit) {
        // Telemetry
        telemetry = telemetryInit;
        telemetryDucks = telemetryDucksInit;

        // Connect to the camera we are to use.  This name must match what is set up in Robot Configuration
        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

        // Servo pointer
        pointer = hardwareMap.get(Servo.class, "cameraPointer1");

        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * We can pass Vuforia the handle to a camera preview resource (on the RC screen);
         * If no camera-preview is desired, use the parameter-less constructor instead (commented out below).
         * Note: A preview window is required if you want to view the camera stream on the Driver Station Phone.
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        // We also indicate which camera we wish to use.
        parameters.cameraName = webcamName;

        // Turn off Extended tracking.  Set this true if you want Vuforia to track beyond the target.
        parameters.useExtendedTracking = false;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        targets = this.vuforia.loadTrackablesFromAsset("FreightFrenzy");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        allTrackables.addAll(targets);

        // Name and locate each trackable object
        identifyTarget(0, "Blue Storage", -halfField, oneAndHalfTile, mmTargetHeight, 90, 0, 90);
        identifyTarget(1, "Blue Alliance Wall", halfTile, halfField, mmTargetHeight, 90, 0, 0);
        identifyTarget(2, "Red Storage", -halfField, -oneAndHalfTile, mmTargetHeight, 90, 0, 90);
        identifyTarget(3, "Red Alliance Wall", halfTile, -halfField, mmTargetHeight, 90, 0, 180);

        setCameraPosition(0,0,200,90);//90


        targets.activate();

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

//            setZoom(false); JELMER
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

        VectorF translation = null;
        Orientation rotation = null;
        if (lastLocation != null) {
            // express position (translation) of robot in inches.
            translation = lastLocation.getTranslation();
            text += String.format("Pos (mm) {X, Y} = %.1f, %.1f",
                    translation.get(0), translation.get(1));

            // express the rotation of the robot in degrees.
            rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            text += String.format("\nRot (deg) {Heading} = %.0f", rotation.thirdAngle);
        }

        if (targetVisible) {
            //blue storage
            double[] location = {-halfField, oneAndHalfTile};
            double[] robotLocationXY = {translation.get(0), translation.get(1)};
            double robotOrientation = rotation.thirdAngle;
            double dx = location[0] - robotLocationXY[0];
            double dy = location[1] - robotLocationXY[1];
//            double angle = Math.atan2(dy,dx)/Math.PI*180 - robotOrientation + 90;
            double newAngle = 180 - robotOrientation + -Math.atan2(dx,dy)/Math.PI*180;
            while (newAngle < 0){
                newAngle+=360;
            }
            while (newAngle >= 360){
                newAngle-=360;
            }

            // max speed of servo
            double difference = newAngle - angle;
            difference = (difference + 180) % 360 - 180;
            difference = Math.max(Math.min(difference, 0.05), -0.05);
            angle += difference;

            while (angle < 0){
                angle+=360;
            }
            while (angle >= 360){
                angle-=360;
            }
            text += String.format("\nd{X, Y, heading} = %.1f, %.1f, %.1f",
                    dx, dy, angle);
            text += String.format("\nangle = %.1f - %.1f + %.1f",
                    180.0,  robotOrientation, -Math.atan2(dx,dy)/Math.PI*180);


            final double TOTAL_COUNTS_PER_ROUND = 1.27;
            final double OFFSET = 0.045;

            double pointerPosition = TOTAL_COUNTS_PER_ROUND/360*(180 - angle) + OFFSET;

//            while (pointerPosition < -(TOTAL_COUNTS_PER_ROUND-1)/2){
//                pointerPosition+=TOTAL_COUNTS_PER_ROUND;
//            }
//            while (pointerPosition >= 1+(TOTAL_COUNTS_PER_ROUND-1)/2){
//                pointerPosition-=TOTAL_COUNTS_PER_ROUND;
//            }
            pointer.setPosition(pointerPosition);
            setCameraPosition(0, 0, 200, (float)(angle));
            text += String.format("\nPointer position = %.1f", pointerPosition);
//            text += "\nPointer position = %.1f" + pointerPosition;

        } else {
            text +="\nVisible Target none\nCamera heading "+angle;
        }

        telemetry.setValue(text);
    }

    // Set angle of servo (angle in degrees)
    public void setServoAngle(double angle) {
        final double TOTAL_COUNTS_PER_ROUND = 1.27;
        final double OFFSET = 0.045;

        double pointerPosition = TOTAL_COUNTS_PER_ROUND/360*angle + OFFSET;
        while (pointerPosition < -0.19){
            pointerPosition+=2;
        }
        while (pointerPosition >= 1.19){
            pointerPosition-=2;
        }
        pointer.setPosition(pointerPosition);
    }

    // Called when stopping script
    public void stop(){
        // Disable Tracking when we are done;
        targets.deactivate();
//        stopDuckDetection();
    }

    /***
     * Identify a target by naming it, and setting its position and orientation on the field
     * @param targetIndex
     * @param targetName
     * @param dx, dy, dz  Target offsets in x,y,z axes
     * @param rx, ry, rz  Target rotations in x,y,z axes
     */
    void identifyTarget(int targetIndex, String targetName, float dx, float dy, float dz, float rx, float ry, float rz) {
        VuforiaTrackable aTarget = targets.get(targetIndex);
        aTarget.setName(targetName);
        aTarget.setLocation(OpenGLMatrix.translation(dx, dy, dz)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz)));
    }

    public double[] getPosition() {
        double[] position;
        if (lastLocation != null) {
            VectorF translation = lastLocation.getTranslation();
            position = new double[]{translation.get(0), translation.get(1)};
        }  else {
            position = new double[]{0, 0};
        }
        return position;
    }

    public void startDuckDetection() {
        checkDuck = true;
        tfod.setZoom(2.5, 16.0/9.0);
        if (tfod != null) {
            while (checkDuck) {
                detectDuck();
            }
        }
    }

    public void stopDuckDetection() {
        checkDuck = false;
    }

    public void detectDuck() {
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
}

