package org.firstinspires.ftc.teamcode.Final;

import java.util.Arrays;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class Drivetrain {
    
    
    PushBot motors = null;
    Telemetry.Item telemetry = null;
    
    ElapsedTime runtime = null;
    double oldTime = 0;

    public void init(PushBot motorsInit, Telemetry.Item telemetryInit, ElapsedTime timeInit){
        telemetry = telemetryInit;
        motors = motorsInit;
        runtime = timeInit;
    }
    
    public void setPowerDirection(double x, double y, double turn, double power){
        double straal = Math.hypot(x, y);
        double robotAngle = Math.atan2(-y, -x) - Math.PI / 4;
        
        //calculate power wheels
        double powerLeftFront = straal * Math.cos(robotAngle) + turn;
        double powerRightFront = straal * Math.sin(robotAngle) - turn;
        double powerLeftBack = straal * Math.sin(robotAngle) + turn;
        double powerRightBack = straal * Math.cos(robotAngle) - turn;

        //setpower to correct power (max 1)
        double[] powers = {Math.abs(powerLeftFront),Math.abs(powerRightFront),Math.abs(powerLeftBack),Math.abs(powerRightBack)};
        double max = Arrays.stream(powers).max().getAsDouble();
        powerLeftFront = powerLeftFront/max*power;
        powerRightFront = powerRightFront/max*power;
        powerLeftBack = powerLeftBack/max*power;
        powerRightBack = powerRightBack/max*power;
        
        //setpowers to motors
        motors.leftFrontDrive.setPower(powerLeftFront);
        motors.rightFrontDrive.setPower(powerRightFront);
        motors.leftBackDrive.setPower(powerLeftBack);
        motors.rightBackDrive.setPower(powerRightBack);
    }
    
    // langzamer opstarten
    public void setPowerAccelaration(double x, double y, double turn, double power){
        double maxAccelaration = 0.35;
        
        double time = runtime.time();
        if (time - oldTime > 0.2){
            oldTime = time;
            double powerLeftFront = motors.leftFrontDrive.getPower();
            double powerRightFront = motors.rightFrontDrive.getPower();
            double powerLeftBack= motors.leftBackDrive.getPower();
            double powerRightBack = motors.rightBackDrive.getPower();
            double[] powers = {Math.abs(powerLeftFront),Math.abs(powerRightFront),Math.abs(powerLeftBack),Math.abs(powerRightBack)};
            double currentPower = Arrays.stream(powers).max().getAsDouble();
            
            
            double powerDifference = power - currentPower;
            powerDifference = Math.min(maxAccelaration, powerDifference);
            powerDifference = Math.max(-maxAccelaration, powerDifference);
            
    
            setPowerDirection(x, y, turn, currentPower + powerDifference);
        };

        
    }
    
    
    public String getDisplay(){
        
        // get values
        double powerLeftFront = motors.leftFrontDrive.getPower();
        double powerRightFront = motors.rightFrontDrive.getPower();
        double powerLeftBack= motors.leftBackDrive.getPower();
        double powerRightBack = motors.rightBackDrive.getPower();
        double[] powers = {Math.abs(powerLeftFront),Math.abs(powerRightFront),Math.abs(powerLeftBack),Math.abs(powerRightBack)};
        double power = Arrays.stream(powers).max().getAsDouble();
        double displayPowerLeftFront = Math.round(powerLeftFront*100);
        double displayPowerRightFront = Math.round(powerRightFront*100);
        double displayPowerLeftBack = Math.round(powerLeftBack*100);
        double displayPowerRightBack = Math.round(powerRightBack*100);
        double displayPower = Math.round(power*100);
        double armPos = motors.arm.getTargetPosition();
        double armPower = motors.arm.getPower();
        double armCurrentPos = motors.arm.getCurrentPosition();
        double displayArmPower = Math.round(armPower*100);
        double gripperPos = motors.gripper.getPosition();
        double lockPos = motors.intakeLock.getPosition();
        double shooterPower = motors.shooter.getPower();
        double displayShooterPower = Math.round(shooterPower*100);
        
        // make and display text
        String text = 
        "\n"+"        ______      "
        +"\n "+displayPowerLeftFront/10+" |          | "+displayPowerRightFront/10
        +"\n"+"        |  "+displayPower/10+"  |      "
        +"\n "+displayPowerLeftBack/10+" |          | "+displayPowerRightBack/10
        +"\n"+"        ¯¯¯¯¯¯      ";
        // +"\n"+"Shooter power: "+displayShooterPower
        // +"\n"+"Arm position current: "+armCurrentPos
        // +"\n"+"Arm position target: "+armPos
        // // +"\n"+"Arm power: "+armPower
        // +"\n*/"+"Gripper pos: "+gripperPos;
        
        return text;
        
    }
    
    public void setBrake(boolean check){
        if (check){
            motors.leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors.rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors.leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors.rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            motors.leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motors.rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motors.leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motors.rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }
}











