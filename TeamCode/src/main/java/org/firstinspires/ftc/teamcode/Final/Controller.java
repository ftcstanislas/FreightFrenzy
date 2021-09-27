package org.firstinspires.ftc.teamcode.Final;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


public class Controller {

    public Drivetrain drivetrain = null;
    public Shooter shooter = null;
    public RingPlacer ringPlacer = null;
    public Arm arm = null;
    public Intake intake = null;
    public IntakeLock intakeLock = null;
    public Gamepad gamepad1 = null;
    public Gamepad gamepad2 = null;
    public DriveTo driveTo = null;
    public Wings wing = null;
    
    // switches controller 1
    boolean b1Pressed = false;
    boolean a1Pressed = false;
    
    String state = "forward"; //is shooter forward
    
    // switches controller 2
    // boolean y2Pressed = false;
    boolean x2Pressed = false;
    boolean b2Pressed = false;
    boolean dpad_up2Pressed = false; 
    boolean dpad_down2Pressed = false;
    boolean dpad_left2Pressed = false;
    boolean right_stick_button2Pressed = false;
    boolean left_bumper2Pressed = false;
    boolean right_bumper2Pressed = false;
    
    // reset encoder
    double previousPos = 0;
    double countPos = 10;
    
    public void init(Gamepad gamepad1Init, Gamepad gamepad2Init, Drivetrain drivetrainInit, Shooter shooterInit, RingPlacer ringPlacerInit, Arm armInit, Intake intakeInit, IntakeLock intakeLockInit, DriveTo driveToInit, Wings wingInit) {
        
        // setup variables
        drivetrain = drivetrainInit;
        shooter = shooterInit;
        ringPlacer = ringPlacerInit;
        arm = armInit;
        intake = intakeInit;
        intakeLock = intakeLockInit;
        gamepad1 = gamepad1Init;
        gamepad2 = gamepad2Init;
        driveTo = driveToInit;
        wing = wingInit;
        
    }
    
    // public double calculatePower(double x, double y){
    //     double straal = Math.hypot(x, y);
    //     straal = Math.max(-1, straal);
    //     straal = Math.min(1, straal);
    //     return straal;
    // }
    
    public void check() {
        mode1();
    }
    
    public void mode1() {
        // controller 1
        // joystick left: driving
        // dpad: change front robot
        // y: slow driving
        // b: ultra slow driving
        // x: slow turning
        // a: drive to shooting position
        // triggers: turning
        
        // controller 2
        // x: arm
        // b: gripper
        // y: wings
        // a: drive to position
        // dpad_up: shooter
        // dpad_down: intake_lock
        // joystick right: intake 
        // joystick left: ring placer
        // triggers: move arm 
        // bumpers left: shoot power targets
        // bumer right: shoot high goal

        // update drivetrain on left stick and a
        if (gamepad1.a || gamepad2.a){
            driveTo.goToPositionNew3(shooter.shootPosition[0], shooter.shootPosition[1], 1, shooter.shootPosition[2], 1000);
        } else {
            double x = gamepad1.left_stick_x; 
            double y = -gamepad1.left_stick_y;
            
            // controller 1 
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
                x = -gamepad1.left_stick_x;
                y = gamepad1.left_stick_y;
            } else if (state == "right") {
                x = gamepad1.left_stick_y;
                y = gamepad1.left_stick_x;
            } else if (state == "backwards") {
                x = gamepad1.left_stick_x;
                y = -gamepad1.left_stick_y;
            } else if (state == "left") {
                x = -gamepad1.left_stick_y;
                y = -gamepad1.left_stick_x;
            }
            
            double turning = gamepad1.right_trigger - gamepad1.left_trigger;
            if (gamepad1.x){
                turning *= 0.45;
            }
            // double power = Math.max(Pcalculateower(x,y), Math.abs(turning));
            double power = Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(turning));
            if (gamepad1.y) {
                power *= 0.45;
            } else if (gamepad1.b){
                power *= 0.3;
            }
            drivetrain.setPowerDirection(x, y, turning, power);
        }
        

        // shoot on bumpers, disable manual control
        if (gamepad2.right_bumper){
            shooter.shoot(true, true);
        } else if (gamepad2.left_bumper){
            shooter.shoot(true, false);
        } else {
            // ringplacer on right stick
            ringPlacer.setPower(-gamepad2.right_stick_y);
            intake.setPower(-gamepad2.left_stick_y);
        }
        
        // shooter switch on dpad up
        if (gamepad2.dpad_up){
            if (dpad_up2Pressed == false){
                dpad_up2Pressed = true;
                if (shooter.getPower() > 0.1){
                    shooter.setModeHighGoal(0);
                } else {
                    shooter.setModeHighGoal(2);
                }
            }
        } else {
            dpad_up2Pressed = false;
        }
        
        // intakelock switch on dpad left
        if (gamepad2.dpad_down) {
            if (dpad_down2Pressed == false) {
                dpad_down2Pressed = true;
                intakeLock.switchIt();
            }
        } else {
            dpad_down2Pressed = false;
        }
        
        // gripper switch on b
        if (gamepad2.b){
            if (b2Pressed == false){
                b2Pressed = true;
                arm.switchGripper();
            }
        } else {
            b2Pressed = false;
        }
        
        // move arm on x and switch on grippers
        arm.moveArm(((double) (gamepad2.right_trigger - gamepad2.left_trigger)*10));
        if (gamepad2.x){
            if (x2Pressed == false) {
                x2Pressed = true;
                arm.switchArm();
            }
        } else {
            x2Pressed = false;
        }
        
        if (gamepad2.dpad_left) {
            if (dpad_left2Pressed == false) {
                dpad_left2Pressed = true;
                wing.switchWing();
            }
        } else {
            dpad_left2Pressed = false;
        }
        
        // reset arm pos on a
        if (gamepad2.start) {
            arm.setArmPos(-1000);
            countPos = 0;
        }
        if (countPos < 10){
            if (Math.abs(previousPos - arm.getArmPos()) < 1) {
                countPos++;
                if (countPos == 10) {
                    arm.resetEncoder();
                    arm.setArmMode(0);
                    arm.setStartPos(-20);
                    arm.setStartPos(0);
                }
            } else {
                previousPos = arm.getArmPos();
            }
        }
    }
}






