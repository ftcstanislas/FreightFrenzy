package org.firstinspires.ftc.teamcode.Final;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Odometry_Sample.OdometryGlobalCoordinatePosition;


public class DriveTo {
    
    public PushBot motors = null;
    public Drivetrain drivetrain = null;
    Telemetry.Item telemetry = null;
    OdometryGlobalCoordinatePosition globalPositionUpdate;
    
    public void init(PushBot motorsInit, Drivetrain drivetrainInit, Telemetry.Item telemetryInit, OdometryGlobalCoordinatePosition globalPositionUpdateInit) {
        motors = motorsInit;
        drivetrain = drivetrainInit;
        telemetry = telemetryInit;
        globalPositionUpdate = globalPositionUpdateInit;
    }
    
    double diameterWheel = 9; // In centimeters
    double circumference = (Math.PI * diameterWheel) / 8192; // In unit of moter
    final double COUNTS_PER_CM = 8192 / (Math.PI / diameterWheel);
    
    // save previous distance
    double previousDistance;
    double stuck;
    
    //Acceleration (test, delete if not used)
    boolean acceleration = false;
    double startingDistance;
    
    // public boolean goToPosition(double goToX, double goToY) {
    //     // current position
    //     double positionX = -motors.rightBackDrive.getCurrentPosition() * circumference;
    //     double positionX2 = -motors.leftBackDrive.getCurrentPosition() * circumference;
    //     double positionY = -motors.rightFrontDrive.getCurrentPosition() * circumference;
        
    //     telemetry.setValue("("+positionX+","+positionY+")");
        
    //     // relative position of target from robot
    //     double relativeX = goToX - positionX;
    //     double relativeY = goToY - positionY;
        
    //     double x = -relativeX;
    //     double y = -relativeY;
    //     double straal = Math.hypot(x, y);
        
    //     // slow down
    //     double slowDownFrom = 10;
    //     double stopAt = 2;
    //     double power = 0.7;
    //     double minPower = 0.2;
        
    //     if (straal < slowDownFrom){
    //         power = minPower+(power-minPower)*(Math.abs(straal)/slowDownFrom);
    //     }
        
        
    //     // update drivetrain
    //     if (straal < stopAt) {
    //         drivetrain.setPowerDirection(x, y, 0, 0);
    //         return true;
    //     } else {
    //         double turn = Math.min(0.8,Math.max(-0.8,(positionX-positionX2)*1000000000));
    //         drivetrain.setPowerAccelaration(x, y, turn, power);
    //         return false;
    //     }
    // }
    
    // public boolean goToPositionNew(double targetXPosition, double targetYPosition, double robotPower, double desiredRobotOrientation, double allowableDistanceError){
        
    //     double rotation = globalPositionUpdate.returnOrientation();
        
    //     //Coordinates relative to rotation
    //     // targetXPosition = targetXPosition * Math.cos(desiredRobotOrientation) + targetYPosition * -Math.sin(desiredRobotOrientation);
    //     // targetYPosition = targetXPosition * Math.sin(desiredRobotOrientation) + targetYPosition * Math.cos(desiredRobotOrientation);
        
    //     double distanceToXTarget = targetXPosition - globalPositionUpdate.returnXCoordinate();
    //     double distanceToYTarget = targetYPosition - globalPositionUpdate.returnYCoordinate();
        
    //     double distance = Math.hypot(distanceToXTarget, distanceToYTarget);
        
    //     if (distance > allowableDistanceError){
            
    //         // moet dit toegevoegd worden? + globalPositionUpdate.returnOrientation()
    //         double robotMovementAngle = Math.toDegrees(Math.atan2(distanceToXTarget, distanceToYTarget));

    //         double robotMovementXComponent = calculateX(robotMovementAngle, robotPower);
    //         double robotMovementYComponent = calculateY(robotMovementAngle, robotPower);
    //         double pivotCorrection = desiredRobotOrientation - globalPositionUpdate.returnOrientation();
            
    //         //testen
    //         double pivot = (desiredRobotOrientation - globalPositionUpdate.returnOrientation()) / 10;
            
    //         pivotCorrection = Math.min(0.4,Math.max(-0.4,pivotCorrection/10));
            
    //         if (pivot < 2.5 && pivot > -2.5) {
    //             targetXPosition = targetXPosition * Math.cos(desiredRobotOrientation) + targetYPosition * -Math.sin(desiredRobotOrientation);
    //             targetYPosition = targetXPosition * Math.sin(desiredRobotOrientation) + targetYPosition * Math.cos(desiredRobotOrientation);
    //             // drivetrain.setPowerDirection(0, 0, 0, 0);
    //         }
            
    //         // slow down
    //         double slowDownFrom = 20000;
    //         double minPower = 0.2;
            
    //         if (distance < slowDownFrom){
    //             robotPower = minPower+(robotPower-minPower)*(Math.abs(distance)/slowDownFrom);
    //         }
        
    //         // telemetry.setValue("location = ("+Math.round(robotMovementXComponent*10)+";"+Math.round(robotMovementYComponent*10)+") "+pivotCorrection+"°, distance = "+Math.round(distance)+" counts");
    //         telemetry.setValue("target: " + targetXPosition + ", " + targetYPosition + ", pivot: " + -pivotCorrection);
    //         drivetrain.setPowerAccelaration(-robotMovementXComponent, -robotMovementYComponent, -pivotCorrection, robotPower);
            
    //         return false;
    //     } else {
    //         telemetry.setValue("Done, distance = "+Math.round(distance)+" counts");
    //         drivetrain.setPowerDirection(0, 0, 0, 0);
    //         return true;
    //     }
    // }
    
    // public boolean goToPositionNew2(double targetXPosition, double targetYPosition, double robotPower, double desiredRobotOrientation, double allowableDistanceError){
        
    //     double distanceToXTarget = targetXPosition - globalPositionUpdate.returnXCoordinate();
    //     double distanceToYTarget = targetYPosition - globalPositionUpdate.returnYCoordinate();
        
    //     double distance = Math.hypot(distanceToXTarget, distanceToYTarget);
        
    //     double pivotCorrection = desiredRobotOrientation - globalPositionUpdate.returnOrientation();
        
    //     if (distance < allowableDistanceError && Math.abs(pivotCorrection) < 10){
    //         telemetry.setValue("Done, distance is "+Math.round(distance)+" counts");
    //         drivetrain.setPowerDirection(0, 0, 0, 0);
            
    //         return true;
    //     } else if (Math.abs(pivotCorrection) > 15){
            
    //         // slow down rotation
    //         double slowDownFrom = 90;
    //         double minPower = 0.2;
    //         if (pivotCorrection < slowDownFrom){
    //             robotPower = minPower+(robotPower-minPower)*(Math.abs(pivotCorrection)/slowDownFrom);
    //         }
            
    //         // set powers
    //         drivetrain.setPowerAccelaration(0, 0, -pivotCorrection, robotPower);
            
    //         //telemetry
    //         telemetry.setValue("Rotating to "+Math.round(desiredRobotOrientation)+"°, distance is "+Math.round(pivotCorrection)+"° ");
            
    //         return false;
    //     } else {
            
    //         // movement angle relative to rotation
    //         double robotMovementAngle = Math.toDegrees(Math.atan2(distanceToXTarget, distanceToYTarget)) + globalPositionUpdate.returnOrientation();
            
    //         // movement components
    //         double robotMovementXComponent = calculateX(robotMovementAngle, robotPower);
    //         double robotMovementYComponent = calculateY(robotMovementAngle, robotPower);
    //         pivotCorrection = Math.min(0.5,Math.max(-0.5, pivotCorrection/20));
            
    //         // slow down driving
    //         double slowDownFrom = 2000;
    //         double minPower = 0.2;
    //         if (distance < slowDownFrom){
    //             robotPower = minPower+(robotPower-minPower)*(Math.abs(distance)/slowDownFrom);
    //         }
            
    //         // set powers
    //         drivetrain.setPowerAccelaration(-robotMovementXComponent, -robotMovementYComponent, -pivotCorrection, robotPower);
            
    //         // telemetry
    //         telemetry.setValue("Driving to ("+Math.round(robotMovementXComponent*10)+";"+Math.round(robotMovementYComponent*10)+") "+Math.round(pivotCorrection*10)+"°, distance is "+Math.round(distance)+" counts");
            
    //         return false;
    //     }
    // }
    
    public boolean goToPositionNew3(double targetXPosition, double targetYPosition, double robotPower, double desiredRobotOrientation, double allowableDistanceError){
        double rotation = globalPositionUpdate.returnOrientation();
        
        double distanceToXTarget = targetXPosition - globalPositionUpdate.returnXCoordinate();
        double distanceToYTarget = targetYPosition - globalPositionUpdate.returnYCoordinate();
        
        double distance = Math.hypot(distanceToXTarget, distanceToYTarget);
        
        //Acceleration (test, delete if not used)
        if (acceleration == false) {
            startingDistance = distance;
            acceleration = true;
        }
        
        double pivotCorrection = desiredRobotOrientation - globalPositionUpdate.returnOrientation();
        
        if (distance > allowableDistanceError || Math.abs(pivotCorrection) > 5){
            double robotMovementAngle = Math.toDegrees(Math.atan2(distanceToXTarget, distanceToYTarget)) - globalPositionUpdate.returnOrientation();
            double robotMovementXComponent = calculateX(robotMovementAngle, robotPower);
            double robotMovementYComponent = calculateY(robotMovementAngle, robotPower);
            
            
            // slow down
            double slowDownFrom = 10000;
            double accelerateTo = 3000;
            double accMinPower = 0.5;
            double minPower = 0.3;
            
            if (distance < slowDownFrom){
                robotPower = minPower + (robotPower-minPower) * Math.abs(distance)/slowDownFrom;
            }
            
            if (Math.abs(startingDistance - distance) <= accelerateTo) {
                robotPower = accMinPower + (robotPower-accMinPower) * Math.abs(startingDistance - distance) / accelerateTo;
            }
            
            // check if stuck
            if (stuck > 30){
                telemetry.setValue("Stuck, distance = "+Math.round(distance)+" counts");
                stuck = 0;
                drivetrain.setPowerDirection(0, 0, 0, 0);
                
                acceleration = false;
                
                return true;
            } else if (Math.abs(distance - previousDistance) < Math.abs(20*robotPower)){
                stuck += 1;
            } else {
                stuck = 0;
            }
            previousDistance = distance;
            
            pivotCorrection = 100*pivotCorrection/distance;//Math.min(0.25,Math.max(-0.25,pivotCorrection/45));
        
            telemetry.setValue("going to = ("+Math.round(robotMovementXComponent*10)+";"+Math.round(robotMovementYComponent*10)+") "+Math.round(pivotCorrection)+"° with "+Math.round(robotPower*10)+", distance = "+Math.round(distance)+" counts");
            drivetrain.setPowerDirection(-robotMovementXComponent, -robotMovementYComponent, pivotCorrection, robotPower);

            return false;
        } else {
            telemetry.setValue("Done, distance = "+Math.round(distance)+" counts");
            drivetrain.setPowerDirection(0, 0, 0, 0);
            
            acceleration = false;
            
            return true;
        }
    }
    
    public boolean goToCheckpoint(double targetXPosition, double targetYPosition, double robotPower, double desiredRobotOrientation, double allowableDistanceError){
        double rotation = globalPositionUpdate.returnOrientation();
        
        double distanceToXTarget = targetXPosition - globalPositionUpdate.returnXCoordinate();
        double distanceToYTarget = targetYPosition - globalPositionUpdate.returnYCoordinate();
        
        double distance = Math.hypot(distanceToXTarget, distanceToYTarget);
        
        //Acceleration (test, delete if not used)
        if (acceleration == false) {
            startingDistance = distance;
            acceleration = true;
        }
        
        double pivotCorrection = desiredRobotOrientation - globalPositionUpdate.returnOrientation();
        
        if (distance > allowableDistanceError || Math.abs(pivotCorrection) > 5){
            
            // moet dit toegevoegd worden? 
            double robotMovementAngle = Math.toDegrees(Math.atan2(distanceToXTarget, distanceToYTarget)) - globalPositionUpdate.returnOrientation();

            double robotMovementXComponent = calculateX(robotMovementAngle, robotPower);
            double robotMovementYComponent = calculateY(robotMovementAngle, robotPower);
            
            
            // slow down
            double accelerateTo = 3000;
            double accMinPower = 0.5;
            
            if (Math.abs(startingDistance - distance) <= accelerateTo) {
                robotPower = accMinPower + (robotPower-accMinPower) * Math.abs(startingDistance - distance) / accelerateTo;
            }
            
            // check if stuck
            if (stuck > 30){
                telemetry.setValue("Stuck, distance = "+Math.round(distance)+" counts");
                stuck = 0;
                drivetrain.setPowerDirection(0, 0, 0, 0);
                
                acceleration = false;
                
                return true;
            } else if (Math.abs(distance - previousDistance) < Math.abs(20*robotPower)){
                stuck += 1;
            } else {
                stuck = 0;
            }
            previousDistance = distance;
            
            pivotCorrection = 100*pivotCorrection/distance;//Math.min(0.25,Math.max(-0.25,pivotCorrection/45));
        
            telemetry.setValue("going to = ("+Math.round(robotMovementXComponent*10)+";"+Math.round(robotMovementYComponent*10)+") "+Math.round(pivotCorrection)+"° with "+Math.round(robotPower*10)+", distance = "+Math.round(distance)+" counts");
            drivetrain.setPowerDirection(-robotMovementXComponent, -robotMovementYComponent, pivotCorrection, robotPower);

            return false;
        } else {
            telemetry.setValue("Done, distance = "+Math.round(distance)+" counts");
            drivetrain.setPowerDirection(0, 0, 0, 0);
            
            acceleration = false;
            
            return true;
        }
    }
    
    public boolean goToOrientation(double desiredRobotOrientation, double robotPower, double allowableDistanceError){
        
        double rotation = globalPositionUpdate.returnOrientation();
        double pivotCorrection = desiredRobotOrientation - globalPositionUpdate.returnOrientation();
        pivotCorrection = pivotCorrection / 15;
        
        if (Math.abs(pivotCorrection) > allowableDistanceError){
            
            
            // slow down
            double slowDownFrom = 20;
            double minPower = 0.25;
            
            if (Math.abs(pivotCorrection) < slowDownFrom){
                robotPower = minPower + (robotPower-minPower) * Math.abs(pivotCorrection)/slowDownFrom;
            }
            
            // pivotCorrection = -Math.min(0.4,Math.max(-0.4,pivotCorrection/15));
            pivotCorrection *= -1;
            
            telemetry.setValue("Turning to "+Math.round(desiredRobotOrientation)+"° with "+Math.round(robotPower*10)+", distance = "+Math.round(pivotCorrection)+"°");
            drivetrain.setPowerDirection(0, 0, pivotCorrection, robotPower);

            return false;
        } else {
            telemetry.setValue("Done, distance = "+Math.round(pivotCorrection)+"° to " + Math.round(desiredRobotOrientation));
            drivetrain.setPowerDirection(0, 0, 0, 0);
            return true;
        }
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
    
}
