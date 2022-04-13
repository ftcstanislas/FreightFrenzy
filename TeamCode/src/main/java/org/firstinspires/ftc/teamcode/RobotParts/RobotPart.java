package org.firstinspires.ftc.teamcode.RobotParts;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Map;
import java.util.HashMap;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Location.Start;


public abstract class RobotPart {
//    private boolean isSwitchPressed = false;
    
    protected Telemetry.Item telemetry = null;
    protected Map<String, DcMotorEx> motors = new HashMap<String, DcMotorEx>();
    protected Map<String, Servo> servos = new HashMap<String, Servo>();
    private Map<String, Boolean> switchPressed = new HashMap<String, Boolean>();
    private Map<String, Boolean> servoSwitchPressed = new HashMap<String, Boolean>();
    protected HashMap<String, HashMap<String, Object[]>> modes = new HashMap<String, HashMap<String, Object[]>>();
    protected HashMap<String, HashMap<String, Object[]>> servoModes = new HashMap<>();
    protected String currentMode = "";
    protected String currentServoMode = "";
    abstract void updateTelemetry();
    

    protected void debug(){
        String text = "";
        if (!currentMode.equals("")){
            text += "\nCurrent mode: "+currentMode;
        }
        for (Map.Entry<String, DcMotorEx> entry : motors.entrySet()){
            DcMotorEx motor = entry.getValue();
            DcMotor.RunMode mode = motor.getMode();
            if (mode == DcMotor.RunMode.RUN_TO_POSITION || mode == DcMotor.RunMode.RUN_USING_ENCODER) {
                text += String.format("\n%s | encoder %d to %d with %.1f",
                        entry.getKey(), motor.getCurrentPosition(), motor.getTargetPosition(),  motor.getPower());
            } else if (mode == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
                text += String.format("\n%s | %.1f power",
                        entry.getKey(), motor.getPower());
            } else {
                text += String.format("\n%s | %s with %.1f power",
                        entry.getKey(), mode,  motor.getPower());
            }
        };
        for (Map.Entry<String, Servo> entry : servos.entrySet()){
            Servo servo = entry.getValue();
            text += String.format("\n%s | servo at %.1f",
                    entry.getKey(),  servo.getPosition());
        };
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
                for (Map.Entry<String, Object[]> entry : modes.get(mode).entrySet()) {
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

    public boolean setServoMode(String mode){
        boolean done = true;
        if (servoModes.containsKey(mode)){
            if (!currentServoMode.equals(mode)){
                for (Map.Entry<String, Object[]> entry : servoModes.get(mode).entrySet()) {
                    Object[] values = entry.getValue();
                    String powerType = (String) values[0];
                    Double value = (double) values[1];
                    Servo servo = servos.get(entry.getKey());
                    if (powerType == "position") {
                        servo.setPosition(value);
                        done = true;
                    }
                }
                currentServoMode = mode;
                updateTelemetry();
            }
        } else {
            telemetry.setValue("Mode "+mode+" doesn't exits");
        }
        return done;
    }

    public void switchServoMode(boolean trigger, String defaultMode, String alternativeMode){
        String name = defaultMode + " " + alternativeMode;
        if (!servoSwitchPressed.containsKey(name)){
            servoSwitchPressed.put(name, false);
        }
        boolean isSwitchPressed = servoSwitchPressed.get(name);
        if (trigger && !isSwitchPressed){
            isSwitchPressed = true;
            if (currentServoMode != defaultMode){
                setServoMode(defaultMode);
            } else {
                setServoMode(alternativeMode);
            }
        } else if (!trigger && isSwitchPressed){
            isSwitchPressed = false;
        }
        servoSwitchPressed.put(name, isSwitchPressed);
    }


    public boolean inMargin(double value, double threshold, double margin) {
        return value <= threshold + margin && value >= threshold - margin;
    }
}











