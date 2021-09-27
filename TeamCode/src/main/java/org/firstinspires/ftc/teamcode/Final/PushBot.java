package org.firstinspires.ftc.teamcode.Final;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PushBot{
    
    /* Public OpMode members. */
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotorEx shooter = null;
    public DcMotor ringPlacer = null;
    public DcMotor intake = null;
    public DcMotor arm = null;
    // public Servo arm = null;
    public Servo gripper = null;
    public Servo intakeLock = null;
    public Servo wing = null;
    
    public PushBot(){
        
    }
    
    public void init(HardwareMap map){
        //left front
        leftFrontDrive = map.get(DcMotor.class, "left_front");
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        // leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // right front
        rightFrontDrive = map.get(DcMotor.class, "right_front");
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        // rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // left back
        leftBackDrive = map.get(DcMotor.class, "left_back");
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        // leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // right back
        rightBackDrive = map.get(DcMotor.class, "right_back");
        rightBackDrive.setDirection(DcMotor.Direction.REVERSE);
        // rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // shooter
        shooter = map.get(DcMotorEx.class, "shooter");
        shooter.setDirection(DcMotor.Direction.REVERSE);
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // ring placer
        ringPlacer = map.get(DcMotor.class, "ring_placer");
        ringPlacer.setDirection(DcMotor.Direction.FORWARD);
        // ringPlacer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // ringPlacer.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        // arm
        arm = map.get(DcMotor.class, "arm");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setDirection(DcMotor.Direction.FORWARD);
        
        
        // intake
        intake = map.get(DcMotor.class, "intake");
        intake.setDirection(DcMotor.Direction.FORWARD);
        
        // gripper
        gripper = map.get(Servo.class, "gripper");
        gripper.setDirection(Servo.Direction.FORWARD);
        
        // intake lock
        intakeLock = map.get(Servo.class, "intake_lock");
        intakeLock.setDirection(Servo.Direction.FORWARD);
        
        // wings
        wing = map.get(Servo.class, "wing");
        wing.setDirection(Servo.Direction.FORWARD);
        
        // DELETE THIS
        // intakeLock = map.get(Servo.class, "gripper");
        // intakeLock.setDirection(Servo.Direction.FORWARD);
        
        // gripper = map.get(Servo.class, "intake_lock");
        // gripper.setDirection(Servo.Direction.FORWARD);
        
    }
    
    public void resetEncoders(){
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
 }

