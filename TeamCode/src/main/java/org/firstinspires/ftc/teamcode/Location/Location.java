package org.firstinspires.ftc.teamcode.Location;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Location {
    private Odometry odometry = null;
    private IMU IMU = null;
//    private Camera camera = null;
    private Telemetry.Item telemetry = null;

    public void init(HardwareMap hardwareMap, Telemetry.Item telemetryInit){
        // Odometry
        odometry = new Odometry(
                hardwareMap.get(DcMotor.class, "leftFront"),
                hardwareMap.get(DcMotor.class, "rightFront"),
                hardwareMap.get(DcMotor.class, "leftBack"),
//                hardwareMap.get(Servo.class, "encoder"),
                2048 * 9 * Math.PI);
        odometry.setPosition(0,0,0);

        // IMU
        IMU = new IMU();
        IMU.init(hardwareMap);

        // Camera
//        camera = new Camera();
//        camera.init(hardwareMap, telemetryInit);

        
        telemetry = telemetryInit;
    }

    public void update(){
        odometry.globalCoordinatePositionUpdate();
        telemetry.setValue(odometry.getDisplay()+"\n"+IMU.getDisplay());
    }

    public double getOrientation(){
        return IMU.getOrientation();
    }

    public boolean goToPosition(double targetXPosition, double targetYPosition, double robotPower, double desiredRobotOrientation, double allowableDistanceError){
//        double distanceToXTarget = targetXPosition - globalPositionUpdate.returnXCoordinate();
//        double distanceToYTarget = targetYPosition - globalPositionUpdate.returnYCoordinate();
//        double distance = Math.hypot(distanceToXTarget, distanceToYTarget);
//        double pivotCorrection = desiredRobotOrientation - getOrientation();
//
//        if (distance > allowableDistanceError || Math.abs(pivotCorrection) > 5){
//            double robotMovementAngle = Math.toDegrees(Math.atan2(distanceToXTarget, distanceToYTarget)) - globalPositionUpdate.returnOrientation();
//            double robotMovementXComponent = calculateX(robotMovementAngle, robotPower);
//            double robotMovementYComponent = calculateY(robotMovementAngle, robotPower);
//
//            // Slow down
//            double slowDownFrom = 10000;
//            double accelerateTo = 3000;
//            double accMinPower = 0.5;
//            double minPower = 0.3;
//
//            if (distance < slowDownFrom){
//                robotPower = minPower + (robotPower-minPower) * Math.abs(distance)/slowDownFrom;
//            }
//
//            if (Math.abs(startingDistance - distance) <= accelerateTo) {
//                robotPower = accMinPower + (robotPower-accMinPower) * Math.abs(startingDistance - distance) / accelerateTo;
//            }
//
//            // Check if stuck
//            if (stuck > 30){
//                telemetry.setValue("Stuck, distance = "+Math.round(distance)+" counts");
//                stuck = 0;
//                drivetrain.setPowerDirection(0, 0, 0, 0);
//
//                acceleration = false;
//
//                return true;
//            } else if (Math.abs(distance - previousDistance) < Math.abs(20*robotPower)){
//                stuck += 1;
//            } else {
//                stuck = 0;
//            }
//            previousDistance = distance;
//
//            pivotCorrection = 100 * pivotCorrection/distance;//Math.min(0.25,Math.max(-0.25,pivotCorrection/45));
//
//            // telemetry.setValue("going to = ("+Math.round(robotMovementXComponent*10)+";"+Math.round(robotMovementYComponent*10)+") "+Math.round(pivotCorrection)+"° with "+Math.round(robotPower*10)+", distance = "+Math.round(distance)+" counts");
//            drivetrain.setPowerDirection(-robotMovementXComponent, -robotMovementYComponent, pivotCorrection, robotPower);
//
//            return false;
//        } else {
//            telemetry.setValue("Done, distance = "+Math.round(distance)+" counts");
//            drivetrain.setPowerDirection(0, 0, 0, 0);
//
//            acceleration = false;
//
//            return true;
//        }
        return false;
    }

    /**
     * Calculate the power in the x direction
     * @param desiredAngle angle on the x axis
     * @param speed robot's speed
     * @return the x vector
     */
    private double calculateX(double desiredAngle, double speed) {
        return Math.sin(Math.toRadians(desiredAngle)) * speed;
    }

    /**
     * Calculate the power in the y direction
     * @param desiredAngle angle on the y axis
     * @param speed robot's speed
     * @return the y vector
     */
    private double calculateY(double desiredAngle, double speed) {
        return Math.cos(Math.toRadians(desiredAngle)) * speed;
    }

//    public void switchServo() {
//        odometry.switchServo();
//    }
}
