package org.firstinspires.ftc.teamcode.RobotParts;

import java.util.Arrays;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
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

public class Spinner extends RobotPart{
    
    public void init(HardwareMap map, Telemetry.Item telemetryInit){
        // get motors
        motors.put("spinner", map.get(DcMotor.class, "spinner"));
        setBrake(true);
        
        // setup
        telemetry = telemetryInit;
    }
    
    public void checkController(Gamepad gamepad1, Gamepad gamepad2){
        // speed
        if (gamepad1.b) {
            setPower(1);
        } else {
            setPower(0);
        }
    }
    
    public void updateTelemetry(){
        debug();
    }
}











