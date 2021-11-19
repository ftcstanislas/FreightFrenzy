package org.firstinspires.ftc.teamcode.Location;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotParts.MecanumDrive;

import java.util.ArrayList;

public class Location {
//    private Odometry odometry = null;
    private IMU IMU = null;
    private Camera camera1 = null;
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

    public void init(HardwareMap hardwareMap, int number, boolean advancedInit, double[] position, MecanumDrive drivetrainInit, Telemetry.Item telemetryInit, Telemetry.Item telemetryDucks){
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
            // Camera 1
            camera1 = new Camera();
            if (number == 1) {
                camera1.init(hardwareMap, "1", 1.32, -0.028, telemetryInit, telemetryDucks); // , new float[]{170, 170, 230}
            } else {
                camera1.init(hardwareMap, "2", 1.32, 0.15, telemetryInit, telemetryDucks);
            }
            camera1.setPointerPosition(x, y, heading);
        }
        
        telemetry = telemetryInit;
    }
    
    public void startDuckDetection() {
        camera1.startDuckDetection();
    }

    public void stopDuckDetection() {
        camera1.stopDuckDetection();
    }

    public void update(){
        // Update heading
        heading = IMU.getHeading();

        // Camera
        double robotX = 0;
        double robotY = 0;
        if (advanced) {
            double robotHeadingRadians = Math.toRadians(heading - 180);

            // Camera 1
            camera1.update();

            // Calculate new position of robot
            double[] positionCamera1 = {17, -17};
            double[] locationCamera1 = camera1.getPosition();
            robotX = positionCamera1[0] * Math.cos(robotHeadingRadians) + positionCamera1[1] * -Math.sin(robotHeadingRadians);
            robotY = positionCamera1[0] * Math.sin(robotHeadingRadians) + positionCamera1[1] * Math.cos(robotHeadingRadians);

            if (camera1.isTargetVisible()) {
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
            double[] positionCamera1 = {17, -17};
            robotX = positionCamera1[0] * Math.cos(robotHeadingRadians) + positionCamera1[1] * -Math.sin(robotHeadingRadians);
            robotY = positionCamera1[0] * Math.sin(robotHeadingRadians) + positionCamera1[1] * Math.cos(robotHeadingRadians);
            camera1.setPointerPosition(x-robotX, y-robotY, heading);
        }

        telemetry.setValue(String.format("Pos camera (mm) {X, Y, heading} = %.1f, %.1f %.1f\nPos (mm) {X, Y,} = %.1f, %.1f",
                x, y, heading, robotX, robotY));
//        telemetry.setValue(String.format("Pos camera1 (mm) {X, Y, heading} = %.1f, %.1f %.1f",
//                x, y, heading));



        //Duck
//        camera.setZoom(true);
//        camera.detectDuck();
//        telemetry.setValue(odometry.getDisplay()+"\n"+IMU.getDisplay());
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

            drivetrain.setPowerDirection(robotMovementXComponent, -robotMovementYComponent, turning, power);

            return false;
        } else {
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
}
