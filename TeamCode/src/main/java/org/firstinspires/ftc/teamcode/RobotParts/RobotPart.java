package org.firstinspires.ftc.teamcode.RobotParts;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Map;
import java.util.HashMap;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public abstract class RobotPart {
//    private boolean isSwitchPressed = false;
    
    protected Telemetry.Item telemetry = null;
    protected Map<String, DcMotorEx> motors = new HashMap<String, DcMotorEx>();
    protected Map<String, Servo> servos = new HashMap<String, Servo>();
    private Map<String, Boolean> switchPressed = new HashMap<String, Boolean>();
//    protected HashMap<String, double[]> modes = new HashMap<String, double[]>();
    protected HashMap<String, HashMap<String, Object[]>> modes = new HashMap<String, HashMap<String, Object[]>>();
    protected String currentMode = "";
    protected String additionalTelemetry = "";
    abstract void updateTelemetry();
    
    abstract void checkController(Gamepad gamepad1, Gamepad gamepad2);
    
    protected void debug(){
        String text = "";
        if (!currentMode.equals("")){
            text += "\nCurrent mode: "+currentMode;
        }
        for (Map.Entry<String, DcMotorEx> entry : motors.entrySet()){
            DcMotorEx motor = entry.getValue();
            DcMotor.RunMode mode = motor.getMode();
            if (mode == DcMotor.RunMode.RUN_TO_POSITION || mode == DcMotor.RunMode.RUN_USING_ENCODER){
//                text += "\n" + entry.getKey() + " | encoder " + motor.getCurrentPosition() + "/" + motor.getTargetPosition() + " with " + motor.getPower();
                text += String.format("\n%s | encoder %d to %d with %.1f",
                        entry.getKey(), motor.getCurrentPosition(), motor.getTargetPosition(),  motor.getPower());
            } else if (mode == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
//                text += "\n" + entry.getKey() + " | power " + motor.getPower();
                text += String.format("\n%s | %.1f power",
                        entry.getKey(), motor.getPower());
            } else {
//                text += "\n" + entry.getKey() + " | "+ motor.getMode() + " " + motor.getPower();
                text += String.format("\n%s | %s with %.1f power",
                        entry.getKey(), mode,  motor.getPower());
            }
        };
        for (Map.Entry<String, Servo> entry : servos.entrySet()){
            Servo servo = entry.getValue();
//            text += "\n" + entry.getKey() + " | servo " + servo.getPosition();
            text += String.format("\n%s | servo at %.1f",
                    entry.getKey(),  servo.getPosition());
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
        for (DcMotorEx motor : motors.values()){
            motor.setZeroPowerBehavior(option);
        };
    }

    public boolean setMode(String mode){
        boolean done = true;
        if (modes.containsKey(mode)){
            if (!currentMode.equals(mode)){
                for (Map.Entry<String, Object[]> entry : modes.get(mode).entrySet()) { //Klopt dit?? Ik heb error gefixt maar weet niet of dit werkt
                    Object[] values = entry.getValue();
                    String powerType = (String) values[0];
                    Double value = (double) values[1];
                    DcMotorEx motor = motors.get(entry.getKey());
                    if (powerType == "power") {
                        motor.setPower(value);
                    } else if (powerType == "velocity") {
                        motor.setVelocity(value);
                    } else if (powerType == "position") {
                        if (motor.getTargetPosition() != value) {
                            motor.setTargetPosition((int) Math.round(value));
                            motor.setPower(0.4);
                            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        }
                        done = inMargin(motor.getCurrentPosition(), motor.getTargetPosition(), 100);
                    }
                }
                currentMode = mode;
                updateTelemetry();
            }
        } else {
            telemetry.setValue("Mode "+mode+" doesn't exits");
        }
        return done;
    }

    public void switchMode(boolean trigger, String defaultMode, String alternativeMode){
        String name = defaultMode + " " + alternativeMode;
        if (!switchPressed.containsKey(name)){
            switchPressed.put(name, false);
        }
        boolean isSwitchPressed = switchPressed.get(name);
        if (trigger && !isSwitchPressed){
            isSwitchPressed = true;
            if (currentMode != defaultMode){
                setMode(defaultMode);
            } else {
                setMode(alternativeMode);
            }
        } else if (!trigger && isSwitchPressed){
            isSwitchPressed = false;
        }
        switchPressed.put(name, isSwitchPressed);
    }


    public boolean inMargin(double value, double threshold, double margin) {
        return value <= threshold + margin && value >= threshold - margin;
    }
}











