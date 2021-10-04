package org.firstinspires.ftc.teamcode.RobotParts;

import java.util.Arrays;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.Map;
import java.util.HashMap;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public abstract class RobotPart {
    
    protected Telemetry.Item telemetry = null;
    protected Map<String, DcMotor> motors = new HashMap<String, DcMotor>();
    protected Map<String, Servo> servos = new HashMap<String, Servo>();
    protected Map<String, Double> modes = new HashMap<String, Double>();
    protected String additionalTelemetry = "";
    
    abstract void updateTelemetry();
    
    abstract void checkController(Gamepad gamepad1, Gamepad gamepad2);
    
    protected void debug(){
        String text = "";
        for (Map.Entry<String, DcMotor> entry : motors.entrySet()){
            DcMotor motor = entry.getValue();
            DcMotor.RunMode mode = motor.getMode();
            if (mode == DcMotor.RunMode.RUN_TO_POSITION || mode == DcMotor.RunMode.RUN_USING_ENCODER){
                text += "\n" + entry.getKey() + " | encoder " + motor.getCurrentPosition() + "/" + motor.getTargetPosition() + " with " + motor.getPower();
            } else if (mode == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
                text += "\n" + entry.getKey() + " | power " + motor.getPower();
            } else {
                text += "\n" + entry.getKey() + " | "+ motor.getMode() + " " + motor.getPower();
            }
        };
        for (Map.Entry<String, Servo> entry : servos.entrySet()){
            Servo servo = entry.getValue();
            text += "\n" + entry.getKey() + " | servo " + servo.getPosition();
        };
        text += additionalTelemetry;
        telemetry.setValue(text);
    }
    
    protected void setPower(double power){
        for (DcMotor motor : motors.values()){
            motor.setPower(power);
        };
        updateTelemetry();
    }
    
    protected void setBrake(boolean check){
        DcMotor.ZeroPowerBehavior option;
        if (check){
            option = DcMotor.ZeroPowerBehavior.BRAKE;
        } else {
            option = DcMotor.ZeroPowerBehavior.FLOAT;
        };
        for (DcMotor motor : motors.values()){
            motor.setZeroPowerBehavior(option);
        };
    }
}











