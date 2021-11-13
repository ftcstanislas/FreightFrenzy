package org.firstinspires.ftc.teamcode.RobotParts;

import java.util.ArrayList;
import java.util.Arrays;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public abstract class RobotPart {
    private boolean isSwitchPressed = false;
    
    protected Telemetry.Item telemetry = null;
    protected Map<String, DcMotor> motors = new HashMap<String, DcMotor>();
    protected Map<String, Servo> servos = new HashMap<String, Servo>();
//    protected HashMap<String, double[]> modes = new HashMap<String, double[]>();
    protected HashMap<String, HashMap<String, Double>> modes = new HashMap<String, HashMap<String, Double>>();
    protected String currentMode = "";
    protected String additionalTelemetry = "";
    abstract void updateTelemetry();
    
    abstract void checkController(Gamepad gamepad1, Gamepad gamepad2);
    
    protected void debug(){
        String text = "";
        if (!currentMode.equals("")){
            text += "\nCurrent mode: "+currentMode;
        }
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
//        text += additionalTelemetry;
        telemetry.setValue(text);
    }
    
    protected void setPower(double power){
        for (DcMotor motor : motors.values()){
            motor.setPower(power);
        };
        updateTelemetry();
    }

    
    public void setBrake(boolean check){
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

    public void setMode(String mode){
        if (modes.containsKey(mode)){
            if (!currentMode.equals(mode)){
                for (Map.Entry<String, Double> entry : mode.entrySet()) {
                    Double value = entry.getValue();
                    DcMotor motor = motors.get(entry.getKey());
                    DcMotor.RunMode mode = motor.getMode();
                    if (mode == DcMotor.RunMode.RUN_WITHOUT_ENCODER){
                        motor.setPower(value);
                    } else if (mode == DcMotor.RunMode.RUN_TO_POSITION || mode == DcMotor.RunMode.RUN_USING_ENCODER){
                        motor.setTargetPosition((int) value);
                    }
                }
                currentMode = mode;
                updateTelemetry();
            }
        } else {
            telemetry.setValue("Mode "+mode+" doesn't exits");
        }
    }

    public void switchMode(boolean trigger, String defaultMode, String alternativeMode){
        if (trigger && !isSwitchPressed){
            isSwitchPressed = true;
            if (currentMode != defaultMode){
                setMode(defaultMode);
            } else {
                setMode(alternativeMode);
            }
        } else if (!trigger){
            isSwitchPressed = false;
        }
    }

    public boolean inMargin(value, threshold, margin) {
        return value <= threshold + margin && value >= threshold - margin;
    }
}











