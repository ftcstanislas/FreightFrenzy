package org.firstinspires.ftc.teamcode.RobotParts;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Location.Start;

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
    
    public void checkController(Gamepad gamepad1, Gamepad gamepad2, Start.TeamColor teamColor){
        if (gamepad2.left_bumper){
            currentMode = SpinMode.STOP;
        } else if (gamepad2.right_bumper){
            if (teamColor == Start.TeamColor.BLUE) {
                currentMode = SpinMode.SPIN_BLUE;
            } else if (teamColor == Start.TeamColor.RED) {
                currentMode = SpinMode.SPIN_RED;
            }
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
            if (startSpinnerTime + 800 < System.currentTimeMillis()){
                speed = 2500;
            }
            if (currentMode == SpinMode.SPIN_RED){
                motors.get("spinner").setVelocity(speed);
            } else if (currentMode == SpinMode.SPIN_BLUE) {
                motors.get("spinner").setVelocity(-speed);
            }

            if (startSpinnerTime + 1500 < System.currentTimeMillis()){
                currentMode = SpinMode.STOP;
            }
        }
        telemetry.setValue(motors.get("spinner").getVelocity());
    }

    public boolean setMode(String mode){
        if (mode == "spinRed"){
            currentMode = SpinMode.SPIN_RED;
            startSpinnerTime = System.currentTimeMillis();
        } else if (mode == "spinBlue"){
            currentMode = SpinMode.SPIN_BLUE;
            startSpinnerTime = System.currentTimeMillis();
        } else if (mode == "stop"){
            currentMode = SpinMode.STOP;
        }
        return true;
    }

    public boolean isNotSpinning(){
        return currentMode == SpinMode.STOP;
    }
    
    public void updateTelemetry(){
        debug();
    }
}

enum SpinMode {
    STOP,
    SPIN_RED,
    SPIN_BLUE
}









