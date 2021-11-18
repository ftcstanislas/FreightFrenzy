package org.firstinspires.ftc.teamcode.Location;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotParts.MecanumDrive;

import java.util.ArrayList;

public class Location {
//    private Odometry odometry = null;
    private IMU IMU = null;
    private Camera camera1 = null;
    private Telemetry.Item telemetry = null;
    private MecanumDrive drivetrain = null;

    //Location
    final int HISTORY_LENGTH = 2;
    ArrayList<Double> historyX = new ArrayList<Double>();
    ArrayList<Double> historyY = new ArrayList<Double>();
    ArrayList<Double> historyHeading = new ArrayList<Double>();
    double x = 0;
    double y = 0;
    double heading = 0;

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
        camera1 = new Camera();
        camera1.init(hardwareMap, "Webcam 1", 1.27, 0.045, telemetryInit, telemetryDucks); // , new float[]{170, 170, 230}
        camera1.updateServoPosition(x, y, heading);
        
        telemetry = telemetryInit;
    }
    
    public void startDuckDetection() {
        camera1.startDuckDetection();
    }

    public void stopDuckDetection() {
        camera1.stopDuckDetection();
    }

    public void update(){
//        odometry.globalCoordinatePositionUpdate();
//        heading = IMU.getOrientation();
        camera1.update();

        // Calculate new position of robot
        double[] positionCamera1 = {17,17};
        double[] locationCamera1 = camera1.getPosition();
        double robotHeadingRadians = Math.toRadians(locationCamera1[2]-180);
        double robotX = positionCamera1[0] * Math.cos(robotHeadingRadians) + positionCamera1[1] * - Math.sin(robotHeadingRadians);
        double robotY = positionCamera1[0] * Math.sin(robotHeadingRadians) + positionCamera1[1] * Math.cos(robotHeadingRadians);

        historyX.add(locationCamera1[0]);
        historyY.add(locationCamera1[1]);
        historyHeading.add(locationCamera1[2]);

        while (historyX.size() > HISTORY_LENGTH){
            historyX.remove(0);
        }
        while (historyY.size() > HISTORY_LENGTH){
            historyY.remove(0);
        }
        while (historyHeading.size() > HISTORY_LENGTH){
            historyHeading.remove(0);
        }
        double[] location = locationCamera1;

        // Update position
        x = location[0];
        y = location[1];
        heading = location[2];

        // Update servo
        camera1.updateServoPosition(location[0], location[1], location[2]);

        telemetry.setValue(String.format("Pos camera (mm) {X, Y, heading} = %.1f, %.1f %.1f\nPos (mm) {X, Y,} = %.1f, %.1f",
                x, y, heading, robotX, robotY));
//        telemetry.setValue(String.format("Pos camera (mm) {X, Y, heading} = %.1f, %.1f %.1f",
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
        double distance = Math.hypot(dx, dy);
        double closestX = midPointX + dx / distance * radius;
        double closestY = midPointY + dy / distance * radius;
        double rotation = Math.toDegrees(Math.atan2(dy, dx));

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
