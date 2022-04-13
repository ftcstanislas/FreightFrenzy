package org.firstinspires.ftc.teamcode.CameraSystemLib;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.SwitchableCamera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.RobotParts.MecanumDrive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CameraOdometry {

    /**********CONSTANTS**********/
    final int HISTORY_LENGTH = 1; // Amount of previous locations stored

    private String vuforiaLicenseKey = "YOUR KEY";

    //The positions of the camera in mm relative to the center of the robot {y, x}
    double[] positionCamera1 = {230, -130};
    double[] positionCamera2 = {230, 130};


    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float mmTargetHeight = 152.4f;// the height of the center of the target image above the floor
    private static final float fieldTile = 609.6f;
    private static final float halfField = 3 * fieldTile;
    private static final float halfTile = 0.5f * fieldTile;
    private static final float oneAndHalfTile = 1.5f * fieldTile;



    /**********GLOBAL VARS**********/
    private IMU IMU = null;
    public Camera camera1 = null;
    public Camera camera2 = null;
    private Telemetry.Item telemetry = null;
    private MecanumDrive drivetrain = null;
    private boolean advanced = false;

    // Storing locations
    ArrayList<Double> historyX = new ArrayList<Double>(); // History arraylist for the X-positions
    ArrayList<Double> historyY = new ArrayList<Double>(); // History arraylist for the Y-positions
    ArrayList<Double> historyHeading = new ArrayList<Double>(); // History arraylist for the headings
    double x;
    double y;
    double heading;

    // Targets
    private VuforiaTrackables targets = null;
    List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();

    // Camera names
    private WebcamName webcam1, webcam2;

    // Active camera
    private SwitchableCamera switchableCamera; // Active camera vuforia object
    private Camera activeCamera = null; // Active camera class
    double[] positionCamera = null; // Active camera position

    //Vuforia
    VuforiaLocalizer.Parameters parameters = null;
    private VuforiaLocalizer vuforia;

    // Telemetry
    String text = "";

    public void CameraOdometry(HardwareMap hardwareMap, Telemetry.Item telemetryInit, Telemetry.Item telemetryDucks) {
        // IMU
        IMU = new IMU();
        IMU.init(hardwareMap);
        IMU.setHeading(heading);

        initVuforia(hardwareMap);
        initTargets();

        // Camera 1
        camera1 = new Camera();
        camera1.init(allTrackables, vuforia, parameters, hardwareMap, "1", 1.32, 0.449, telemetryInit);
        camera1.setPointerPosition(x, y, heading, false);

        // Camera 2
        camera2 = new Camera();
        camera2.init(allTrackables, vuforia, parameters, hardwareMap, "2", 1.32, 1.291, telemetryInit);
        camera2.setPointerPosition(x, y, heading, false);

        // Activate active camera
        updatePointerPositions();
        updateActiveCamera();

        telemetry = telemetryInit;
    }


    public void setStartingPosition(double[] position) {
        x = position[0];
        y = position[1];
        heading = position[2];

        historyX.add(x);
        historyY.add(y);
        historyHeading.add(heading);
    }

    public void update() {

        // Update heading
        heading = IMU.getHeading();
        if (heading < -180) {
            heading += 360;
        } else if (heading >= 180) {
            heading -= 360;
        }

        // Camera
        camera1.updateServoPosition();
        camera2.updateServoPosition();

        // Camera update
        activeCamera.update();

        // Calculate new position of robot
        if (activeCamera.targetVisible) {
            //Location of camera on the field
            double[] locationCamera = activeCamera.getPosition();

            //Location of center of robot from the camera (relative the field)
            double[] robotCoordinates = activeCamera.calculateRobotCoordinates(positionCamera, heading);

            //Calculate center of robot on the field
            double robotX = locationCamera[0] + robotCoordinates[0];
            double robotY = locationCamera[1] + robotCoordinates[1];

            //Add new location to locations history
            historyX.add(robotX);
            historyY.add(robotY);
            historyHeading.add(locationCamera[2]);
        }

        // Remove indexes of history when length exceeds limit
        while (historyX.size() > HISTORY_LENGTH) {
            historyX.remove(0);
        }
        while (historyY.size() > HISTORY_LENGTH) {
            historyY.remove(0);
        }
        while (historyHeading.size() > HISTORY_LENGTH) {
            historyHeading.remove(0);
        }

        // Update position by getting avarage of previous locations
        if (historyX.size() != 0) {
            x = historyX.stream().mapToDouble(a -> a).average().getAsDouble();
        }
        if (historyY.size() != 0) {
            y = historyY.stream().mapToDouble(a -> a).average().getAsDouble();
        }

        // Telemetry
        text += String.format("\nPos robot (mm) {X, Y, heading} = %.1f, %.1f %.1f",
                x, y, heading);

        // Telemetry
        text += String.format("\nActive cam %s visible %b",
                activeCamera.id, activeCamera.targetVisible);

        // Update both pointer servo's
        updatePointerPositions();

        //Update active camera stream
        updateActiveCamera();

        // Flush text
        telemetry.setValue(text);
        text = "";
    }


    // Get inactive webcam class
    public Camera getInActiveWebcam() {
        if (activeCamera == camera1) {
            return camera2;
        } else {
            return camera1;
        }
    }

    // Get inactive webcam name
    public String getInActiveWebcamName() {
        if (activeCamera == camera1) {
            return "Webcam 2";
        } else if (activeCamera == camera2) {
            return "Webcam 1";
        } else {
            return "";
        }
    }

    private void updatePointerPositions() {
        // Get camera 1 position on the field by using robot's center position
        double[] robotCoordinates1 = camera1.calculateRobotCoordinates(positionCamera1, heading);
        double camera1RobotX = robotCoordinates1[0];
        double camera1RobotY = robotCoordinates1[1];

        // Get camera 2 position on the field by using robot's center position
        double[] robotCoordinates2 = camera2.calculateRobotCoordinates(positionCamera2, heading);
        double camera2RobotX = robotCoordinates2[0];
        double camera2RobotY = robotCoordinates2[1];

        // Update pointer positions, keep this way because of pointing at variable
        boolean activeCamera1;
        boolean activeCamera2;
        if (activeCamera == camera1) {
            activeCamera1 = true;
            activeCamera2 = false;
        } else if (activeCamera == camera2) {
            activeCamera1 = false;
            activeCamera2 = true;
        } else {
            activeCamera1 = false;
            activeCamera2 = false;
        }

        //Update both camera pointers
        camera1.setPointerPosition(x - camera1RobotX, y - camera1RobotY, heading, activeCamera1);
        camera2.setPointerPosition(x - camera2RobotX, y - camera2RobotY, heading, activeCamera2);
    }

    private void updateActiveCamera() {
        // Get camera 1 position on the field by using robot's center position
        double[] robotCoordinates1 = camera1.calculateRobotCoordinates(positionCamera1, heading);
        double camera1RobotX = robotCoordinates1[0];
        double camera1RobotY = robotCoordinates1[1];

        // Get camera 2 position on the field by using robot's center position
        double[] robotCoordinates2 = camera2.calculateRobotCoordinates(positionCamera2, heading);
        double camera2RobotX = robotCoordinates2[0];
        double camera2RobotY = robotCoordinates2[1];

        // Get scores for both camera
        boolean activeCamera1;
        boolean activeCamera2;
        if (activeCamera == camera1) {
            activeCamera1 = true;
            activeCamera2 = false;
        } else if (activeCamera == camera2) {
            activeCamera1 = false;
            activeCamera2 = true;
        } else {
            activeCamera1 = false;
            activeCamera2 = false;
        }
        double[] scoreCamera1 = camera1.getBestScore(x - camera1RobotX, y - camera1RobotY, heading, activeCamera1);
        double[] scoreCamera2 = camera2.getBestScore(x - camera2RobotX, y - camera2RobotY, heading, activeCamera2);

        // Switch camera to the one with best score
        double scoreDifference = Math.abs(scoreCamera1[0] - scoreCamera2[0]);
        double MAX_SCORE_TO_SWITCH = 1000;
        double MIN_SCORE_TO_SWITCH = 100;
        double MIN_SCORE_DIFFERENCE = 100;
        if (scoreDifference >= MIN_SCORE_DIFFERENCE) {
            /* Set camera 1 active if:
            -It's score is better than camera 2
            -It's not currently the active camera
            -It's does not exceed the limits for when to switch */
            if (scoreCamera1[0] < scoreCamera2[0] && activeCamera != camera1 && scoreCamera1[0] < MAX_SCORE_TO_SWITCH && scoreCamera1[0] > MIN_SCORE_TO_SWITCH) {
                setActiveCamera(1);
            }
            /* Set camera 2 active if:
            -It's score is better than camera 1
            -It's not currently the active camera
            -It's does not exceed the limits for when to switch */
            else if (scoreCamera1[0] > scoreCamera2[0] && activeCamera != camera2 && scoreCamera2[0] < MAX_SCORE_TO_SWITCH && scoreCamera2[0] > MIN_SCORE_TO_SWITCH) {
                setActiveCamera(2);
            }
        }

        // Telemetry
        text += String.format("\ncamera 1 (%.0f, %.1f, %.1f)",
                scoreCamera1[2], scoreCamera1[0], scoreCamera1[1]);
        text += String.format("\ncamera 2 (%.0f, %.1f, %.1f)",
                scoreCamera2[2], scoreCamera2[0], scoreCamera2[1]);
    }

    private void setActiveCamera(int number) {
        // Swap active camera variables
        if (number == 1) {
            activeCamera = camera1;
            positionCamera = positionCamera1;
            switchableCamera.setActiveCamera(webcam1);
        } else if (number == 2) {
            activeCamera = camera2;
            positionCamera = positionCamera2;
            switchableCamera.setActiveCamera(webcam2);
        } else {
            throw new java.lang.Error("Active camera number is not 1 or 2");
        }
        activeCamera.targetVisible = false;
        updatePointerPositions();
    }

    public void initVuforia(HardwareMap hardwareMap) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        
        // With camera stream
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // Without camera stream
        // parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = vuforiaLicenseKey;

        // Indicate that we wish to be able to switch cameras.
        webcam1 = hardwareMap.get(WebcamName.class, "Webcam 1");
        webcam2 = hardwareMap.get(WebcamName.class, "Webcam 2");
        parameters.cameraName = ClassFactory.getInstance().getCameraManager().nameForSwitchableCamera(webcam1, webcam2);

        // parameters.useExtendedTracking = false;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        switchableCamera = (SwitchableCamera) vuforia.getCamera();
    }

    public void initTargets() {
        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        targets = vuforia.loadTrackablesFromAsset("FreightFrenzy");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        allTrackables.addAll(targets);

        // Name and locate each trackable object
        identifyTarget(0, "Blue Storage", -halfField, oneAndHalfTile, mmTargetHeight, 90, 0, 90);
        identifyTarget(1, "Blue Alliance Wall", halfTile, halfField, mmTargetHeight, 90, 0, 0);
        identifyTarget(2, "Red Storage", -halfField, -oneAndHalfTile, mmTargetHeight, 90, 0, 90);
        identifyTarget(3, "Red Alliance Wall", halfTile, -halfField, mmTargetHeight, 90, 0, 180);

        targets.activate();
    }

    public void stop() {
        targets.deactivate();
    }

    public double getXCoordinate() {
        return x;
    }

    public double getYCoordinate() {
        return y;
    }

    public double getOrientation() {
        return heading;
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
}