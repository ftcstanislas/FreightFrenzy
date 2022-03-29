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


public class Slider extends RobotPart{

    String state = "input";
    ColorSensor colorSensor;

    double position = 0; //temp
    double stuck = 0;
    boolean goUp = false;
    boolean isSwitchPressed = false;

//    public Map<String, Integer> sensorInput = new HashMap<String, Integer>();

    public void init(HardwareMap map, Telemetry.Item telemetryInit){
        // setup
        telemetry = telemetryInit;
        motors.put("slider", map.get(DcMotorEx.class, "slider"));
        motors.get("slider").setDirection(DcMotor.Direction.REVERSE);
        motors.get("slider").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.get("slider").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        setBrake(true);
        servos.put("fork", map.get(Servo.class, "fork"));

        // not final
        modes.put("base", new HashMap<String, Object[]>() {{
            put("slider", new Object[]{"position", 0.0});
        }});

        modes.put("low", new HashMap<String, Object[]>() {{
            put("slider", new Object[]{"position", 1770.0});
        }});

        modes.put("mid", new HashMap<String, Object[]>() {{
            put("slider", new Object[]{"position", 3025.0});
        }});

        modes.put("high", new HashMap<String, Object[]>() {{
            put("slider", new Object[]{"position", 3264.0});
        }});

        servos.get("fork").setPosition(0.8);
    }

    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
//        boolean tipping = false;
//        checkSensorInput();
        if (gamepad2.dpad_up) {
            state = "mid";
        } else if (gamepad2.dpad_right) {
            state = "high";
        } else if (gamepad2.dpad_down) {
            state = "base";
            servos.get("fork").setPosition(0.8);
        } else if (gamepad2.dpad_left) {
            state = "low";
        }

        setMode(state);

//        // set power
//        position += gamepad2.right_stick_y/1000;
//        servos.get("fork").setPosition(position);
//        telemetry.setValue(servos.get("fork").getPosition() +" to " +position);

        if (gamepad2.x && !isSwitchPressed && (state == "mid" || state=="low" || state=="high")){
            isSwitchPressed = true;
            switchServo();
        } else if (!gamepad2.x && isSwitchPressed){
            isSwitchPressed = false;
        }
        updateTelemetry();
    }

    public boolean switchServo() {
        if (servos.get("fork").getPosition() > 0.5) {
            servos.get("fork").setPosition(0.4);
        } else if (servos.get("fork").getPosition() > 0.2){
            servos.get("fork").setPosition(0);
        } else {
            servos.get("fork").setPosition(0.8);
        }
        return true;
    }

    public boolean calibrate() {
        DcMotorEx sliderMotor = motors.get("slider");
        //Set slider position to -100 (previous encoder 0 position - 100);
        if (goUp == false) {
            if (sliderMotor.getTargetPosition() != -100) {
                sliderMotor.setTargetPosition(-100);
                sliderMotor.setVelocity(80);
                sliderMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if (inMargin(sliderMotor.getCurrentPosition(), sliderMotor.getTargetPosition(), 50)) {
            goUp = true;
            }
        } else {
            //Go up slowly
            if (sliderMotor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
                sliderMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                sliderMotor.setPower(0.1);
            }
            //Check stuck
            if (sliderMotor.getVelocity() <= 10) {
                stuck += 1;
            }
            if (stuck >= 30) {
                stuck = 0;
                goUp = false;
                sliderMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                sliderMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                return true;
            }
        }
        return false;
    }

    public void updateTelemetry(){
        debug();
    }
}











