package org.firstinspires.ftc.teamcode.RobotParts;

import java.util.Arrays;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.Map;
import java.util.HashMap;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class DrivetrainOld {
    
    private Telemetry.Item telemetry = null;
    // public Servo pointer = null;
    Map<String, DcMotor> motors = new HashMap<String, DcMotor>();
    Map<String, Servo> servos = new HashMap<String, Servo>();
    String state = "forward";
    double oldTime = 0;
    ElapsedTime runtime = null;

    public void init(HardwareMap map, Telemetry.Item telemetryInit, ElapsedTime timeInit){
        // get motors
        motors.put("leftFront", map.get(DcMotor.class, "left_front"));
        motors.get("leftFront").setDirection(DcMotor.Direction.REVERSE);
        motors.put("rightFront", map.get(DcMotor.class, "right_front"));
        motors.get("rightFront").setDirection(DcMotor.Direction.FORWARD);
        motors.put("leftBack", map.get(DcMotor.class, "left_back"));
        motors.get("leftBack").setDirection(DcMotor.Direction.REVERSE);
        motors.put("rightBack", map.get(DcMotor.class, "right_back"));
        motors.get("rightBack").setDirection(DcMotor.Direction.FORWARD);
        
        // servos.put("pointer", map.get(Servo.class, "pointer"));
        // servos.get("pointer").setDirection(Servo.Direction.FORWARD);
        // servos.put("i_am_SPEED", map.get(Servo.class, "i_am_speed"));
        // servos.get("i_am_SPEED").setDirection(Servo.Direction.FORWARD);
        // pointer = map.get(Servo.class, "pointer");
        // pointer.setDirection(Servo.Direction.FORWARD);
        
        // setup
        telemetry = telemetryInit;
        runtime = timeInit;
    }
    
    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
        // xy
        double x = gamepad1.left_stick_x; 
        double y = -gamepad1.left_stick_y;
        

        // facing
        if (gamepad1.dpad_up) {
            state = "forward";
        } else if (gamepad1.dpad_right) {
            state = "right";
        } else if (gamepad1.dpad_down) {
            state = "backwards";
        } else if (gamepad1.dpad_left) {
            state = "left";
        }
        
        if (state == "forward") {
            x = gamepad1.left_stick_x;
            y = -gamepad1.left_stick_y;
        } else if (state == "right") {
            x = -gamepad1.left_stick_y;
            y = -gamepad1.left_stick_x;
        } else if (state == "backwards") {
            x = -gamepad1.left_stick_x;
            y = gamepad1.left_stick_y;
        } else if (state == "left") {
            x = gamepad1.left_stick_y;
            y = gamepad1.left_stick_x;
        }
        
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
        double robotAngle = Math.atan2(-y, -x) - Math.PI / 4;
        double actualRobotAngle = Math.atan2(y, -x) + Math.PI;
        double servoPosition = actualRobotAngle/Math.PI * 0.8;
        if (servoPosition > 0.8){
            servoPosition = servoPosition - 0.8;
        }
        
        
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
        double powerLeftFront = motors.get("leftFront").getPower();
        double displayPowerLeftFront = Math.round(powerLeftFront*100);
        double powerRightFront = motors.get("rightFront").getPower();
        double displayPowerRightFront = Math.round(powerRightFront*100);
        double powerLeftBack= motors.get("leftBack").getPower();
        double displayPowerLeftBack = Math.round(powerLeftBack*100);
        double powerRightBack = motors.get("rightBack").getPower();
        double displayPowerRightBack = Math.round(powerRightBack*100);
        double[] powers = {Math.abs(powerLeftFront),Math.abs(powerRightFront),Math.abs(powerLeftBack),Math.abs(powerRightBack)};
        double power = Arrays.stream(powers).max().getAsDouble();
        double displayPower = Math.round(power*100);
        
        
        // make and display text
        String text = 
        "\n"+"        ______      "
        +"\n "+displayPowerLeftFront/10+" |          | "+displayPowerRightFront/10
        +"\n"+"        |  "+displayPower/10+"  |      "
        +"\n "+displayPowerLeftBack/10+" |          | "+displayPowerRightBack/10
        +"\n"+"        ¯¯¯¯¯¯      ";
        
        // display text
        telemetry.setValue(text);
    }
    
    public void setBrake(boolean check){
        if (check){
            motors.get("leftFront").setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors.get("rightFront").setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors.get("leftBack").setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors.get("rightBack").setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            motors.get("leftFront").setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motors.get("rightFront").setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motors.get("leftBack").setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motors.get("rightBack").setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }
}











