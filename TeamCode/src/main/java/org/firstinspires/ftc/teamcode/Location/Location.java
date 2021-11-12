package org.firstinspires.ftc.teamcode.Location;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Location {
//    private Odometry odometry = null;
    private IMU IMU = null;
    private Camera camera = null;
    private Telemetry.Item telemetry = null;
    private MecanumDrive drivetrain = null;

    //Location


    public void init(HardwareMap hardwareMap, MecanumDrive drivetrainInit, Telemetry.Item telemetryInit, Telemetry.Item telemetryDucks){
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

        // Camera
        camera = new Camera();
        camera.init(hardwareMap, telemetryInit, telemetryDucks);
        
        telemetry = telemetryInit;
    }
    
    public void startDuckDetection() {
        camera.startDuckDetection();
    }

    public void stopDuckDetection() {
        camera.stopDuckDetection();
    }

    public void update(){
//        odometry.globalCoordinatePositionUpdate();
        camera.update();

        //Duck
//        camera.setZoom(true);
//        camera.detectDuck();
//        telemetry.setValue(odometry.getDisplay()+"\n"+IMU.getDisplay());
    }

    public void stop(){
        camera.stop();
    }

    public double getXCoordinate() {
        return 0;
    }

    public double getYCoordinate() {
        return 0;
    }

    public double getOrientation(){
        return IMU.getOrientation();
    }

    public boolean goToPosition(double targetX, double targetY, double targetRotation, double power) {
        //Constants
        double allowableDistanceError = 10; // mm?

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

            drivetrain.setPowerDirection(robotMovementXComponent, robotMovementYComponent, turning, power);

            return false;
        } else {
            return true;
        }
    }

    public boolean goToCircle(double radius, double[] midPoint) {
        double currentX = getXCoordinate();
        double currentY = getYCoordinate();
        double midPointX = midPoint[0];
        double midPointY = midPoint[1];
        double dx = currentX - midPointX;
        double dy = currentY - midPointY;
        double distance = Math.sqrt(dx**2 + dy**2);
        double closestX = midPointX + dx / distance * radius;
        double closestY = midPointY + dy / distance * radius;
        double rotation = Math.toDegrees(Math.atan(dx, dy));

        boolean targetReached = goToPosition(closestX, closestY, rotation, 1); 

        return targetReached; //Remove after
    }

    private double calculateX(double desiredAngle, double speed) {
        return Math.sin(Math.toRadians(desiredAngle)) * speed;
    }

    private double calculateY(double desiredAngle, double speed) {
        return Math.cos(Math.toRadians(desiredAngle)) * speed;
    }
}
