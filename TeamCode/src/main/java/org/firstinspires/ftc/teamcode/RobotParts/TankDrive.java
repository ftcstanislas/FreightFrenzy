package org.firstinspires.ftc.teamcode.RobotParts;

import java.util.Arrays;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.Map;
import java.util.HashMap;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

// debug, setBrake, setPower, 


public class TankDrive extends RobotPart{
    
    // private Telemetry.Item telemetry = null;
    // public Servo pointer = null;
    // Map<String, DcMotor> motors = new HashMap<String, DcMotor>();
    // Map<String, Servo> servos = new HashMap<String, Servo>();
    String state = "forward";
    double oldTime = 0;
    ElapsedTime runtime = null;

    public void init(HardwareMap map, Telemetry.Item telemetryInit, ElapsedTime timeInit){
        // get motors
        motors.put("left", map.get(DcMotor.class, "leftDrive"));
        motors.get("left").setDirection(DcMotor.Direction.FORWARD);
        motors.put("right", map.get(DcMotor.class, "rightDrive"));
        motors.get("right").setDirection(DcMotor.Direction.REVERSE);
        // motors.put("intake_right", map.get(DcMotor.class, "intake_right"));
        // motors.get("intake_right").setDirection(DcMotor.Direction.FORWARD);
        // motors.put("intake_left", map.get(DcMotor.class, "intake_left"));
        // motors.get("intake_left").setDirection(DcMotor.Direction.REVERSE);
        
        // setup
        telemetry = telemetryInit;
        runtime = timeInit;
    }
    
    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
        // speed
        double speed = gamepad1.left_stick_y;
        double intakeSpeed = gamepad1.right_stick_y;
        // motors.get("intake_left").setPower(intakeSpeed);
        // motors.get("intake_right").setPower(intakeSpeed);
        
        // turning
        double turning = gamepad1.right_trigger - gamepad1.left_trigger;
        turning = turning*Math.abs(turning); //experimental
        if (gamepad1.x){
            turning *= 0.45;
        }
        
        // calculate power
        double power = Math.max(Math.abs(speed), Math.abs(turning));
        if (gamepad1.y) {
            power *= 0.45;
        } else if (gamepad1.b){
            power *= 0.3;
        }
        
        // set power
        setPowerDirection(speed, turning, power);
    }
    
    public void setPowerDirection(double speed, double turn, double power){
        
        //calculate power wheels
        double powerLeft = speed + turn;
        double powerRight = speed - turn;

        //setpower to correct power
        double[] powers = {Math.abs(powerLeft),Math.abs(powerRight)};
        double max = Arrays.stream(powers).max().getAsDouble();
        powerLeft = powerLeft/max*power;
        powerRight = powerRight/max*power;

        //setpowers to motors
        setPowers(powerLeft, powerRight);
    }
    
    private void setPowers(double powerLeft, double powerRight){
        motors.get("left").setPower(powerLeft);
        motors.get("right").setPower(powerRight);
        
        debug();
    }
    
    // langzamer opstarten
    // public void setPowerAccelaration(double x, double y, double turn, double power){
    //     double maxAccelaration = 0.35;
        
    //     double time = runtime.time();
    //     if (time - oldTime > 0.2){
    //         oldTime = time;
    //         double powerLeftFront = motors.leftFrontDrive.getPower();
    //         double powerRightFront = motors.rightFrontDrive.getPower();
    //         double powerLeftBack= motors.leftBackDrive.getPower();
    //         double powerRightBack = motors.rightBackDrive.getPower();
    //         double[] powers = {Math.abs(powerLeftFront),Math.abs(powerRightFront),Math.abs(powerLeftBack),Math.abs(powerRightBack)};
    //         double currentPower = Arrays.stream(powers).max().getAsDouble();
            
            
    //         double powerDifference = power - currentPower;
    //         powerDifference = Math.min(maxAccelaration, powerDifference);
    //         powerDifference = Math.max(-maxAccelaration, powerDifference);
            
    
    //         setPowerDirection(x, y, turn, currentPower + powerDifference);
    //     };

        
    // }
    
    
    public void updateTelemetry(){
        
        // get values
        double powerLeftFront = motors.get("left").getPower();
        double displayPowerLeftFront = Math.round(powerLeftFront*100);
        double powerRightFront = motors.get("right").getPower();
        double displayPowerRightFront = Math.round(powerRightFront*100);
        
        
        // make and display text
        String text = 
        "\n"+"        ______      "
        +"\n "+powerLeftFront/10+" |          | "+powerRightFront/10
        +"\n  |          | "
        +"\n  |          | "
        +"\n"+"        ¯¯¯¯¯¯      ";
        
        // display text
        telemetry.setValue(text);
    }
    
}











