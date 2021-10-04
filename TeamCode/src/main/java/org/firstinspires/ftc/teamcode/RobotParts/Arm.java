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

// debug, setBrake, setPower, 


public class Arm extends RobotPart{
    
    String state = "input";
    ColorSensor colorSensor;

    public Map<String, Integer> sensorInput = new HashMap<String, Integer>();

    public void init(HardwareMap map, Telemetry.Item telemetryInit){
        // set modes
        modes.put("lvl1", 1.5 * Math.PI);
        modes.put("lvl2", 1.3 * Math.PI);
        modes.put("lvl3", 0.9 * Math.PI);
        modes.put("input", 0 * Math.PI);
        
        // get motors
        motors.put("lifter", map.get(DcMotor.class, "lifter"));
        motors.get("lifter").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.get("lifter").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors.get("lifter").setDirection(DcMotor.Direction.FORWARD);
        setBrake(true);
        
        
        // get servos
        servos.put("fork", map.get(Servo.class, "fork"));

        // get sensors
        colorSensor = map.get(ColorSensor.class, "color_sensor");

        // setup
        telemetry = telemetryInit;
    }
    
    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
        boolean tipping = false;
        checkSensorInput();
        if (gamepad1.dpad_up) {
            state = "lvl2";
        } else if (gamepad1.dpad_right) {
            state = "lvl3";
        } else if (gamepad1.dpad_down) {
            state = "input";
        } else if (gamepad1.dpad_left) {
            state = "lvl1";
        }
        
        if (gamepad1.x) {
            tipping = true;
        }
        
        // set power
        setPowerState(tipping);
    }

    public void checkSensorInput() {
        sensorInput.put("red", colorSensor.red());
        sensorInput.put("green", colorSensor.blue());
        sensorInput.put("blue", colorSensor.blue());
        sensorInput.put("argb", colorSensor.argb());
        additionalTelemetry = "\n" + "Color Sensor Input:" +
                "red: " + sensorInput.get("red") + "\n" +
                "green: " + sensorInput.get("green") + "\n" +
                "blue: " + sensorInput.get("blue") + "\n" +
                "argb: " + sensorInput.get("argb") + "\n";
    }
    
    public void setPowerState(boolean tipping){
        double totalMotorCounts = -1100;
        double totalServoCounts = 1.6;
        
        // position motor and servo
        double radians = modes.get(state);
        double positionLifter = totalMotorCounts * (radians / (2 * Math.PI));
        double radiansLifter = motors.get("lifter").getCurrentPosition()/totalMotorCounts * (2 * Math.PI);
        double positionFork;
        if (radiansLifter < 0.05 * Math.PI){
            positionFork = totalServoCounts * ((0.1 * Math.PI + radiansLifter) / (2 * Math.PI));
        } else {
            positionFork = totalServoCounts * ((-0.2 * Math.PI + radiansLifter) / (2 * Math.PI));
        }
        
        // tipping
        if (tipping){
            positionFork -= totalServoCounts * 0.3;
        }

        //setpowers to motors
        setPositions(positionLifter, positionFork);
    }
    
    private void setPositions(double lifterPosition, double positionFork){
        if (motors.get("lifter").getTargetPosition() != lifterPosition){
            motors.get("lifter").setTargetPosition((int) lifterPosition);
            motors.get("lifter").setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motors.get("lifter").setPower(0.1);
        }
        servos.get("fork").setPosition(positionFork);
        
        updateTelemetry();
    }

    public void updateTelemetry(){
        debug();
    }
}











