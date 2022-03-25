package org.firstinspires.ftc.teamcode.RobotParts;

import java.util.ArrayList;
import java.util.Arrays;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
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

public class Spinner extends RobotPart{
    SpinMode currentMode = SpinMode.STOP;
    long startSpinnerTime;
    
    public void init(HardwareMap map, Telemetry.Item telemetryInit){
        // get motors
        motors.put("spinner", map.get(DcMotorEx.class, "spinner"));
        setBrake(true);
        
        // setup telemetry
        telemetry = telemetryInit;
    }
    
    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
        if (gamepad2.left_bumper){
            currentMode = SpinMode.SPIN_LEFT;
            startSpinnerTime = System.currentTimeMillis();
        } else if (gamepad2.right_bumper){
            currentMode = SpinMode.SPIN_RIGHT;
            startSpinnerTime = System.currentTimeMillis();
        } else if (gamepad2.touchpad){
            currentMode = SpinMode.STOP;
        }
    }

    public void update(){
        if (currentMode == SpinMode.STOP){
            motors.get("spinner").setVelocity(0);
        } else {
            double speed = 2000;
            if (startSpinnerTime + 1400 < System.currentTimeMillis()){
                speed = 2500;
            }
            if (currentMode == SpinMode.SPIN_LEFT){
                motors.get("spinner").setVelocity(speed);
            } else if (currentMode == SpinMode.SPIN_RIGHT) {
                motors.get("spinner").setVelocity(-speed);
            }

            if (startSpinnerTime + 1800 < System.currentTimeMillis()){
                currentMode = SpinMode.STOP;
            }
        }
        telemetry.setValue(motors.get("spinner").getVelocity());
    }
    
    public void updateTelemetry(){
        debug();
    }
}

enum SpinMode {
    STOP,
    SPIN_LEFT,
    SPIN_RIGHT
}









