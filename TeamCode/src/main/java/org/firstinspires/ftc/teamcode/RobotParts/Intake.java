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



public class Intake extends RobotPart{

    public void init(HardwareMap map, Telemetry.Item telemetryInit){
        // get motors
        motors.put("intake", map.get(DcMotorEx.class, "intake"));
        motors.get("intake").setDirection(DcMotor.Direction.FORWARD);
        setBrake(true);

        // set modes
        modes.put("stop", new HashMap<String, Object[]>() {{
            put("intake", new Object[]{"power", 0.0});
        }});

        modes.put("intaking", new HashMap<String, Object[]>() {{
            put("intake", new Object[]{"power", 1.0});
        }});

        modes.put("outtaking", new HashMap<String, Object[]>() {{
            put("intake", new Object[]{"power", -1.0});
        }});

        // setup
        telemetry = telemetryInit;

        setMode("stop");
    }

    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
        switchMode(gamepad2.a, "stop","intaking");
        switchMode(gamepad2.y, "stop","outtaking");
    }
    
    public void updateTelemetry() {
        debug();
    }
}











