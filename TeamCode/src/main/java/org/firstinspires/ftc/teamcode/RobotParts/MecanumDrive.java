package org.firstinspires.ftc.teamcode.RobotParts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Location.IMU;
import org.firstinspires.ftc.teamcode.Location.Location;


public class MecanumDrive extends RobotPart{
    Location location = null;
    double lastPower = 0;
    double lastAngle = 0;
    HashMap<Double, String> drawArrowsMap = new HashMap<>();

    public void init(HardwareMap map, Telemetry.Item telemetryInit, Location locationInit){
        // get motors
        motors.put("leftFront", map.get(DcMotorEx.class, "leftFront"));
        motors.get("leftFront").setDirection(DcMotor.Direction.REVERSE);
        motors.put("rightFront", map.get(DcMotorEx.class, "rightFront"));
        motors.get("rightFront").setDirection(DcMotor.Direction.FORWARD);
        motors.put("leftBack", map.get(DcMotorEx.class, "leftBack"));
        motors.get("leftBack").setDirection(DcMotor.Direction.REVERSE);
        motors.put("rightBack", map.get(DcMotorEx.class, "rightBack"));
        motors.get("rightBack").setDirection(DcMotor.Direction.FORWARD);
        
        // telemetry setup
        telemetry = telemetryInit;

        // Orientation draw setup
        // Orientation
        drawArrowsMap.put(0.0, "→");
        drawArrowsMap.put(45.0, "↗");
        drawArrowsMap.put(90.0, "↑");
        drawArrowsMap.put(135.0, "↖");
        drawArrowsMap.put(180.0, "←");
        drawArrowsMap.put(-180.0, "←");
        drawArrowsMap.put(-135.0, "↙");
        drawArrowsMap.put(-90.0, "↓");
        drawArrowsMap.put(-45.0, "↘");

        // location setup
        location = locationInit;
    }
    
    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
        // xy
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;

        // turning
        double turning = gamepad1.left_trigger - gamepad1.right_trigger;
        //turning = turning*Math.abs(turning); //experimental
        if (gamepad1.x){
            turning *= 0.45;
        }
        
        // calculate power
        double power = Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(turning));
        if (gamepad1.y) {
            power *= 0.45;
        } else if (gamepad1.b){
            power *= 0.3;
        }

        // set power
        double MAX_DIFFERENCE = 0.025;
        double difference = Math.max(Math.min(lastPower - power, MAX_DIFFERENCE), MAX_DIFFERENCE);
        double newPower = lastPower+difference;
        double newAngle = Math.atan2(x,y);
        double angleDifference = Math.abs(newAngle-lastAngle);
        newPower *= 1-angleDifference/(Math.PI);
        newPower = power;
        setPowerDirection(x, y, turning, newPower);
        lastAngle = newAngle;
        lastPower = newPower;
    }
    
    public void setPowerDirection(double x, double y, double turn, double power){
        double straal = Math.hypot(x, y);
//        double robotAngle = Math.atan2(-y, -x) - Math.PI / 4 - location.getRotation();
        double robotAngle = Math.atan2(y, x);
//        drawArrows(robotAngle, turn);
        
        // Calculate power wheels
        double calcRobotAngle = robotAngle - 0.25 * Math.PI;
        double powerLeftFront = straal * Math.cos(calcRobotAngle) - turn;
        double powerRightFront = straal * Math.sin(calcRobotAngle) + turn;
        double powerLeftBack = straal * Math.sin(calcRobotAngle) - turn;
        double powerRightBack = straal * Math.cos(calcRobotAngle) + turn;

        // Set powers to cap at 1
        double[] powers = {Math.abs(powerLeftFront),Math.abs(powerRightFront),Math.abs(powerLeftBack),Math.abs(powerRightBack)};
        double max = Arrays.stream(powers).max().getAsDouble();
        powerLeftFront = powerLeftFront/max*power;
        powerRightFront = powerRightFront/max*power;
        powerLeftBack = powerLeftBack/max*power;
        powerRightBack = powerRightBack/max*power;
        
        // Set powers to motors
        setPowers(powerLeftFront, powerRightFront, powerLeftBack, powerRightBack);
    }
    
    private void setPowers(double powerLeftFront, double powerRightFront, double powerLeftBack, double powerRightBack){
        motors.get("leftFront").setPower(powerLeftFront);
        motors.get("rightFront").setPower(powerRightFront);
        motors.get("leftBack").setPower(powerLeftBack);
        motors.get("rightBack").setPower(powerRightBack);

        updateTelemetry();
    }

    public void pause(){
        setPower(0);
    }
    
    public void updateTelemetry(){
        double powerLeftFront = motors.get("leftFront").getPower();
        double powerRightFront = motors.get("rightFront").getPower();
        double powerRightBack = motors.get("rightBack").getPower();

        double turn = (powerRightBack - powerLeftFront) / 2;

        // powers without turning
        powerLeftFront += turn;
        powerRightFront -= turn;
        powerRightBack -= turn;


        double dif = (powerRightBack - powerRightFront) / 2 ;
        double orientation = Math.toDegrees(Math.asin(dif));
        if (powerLeftFront + powerRightFront > 0) {
            if (orientation > 0) {
                orientation = 180 - orientation;
            } else {
                orientation = -180 - orientation;
            }
        }

        drawArrows(Math.toRadians(orientation), turn);
    }

    public void drawArrows(double orientationRadians, double turn){
        double orientation = Math.toDegrees(orientationRadians);
        String text = "";

        // Orientation
        double key = nearestKey(drawArrowsMap, orientation);
        text += drawArrowsMap.get(key);

        // Turn
        if (turn < 0){
            text += "⟳";
        } else if (turn > 0){
            text += "⟲";
        } else {
            text += "╳";
        }

        telemetry.setValue(orientation+"\n"+turn+"\n"+text);
    }

    private double nearestKey(Map<Double, String> map, Double target) {
        double minDiff = Double.MAX_VALUE;
        double nearest = 0;
        for (double key : map.keySet()) {
            double diff = Math.abs((double) target - (double) key);
            if (diff < minDiff) {
                nearest = key;
                minDiff = diff;
            }
        }
        return nearest;
    }
}











