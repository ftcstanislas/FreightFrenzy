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
import org.firstinspires.ftc.teamcode.Location.Start;


public class MecanumDrive extends RobotPart{
    Location location = null;
    double lastPower = 0;
    double lastAngle = 0;
    Start.TeamColor teamColor;
    HashMap<Double, String> drawArrowsMap = new HashMap<>();

    public void init(HardwareMap map, Telemetry.Item telemetryInit, Location locationInit, Start.TeamColor teamColorInit){
        teamColor = teamColorInit;
        // get motors
        motors.put("leftFront", map.get(DcMotorEx.class, "leftFront"));
        motors.get("leftFront").setDirection(DcMotor.Direction.REVERSE);
        motors.get("leftFront").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors.put("rightFront", map.get(DcMotorEx.class, "rightFront"));
        motors.get("rightFront").setDirection(DcMotor.Direction.FORWARD);
        motors.get("rightFront").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors.put("leftBack", map.get(DcMotorEx.class, "leftBack"));
        motors.get("leftBack").setDirection(DcMotor.Direction.REVERSE);
        motors.get("leftBack").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors.put("rightBack", map.get(DcMotorEx.class, "rightBack"));
        motors.get("rightBack").setDirection(DcMotor.Direction.FORWARD);
        motors.get("rightBack").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
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
        double x = 0;
        double y = 0;
        double power = 0;

        if (Math.abs(gamepad1.left_stick_x) > 0 || Math.abs(gamepad1.left_stick_y) > 0){
            // Normal controls
            x = gamepad1.left_stick_x;
            y = -gamepad1.left_stick_y;
            power = Math.sqrt(x*x + y*y); // Hypotenuse
        } else if (Math.abs(gamepad1.right_stick_x) > 0 || Math.abs(gamepad1.right_stick_y) > 0) {
            // Headless controls
            double robotAngle;
            if (teamColor == Start.TeamColor.RED) {
                robotAngle = Math.atan2(-gamepad1.right_stick_y, gamepad1.right_stick_x) - Math.toRadians(location.getOrientation()) + 0.5 * Math.PI; // Add 0.5π so 0 is forward
            } else {
                robotAngle = Math.atan2(gamepad1.right_stick_y, -gamepad1.right_stick_x) - Math.toRadians(location.getOrientation()) + 0.5 * Math.PI; // Add 0.5π so 0 is forward
            }
            x = Math.cos(robotAngle);
            y = Math.sin(robotAngle);
            power = Math.sqrt(Math.pow(gamepad1.right_stick_x, 2) + Math.pow(gamepad1.right_stick_y, 2)); // Hypotenuse
        }

        // Turning 
        double turning = gamepad1.left_trigger - gamepad1.right_trigger;

        // Slower turning
        if (gamepad1.x){
            turning *= 0.45;
        }
        power = Math.max(power, Math.abs(turning));
        
        // Slow robot speed
        if (gamepad1.a || gamepad1.dpad_down) {
            power *= 0.45;
        } else if (gamepad1.x || gamepad1.dpad_right){
            power *= 0.3;
        }

        // Set motor to powers
        setPowerDirection(x, y, turning, power);
    }
    
    // Set power to motors
    public void setPowerDirection(double x, double y, double turn, double power){
        power = Math.min(Math.max(power, -1), 1);
        double straal = Math.hypot(x, y);
        double robotAngle = Math.atan2(y, x);
        drawArrows(x, y, turn);
        
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

    public void stopDriving(){
        setPower(0);
    }
    
    public void updateTelemetry(){
//        double powerLeftFront = motors.get("leftFront").getPower();
//        double powerRightFront = motors.get("rightFront").getPower();
//        double powerRightBack = motors.get("rightBack").getPower();
//
//        double turn = (powerRightBack - powerLeftFront) / 2;
//
//        // powers without turning
//        powerLeftFront += turn;
//        powerRightFront -= turn;
//        powerRightBack -= turn;
//
//        double orientation = Math.toDegrees(Math.atan2(powerRightFront, powerLeftFront)) + 45;
//        if (orientation <= -180) {
//            orientation += 360;
//        } else if (orientation > 180){
//            orientation -= 360;
//        }
//
//        orientation = Math.toRadians(orientation);
//        double x = Math.cos(orientation);
//        double y = Math.sin(orientation);
//
//        drawArrows(x, y, turn);
    }

    // Draw arrows to indicate to which side the robot is moving (or turning)
    public void drawArrows(double x, double y, double turn){
        double orientation = Math.toDegrees(Math.atan2(y, x));
        String text = "";

        // Orientation
        if ((x == 0 && y == 0) || Double.isNaN(x) || Double.isNaN(y)) {
            text += "╳";
        } else {
            double key = nearestKey(drawArrowsMap, orientation);
            text += drawArrowsMap.get(key);
        }

        // Turn
        if (turn < 0){
            text += "⟳";
        } else if (turn > 0){
            text += "⟲";
        } else {
            text += "╳";
        }

        telemetry.setValue(text);
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











