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
    ColorSensor colorSensor;

    double position = 0; //temp
    double stuck = 0;
    boolean goUp = false;

//    public Map<String, Integer> sensorInput = new HashMap<String, Integer>();

    public void init(HardwareMap map, Telemetry.Item telemetryInit){
        // setup
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
            put("arm", new Object[]{"position", 1770.0});
        }});

        modes.put("mid", new HashMap<String, Object[]>() {{
            put("arm", new Object[]{"position", 3025.0});
        }});

        modes.put("high", new HashMap<String, Object[]>() {{
            put("arm", new Object[]{"position", 3864.0});
        }});

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
        } else if (gamepad2.dpad_left) {
            state = "low";
        }

        setMode(state);


//        // set power
//        position += gamepad2.right_stick_y*2;
//        motors.get("arm").setTargetPosition((int) position);
//        motors.get("arm").setPower(0.3);
//        motors.get("arm").setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (gamepad2.x) {
            switchServo();
        }
        updateTelemetry();
    }

    public boolean switchServo() {
        if (servos.get("fork").getPosition() > 0.2) {
            servos.get("fork").setPosition(0);
        } else {
            servos.get("fork").setPosition(1);
        }
        return true;
    }

    public boolean calibrate() {
        DcMotorEx armMotor = motors.get("arm");
        //Set arm position to -100 (previous encoder 0 position - 100);
        if (goUp == false) {
            if (armMotor.getTargetPosition() != -100) {
                armMotor.setTargetPosition(-100);
                armMotor.setVelocity(80);
                armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if (inMargin(armMotor.getCurrentPosition(), armMotor.getTargetPosition(), 50)) {
            goUp = true;
            }
        } else {
            //Go up slowly
            if (armMotor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
                armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                armMotor.setPower(0.1);
            }
            //Check stuck
            if (armMotor.getVelocity() <= 10) {
                stuck += 1;
            }
            if (stuck >= 30) {
                stuck = 0;
                goUp = false;
                armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                return true;
            }
        }
        return false;
    }

    public void updateTelemetry(){
        debug();
    }
}











