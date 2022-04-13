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
import static java.util.Map.Entry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class Capper extends RobotPart{

    public void init(HardwareMap map, Telemetry.Item telemetryInit){
        servos.put("capping", map.get(Servo.class, "capping"));

        // set servo modes
        currentServoMode = "retract";
        servoModes.put("retract", new HashMap<String, Object[]>() {{
            put("capping", new Object[]{"position", 0.0});
        }});

        servoModes.put("cap", new HashMap<String, Object[]>() {{
            put("capping", new Object[]{"position", 1.0});
        }});

        servos.get("capping").setPosition(0);


        // setup
        telemetry = telemetryInit;

    }

    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
        switchServoMode(gamepad2.b, "retract", "cap");
    }

    public void updateTelemetry() {
        debug();
    }
}











