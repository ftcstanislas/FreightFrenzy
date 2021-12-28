package org.firstinspires.ftc.teamcode.RobotParts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Location.Location;

import java.util.HashMap;

public class Arm extends RobotPart{
    private final double ENCODER_TICK_PER_ROUND = 560;
    private double lookingAt = 0;
    Location location = null;

    public void init(HardwareMap map, Telemetry.Item telemetryInit, Location locationInit){
        // get motors
        motors.put("arm", map.get(DcMotorEx.class, "spinner"));
        motors.get("arm").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors.get("arm").setPower(0.5);
        motors.get("arm").setTargetPosition(0);
        motors.get("arm").setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setBrake(true);

        // setup variables
        telemetry = telemetryInit;
        location = locationInit;
    }

    @Override
    public void updateTelemetry() {
//        debug();
    }

    @Override
    public void checkController(Gamepad gamepad1, Gamepad gamepad2) {
        if (gamepad2.right_stick_y != 0 || gamepad2.right_stick_x != 0) {
            lookingAt = Math.atan2(-gamepad2.right_stick_y, gamepad2.right_stick_x);
        }
        double heading = lookingAt - location.getOrientation()/180*Math.PI;
        while (heading < 0){
            heading += 2*Math.PI;
        }
        while (heading >= 2*Math.PI){
            heading -= 2*Math.PI;
        }
        double armPosition = motors.get("arm").getCurrentPosition();
        double armHeading = armPosition % ENCODER_TICK_PER_ROUND / ENCODER_TICK_PER_ROUND * (2*Math.PI);

        double difference = (heading - armHeading + Math.PI) % (2*Math.PI) - Math.PI;
        telemetry.setValue(armHeading+" "+heading+ " "+difference);

        double newArmPosition = armPosition + difference/(2*Math.PI)*ENCODER_TICK_PER_ROUND;
        motors.get("arm").setTargetPosition((int) Math.round(newArmPosition));
        motors.get("arm").setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }
}
