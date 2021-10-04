package org.firstinspires.ftc.teamcode.RobotParts;

import java.util.Arrays;

import com.qualcomm.robotcore.hardware.ColorSensor;
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



public class Intake extends RobotPart{
    
    double[] modes = {0,1};

    public void init(HardwareMap map, Telemetry.Item telemetryInit){
        // get motors
        motors.put("intake", map.get(DcMotor.class, "intake"));
        motors.get("intake").setDirection(DcMotor.Direction.REVERSE);
        motors.get("intake").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setBrake(true);
        
        // setup
        telemetry = telemetryInit;
    }
    
    // public void setMode(int mode) {
    //     double speed = modes[mode];
    //     setPower(speed);
    // }
    
    // public double getPower() {
    //     return motors.get("intake").getPower();
    // }
    
    // public void switchMode() {
    //     if (getPower() != modes[0]) {
    //         setMode(0);
    //     } else {
    //         setMode(1);
    //     }
    // }
    
    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
        // speed
        if (gamepad1.a) {
            motors.get("intake").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            setPower(1);
        } else {
            //288
            double rotateAmount = motors.get("intake").getCurrentPosition() / 288.0;
            double targetPos = 288 * Math.round(rotateAmount);
            motors.get("intake").setTargetPosition((int) targetPos);
            motors.get("intake").setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }
    
    public void updateTelemetry() {
        debug();
    }
}











