package org.firstinspires.ftc.teamcode.Location;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.SwitchableCamera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.RobotParts.MecanumDrive;

import java.util.ArrayList;

public class Location {
//    private Odometry odometry = null;
    private IMU IMU = null;
    private Camera camera1 = null;
    private Camera camera2 = null;
    private Telemetry.Item telemetry = null;
    private MecanumDrive drivetrain = null;
    private boolean advanced = false;

    //Location
    final int HISTORY_LENGTH = 6;
    ArrayList<Double> historyX = new ArrayList<Double>();
    ArrayList<Double> historyY = new ArrayList<Double>();
    ArrayList<Double> historyHeading = new ArrayList<Double>();
    double x;
    double y;
    double heading;

    // Camera
    private WebcamName webcam1, webcam2;
    private SwitchableCamera switchableCamera;
    VuforiaLocalizer.Parameters parameters = null;
    private VuforiaLocalizer vuforia;

    public void init(HardwareMap hardwareMap, boolean advancedInit, double[] position, MecanumDrive drivetrainInit, Telemetry.Item telemetryInit, Telemetry.Item telemetryDucks){
        advanced = advancedInit; // keep track of location
        x = position[0];
        y = position[1];
        heading = position[2];

        // Odometry
//        odometry = new Odometry(
//                hardwareMap.get(DcMotor.class, "leftFront"),
//                hardwareMap.get(DcMotor.class, "rightFront"),
//                hardwareMap.get(DcMotor.class, "leftBack"),
////                hardwareMap.get(Servo.class, "encoder"),
//                2048 * 9 * Math.PI);
//        odometry.setPosition(0,0,0);

        //Drivetrain
        drivetrain = drivetrainInit;

        // IMU
        IMU = new IMU();
        IMU.init(hardwareMap);
        IMU.setHeading(heading);

        if (advanced) {
            initVuforia(hardwareMap);

            // Camera 1
            camera1 = new Camera();
            camera1.init(vuforia, parameters, hardwareMap, "1", 1.32, -0.028, telemetryInit, telemetryDucks); // , new float[]{170, 170, 230}
            camera1.setPointerPosition(x, y, heading);

            // Camera 2
            camera2 = new Camera();
            camera2.init(vuforia, parameters, hardwareMap, "2", 1.32, -0.028, telemetryInit, telemetryDucks); // , new float[]{170, 170, 230}
            camera2.setPointerPosition(x, y, heading);
        }
        
        telemetry = telemetryInit;
    }

    public void update(){
        double[][] positionCameras = {{170, -170}, {170, 170}};

        // Update heading
        heading = IMU.getHeading();

        // Camera
        double robotX = 0;
        double robotY = 0;
        Camera camera = null;
        double[] positionCamera = null;
        if (advanced) {
            double robotHeadingRadians = Math.toRadians(heading - 180);

            if (switchableCamera.getActiveCamera() == webcam1) {
                camera = camera1;
                positionCamera = positionCameras[0];
            } else if (switchableCamera.getActiveCamera() == webcam2){
                camera = camera2;
                positionCamera = positionCameras[1];
            }

            // Camera update
            camera.update();

            // Calculate new position of robot

            double[] locationCamera1 = camera.getPosition();
            robotX = positionCamera[0] * Math.cos(robotHeadingRadians) + positionCamera[1] * -Math.sin(robotHeadingRadians);
            robotY = positionCamera[0] * Math.sin(robotHeadingRadians) + positionCamera[1] * Math.cos(robotHeadingRadians);

            if (camera.isTargetVisible()) {
                historyX.add(locationCamera1[0]+robotX);
                historyY.add(locationCamera1[1]+robotY);
                historyHeading.add(locationCamera1[2]);
            }
        }

        // Remove part of history
        while (historyX.size() > HISTORY_LENGTH){
            historyX.remove(0);
        }
        while (historyY.size() > HISTORY_LENGTH){
            historyY.remove(0);
        }
        while (historyHeading.size() > HISTORY_LENGTH){
            historyHeading.remove(0);
        }

        // Update position
        if (historyX.size() != 0) {
            x = historyX.stream().mapToDouble(a -> a).average().getAsDouble();
        }
        if (historyY.size() != 0) {
            y = historyY.stream().mapToDouble(a -> a).average().getAsDouble();
        }

        // Update pointers camera
        if (advanced) {
            double robotHeadingRadians = Math.toRadians(heading - 180);

            // Camera 1
            robotX = positionCamera[0] * Math.cos(robotHeadingRadians) + positionCamera[1] * -Math.sin(robotHeadingRadians);
            robotY = positionCamera[0] * Math.sin(robotHeadingRadians) + positionCamera[1] * Math.cos(robotHeadingRadians);
            camera.setPointerPosition(x-robotX, y-robotY, heading);
        }

        telemetry.setValue(String.format("Pos robot (mm) {X, Y, heading} = %.1f, %.1f %.1f\nPos relative camera (mm) {X, Y,} = %.1f, %.1f",
                x, y, heading, robotX, robotY));



        //Duck
//        camera.setZoom(true);
//        camera.detectDuck();
//        telemetry.setValue(odometry.getDisplay()+"\n"+IMU.getDisplay());
    }

    public void initVuforia(HardwareMap hardwareMap){
//        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "AYijR7b/////AAABmQXodEBY4E1gjxKsoSygzWsm4RFjj/z+nzPa0q3oo3vJNz51j477KysEdl4h1YfezCokKxkUeK3ARNjE1tH80M5gZbubu2bkdF6Ja8gINhJTAY/ZJrFkPGiiLfausXsCWAygHW7ufeu3FLIDp1DN2NHj4rzDP4vRv5z/0T0deRLucpRcv36hqUkJ1N6Duumo0se+BCmdAh7ycUW2wteJ3T1e/LxuO5sI6qtwnJW64fe2n6cehk5su76c9t45AcBfod6f0txGezdzpqY5NoHjz0G/gLvah0vAYW+/0x3yaWy8thEd64OVMVb2q37TsJ1UDjl5qupztdG7nXkRGYF5oaR8CGkm2lqPyugJuRFNMRcM";;

        // Indicate that we wish to be able to switch cameras.
        webcam1 = hardwareMap.get(WebcamName.class, "Webcam 1");
        webcam2 = hardwareMap.get(WebcamName.class, "Webcam 2");
        parameters.cameraName = ClassFactory.getInstance().getCameraManager().nameForSwitchableCamera(webcam1, webcam2);

//        parameters.useExtendedTracking = false;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Set the active camera to Webcam 1.
        switchableCamera = (SwitchableCamera) vuforia.getCamera();
        switchableCamera.setActiveCamera(webcam1);

    }

    public void stop(){
        camera1.stop();
    }

    public double getXCoordinate() {
        return x;
    }

    public double getYCoordinate() {
        return y;
    }

    public double getOrientation(){
        return heading;
    }

//    FUTURE
    public boolean goToPosition(double targetX, double targetY, double targetRotation, double power) {
        //Constants
        double allowableDistanceError = 50;

        //Coordinates
        double distanceToXTarget = targetX - getXCoordinate();
        double distanceToYTarget = targetY - getYCoordinate();

        double orientationDifference = targetRotation - getOrientation();

        double distance = Math.hypot(distanceToXTarget, distanceToYTarget);

        if (distance > allowableDistanceError) {
            double robotMovementAngle = Math.toDegrees(Math.atan2(distanceToXTarget, distanceToYTarget)) - getOrientation();
            double robotMovementXComponent = calculateX(robotMovementAngle, power);
            double robotMovementYComponent = calculateY(robotMovementAngle, power);

            double turning = orientationDifference / 360;

//            drivetrain.setPowerDirection(robotMovementXComponent, robotMovementYComponent, turning, power);
            drivetrain.setPowerDirection(robotMovementYComponent, -robotMovementXComponent, turning, power);
            return false;
        } else {
            drivetrain.setPowerDirection(0,0,0,0);
            return true;
        }
    }

    public boolean goToCircle(double midPointX, double midPointY, double radius) {
        double currentX = getXCoordinate();
        double currentY = getYCoordinate();
        double dx = currentX - midPointX;
        double dy = currentY - midPointY;
        double distance = Math.hypot(dx, dy);
        double closestX = midPointX + dx / distance * radius;
        double closestY = midPointY + dy / distance * radius;
        double rotation = Math.toDegrees(Math.atan2(dy, dx));

        boolean targetReached = goToPosition(closestX, closestY, rotation, 1); 

        return targetReached;
    }

    private double calculateX(double desiredAngle, double speed) {
        return Math.sin(Math.toRadians(desiredAngle)) * speed;
    }

    private double calculateY(double desiredAngle, double speed) {
        return Math.cos(Math.toRadians(desiredAngle)) * speed;
    }

    public void addZoomBox() {
        camera1.addZoomBox();
    }

    public void removeZoomBox() {
        camera1.removeZoomBox();
    }

    public String detectDuck() {
        camera1.setPointerAngle(90);//look right ahead
        String position = "none";
        while (position == "none") {
            position = camera1.detectDuck();
        }
        return position;
    }
}