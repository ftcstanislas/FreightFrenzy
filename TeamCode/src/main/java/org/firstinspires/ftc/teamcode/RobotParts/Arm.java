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
// Nathalie wasn't here

public class Arm extends RobotPart{

    String state = "input";
    ColorSensor colorSensor;

//    public Map<String, Integer> sensorInput = new HashMap<String, Integer>();

    public void init(HardwareMap map, Telemetry.Item telemetryInit){
//        // get sensors
//        colorSensor = map.get(ColorSensor.class, "color_sensor");
//
//        // setup
        telemetry = telemetryInit;
        motors.put("arm", map.get(DcMotor.class, "arm"));
        motors.get("arm").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //temporary
        servos.put("fork", map.get(Servo.class, "fork"));

        // not final
        modes.put("low", new HashMap<String, Object[]>() {{
            put("arm", new Object[]{"position", 50});
        }});

        modes.put("mid", new HashMap<String, Object[]>() {{
            put("arm", new Object[]{"position", 100});
        }});

        modes.put("high", new HashMap<String, Object[]>() {{
            put("arm", new Object[]{"position", 150});
        }});

        modes.put("base", new HashMap<String, Object[]>() {{
            put("arm", new Object[]{"position", 0});
        }});
    }

//    @Override
//    public void setMode(String mode){
//        if (modes.containsKey(mode)){
//            if (currentMode != mode){
//                setPosition(modes.get(mode));
//                updateTelemetry();
//            }
//            currentMode = mode;
//        } else {
//            telemetry.setValue("Mode "+mode+" doesn't exit");
//        }
//    }
//
//    @Override
//    public void setPosition(int pos) {
//        for (DcMotor motor : motors.values()){
//            motor.setTargetPosition(pos);
//        };
//        updateTelemetry();
//    }

    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
//        boolean tipping = false;
//        checkSensorInput();
//        if (gamepad1.dpad_up) {
//            state = "lvl2";
//        } else if (gamepad1.dpad_right) {
//            state = "lvl3";
//        } else if (gamepad1.dpad_down) {
//            state = "input";
//        } else if (gamepad1.dpad_left) {
//            state = "lvl1";
//        }
//
//        if (gamepad1.x) {
//            tipping = true;
//        }
//
//        // set power
//        setPowerState(tipping);
        double powers = gamepad1.right_stick_y;
        setPower(powers);

        if (gamepad1.a) {
            switchServo();
        }
    }

    public void switchServo() {
        if (servos.get("fork").getPosition() > 0) {
            servos.get("fork").setPosition(0);
        } else {
            servos.get("fork").setPosition(1);
        }
    }

//    public void checkSensorInput() {
//        sensorInput.put("red", colorSensor.red());
//        sensorInput.put("green", colorSensor.blue());
//        sensorInput.put("blue", colorSensor.blue());
//        sensorInput.put("argb", colorSensor.argb());
//        additionalTelemetry = "\n" + "Color Sensor Input:" +
//                "red: " + sensorInput.get("red") + "\n" +
//                "green: " + sensorInput.get("green") + "\n" +
//                "blue: " + sensorInput.get("blue") + "\n" +
//                "argb: " + sensorInput.get("argb") + "\n";
//    }

//    public void setPowerState(boolean tipping){
//        double totalMotorCounts = -1100;
//        double totalServoCounts = 1.6;
//
//        // position motor and servo
//        double radians = 0;//modes.get(state);
//        double positionLifter = totalMotorCounts * (radians / (2 * Math.PI));
//        double radiansLifter = motors.get("lifter").getCurrentPosition()/totalMotorCounts * (2 * Math.PI);
//        double positionFork;
//        if (radiansLifter < 0.05 * Math.PI){
//            positionFork = totalServoCounts * ((0.1 * Math.PI + radiansLifter) / (2 * Math.PI));
//        } else {
//            positionFork = totalServoCounts * ((-0.2 * Math.PI + radiansLifter) / (2 * Math.PI));
//        }
//
//        // tipping
//        if (tipping){
//            positionFork -= totalServoCounts * 0.3;
//        }
//
//        //setpowers to motors
//        setPositions(positionLifter, positionFork);
//    }
//
//    private void setPositions(double lifterPosition, double positionFork){
//        if (motors.get("lifter").getTargetPosition() != lifterPosition){
//            motors.get("lifter").setTargetPosition((int) lifterPosition);
//            motors.get("lifter").setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            motors.get("lifter").setPower(0.1);
//        }
//        servos.get("fork").setPosition(positionFork);
//
//        updateTelemetry();
//    }

    public void updateTelemetry(){
        debug();
    }
}











