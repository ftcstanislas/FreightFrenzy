package org.firstinspires.ftc.teamcode.RobotParts;

import java.util.Arrays;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.Map;
import java.util.HashMap;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class MecanumDrive extends RobotPart{
    Location location = null;

    public void init(HardwareMap map, Telemetry.Item telemetryInit, Location locationInit){
        // get motors
        motors.put("leftFront", map.get(DcMotor.class, "leftFront"));
        motors.get("leftFront").setDirection(DcMotor.Direction.FORWARD);
        motors.put("rightFront", map.get(DcMotor.class, "rightFront"));
        motors.get("rightFront").setDirection(DcMotor.Direction.REVERSE);
        motors.put("leftBack", map.get(DcMotor.class, "leftBack"));
        motors.get("leftBack").setDirection(DcMotor.Direction.FORWARD);
        motors.put("rightBack", map.get(DcMotor.class, "rightBack"));
        motors.get("rightBack").setDirection(DcMotor.Direction.REVERSE);
        
        // telemetry setup
        telemetry = telemetryInit;

        // location setup
        location = locationInit;
    }
    
    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
        // xy
        double x = gamepad1.left_stick_x; 
        double y = -gamepad1.left_stick_y;

        // turning
        double turning = gamepad1.left_trigger - gamepad1.right_trigger;

        turning = turning*Math.abs(turning); //experimental
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
        setPowerDirection(x, y, turning, power);
    }
    
    public void setPowerDirection(double x, double y, double turn, double power){
        double straal = Math.hypot(x, y);
        double robotAngle = Math.atan2(-y, -x) - Math.PI / 4 - location.getRotation();
        
        //calculate power wheels
        double powerLeftFront = straal * Math.cos(robotAngle) + turn;
        double powerRightFront = straal * Math.sin(robotAngle) - turn;
        double powerLeftBack = straal * Math.sin(robotAngle) + turn;
        double powerRightBack = straal * Math.cos(robotAngle) - turn;

        //setpower to correct power
        double[] powers = {Math.abs(powerLeftFront),Math.abs(powerRightFront),Math.abs(powerLeftBack),Math.abs(powerRightBack)};
        double max = Arrays.stream(powers).max().getAsDouble();
        powerLeftFront = powerLeftFront/max*power;
        powerRightFront = powerRightFront/max*power;
        powerLeftBack = powerLeftBack/max*power;
        powerRightBack = powerRightBack/max*power;
        
        //setpowers to motors
        setPowers(powerLeftFront, powerRightFront, powerLeftBack, powerRightBack);
    }
    
    private void setPowers(double powerLeftFront, double powerRightFront, double powerLeftBack, double powerRightBack){
        motors.get("leftFront").setPower(powerLeftFront);
        motors.get("rightFront").setPower(powerRightFront);
        motors.get("leftBack").setPower(powerLeftBack);
        motors.get("rightBack").setPower(powerRightBack);

        updateTelemetry();
    }
    
    public void updateTelemetry(){
        debug();
        return;

        // get values
//        double powerLeftFront = motors.get("leftFront").getPower();
//        double displayPowerLeftFront = Math.round(powerLeftFront*100);
//        double powerRightFront = motors.get("rightFront").getPower();
//        double displayPowerRightFront = Math.round(powerRightFront*100);
//        double powerLeftBack= motors.get("leftBack").getPower();
//        double displayPowerLeftBack = Math.round(powerLeftBack*100);
//        double powerRightBack = motors.get("rightBack").getPower();
//        double displayPowerRightBack = Math.round(powerRightBack*100);
//        double[] powers = {Math.abs(powerLeftFront),Math.abs(powerRightFront),Math.abs(powerLeftBack),Math.abs(powerRightBack)};
//        double power = Arrays.stream(powers).max().getAsDouble();
//        double displayPower = Math.round(power*100);
//
//
//        // make and display text
//        String text =
//        "\n"+"        ______      "
//        +"\n "+displayPowerLeftFront/10+" |          | "+displayPowerRightFront/10
//        +"\n"+"        |  "+displayPower/10+"  |      "
//        +"\n "+displayPowerLeftBack/10+" |          | "+displayPowerRightBack/10
//        +"\n"+"        ¯¯¯¯¯¯      ";
//
//        // display text
//        telemetry.setValue(text);
    }
}











