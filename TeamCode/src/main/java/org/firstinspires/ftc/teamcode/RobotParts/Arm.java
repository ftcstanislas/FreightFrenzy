package org.firstinspires.ftc.teamcode.RobotParts;

import java.util.Arrays;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
    boolean isPressed = false;
    ColorSensor colorSensor;

    double position = 0; //temp

//    public Map<String, Integer> sensorInput = new HashMap<String, Integer>();

    public void init(HardwareMap map, Telemetry.Item telemetryInit){
//        // get sensors
//        colorSensor = map.get(ColorSensor.class, "color_sensor");
//
//        // setup
        telemetry = telemetryInit;
        motors.put("arm", map.get(DcMotorEx.class, "arm"));
        motors.get("arm").setDirection(DcMotor.Direction.REVERSE);
        motors.get("arm").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.get("arm").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        setBrake(true);
        servos.put("fork", map.get(Servo.class, "fork"));

        // not final
        modes.put("base", new HashMap<String, Object[]>() {{
            put("arm", new Object[]{"position", 0.0});
        }});

        modes.put("low", new HashMap<String, Object[]>() {{
            put("arm", new Object[]{"position", 400.0});
        }});

        modes.put("mid", new HashMap<String, Object[]>() {{
            put("arm", new Object[]{"position", 900.0});
        }});

        modes.put("high", new HashMap<String, Object[]>() {{
            put("arm", new Object[]{"position", 1650.0});
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
        if (gamepad1.dpad_up) {
            state = "mid";
        } else if (gamepad1.dpad_right) {
            state = "high";
        } else if (gamepad1.dpad_down) {
            state = "base";
        } else if (gamepad1.dpad_left) {
            state = "low";
        }

        setMode(state);
//
//        if (gamepad1.x) {
//            tipping = true;
//        }
//
//        // set power
//        setPowerState(tipping);
//        position += gamepad1.right_stick_y;
//        motors.get("arm").setTargetPosition((int) position);
//        motors.get("arm").setPower(0.3);
//        motors.get("arm").setMode(DcMotor.RunMode.RUN_TO_POSITION);


//        if (gamepad1.x) {
            switchServo(gamepad1.x);
//        }
        updateTelemetry();
    }

    public void switchServo(boolean trigger) {
        if (trigger && !isPressed){
            isPressed = true;
            if (0 != servos.get("fork").getPosition()){
                servos.get("fork").setPosition(0);
            } else {
                servos.get("fork").setPosition(0.75);
            }
        } else if (!trigger){
            isPressed = false;
        }
//        if (servos.get("fork").getPosition() > 0.45) {
//            servos.get("fork").setPosition(0);
//        } else {
//            servos.get("fork").setPosition(0.75);
//        }
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
        telemetry.setValue(motors.get("arm").getCurrentPosition());
//        debug();
    }
}











