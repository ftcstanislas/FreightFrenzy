package org.firstinspires.ftc.teamcode.RobotParts;

import java.util.Arrays;

import com.qualcomm.robotcore.hardware.ColorSensor;
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
        motors.put("intake", map.get(DcMotor.class, "intake"));
        motors.get("intake").setDirection(DcMotor.Direction.FORWARD);
//        motors.get("intake").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setBrake(true);

        // set modes
        modes.put("stop", new HashMap<String, Double>() {{
            put("intake", 0.0);
        }});

        modes.put("intaking", new HashMap<String, Double>() {{
            put("intake", 1.0);
        }});
//        modes.put("stop", new double[] {0.0});
//        modes.put("intaking", new double[] {1.0});
        
        // setup
        telemetry = telemetryInit;

        setMode("stop");
    }

    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
        switchMode(gamepad1.b, "stop","intaking");
        // speed
//        setPowers(modes.get(currentMode)); DIT HOEFT NIET
//        if (gamepad1.a) {
//            motors.get("intake").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            setPower(1);
//        } else {
//            double rotateAmount = motors.get("intake").getCurrentPosition() / 288.0;
//            double targetPos = 288 * Math.round(rotateAmount);
//            motors.get("intake").setTargetPosition((int) targetPos);
//            motors.get("intake").setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        }
    }
    
    public void updateTelemetry() {
        debug();
    }
}











