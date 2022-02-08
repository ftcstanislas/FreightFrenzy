package org.firstinspires.ftc.teamcode.RobotParts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Location.Location;

import java.util.HashMap;

public class Arm extends RobotPart{
    private final double ENCODER_TICK_PER_ROUND = 2786.2;
    private double lookingAt = 0;
    Location location = null;

    public void init(HardwareMap map, Telemetry.Item telemetryInit, Location locationInit){
        // get motors
        motors.put("armSpinner", map.get(DcMotorEx.class, "armSpinner"));
        motors.get("armSpinner").setDirection(DcMotor.Direction.REVERSE);
        motors.put("arm", map.get(DcMotorEx.class, "arm"));
        motors.get("arm").setDirection(DcMotor.Direction.REVERSE);

        motors.get("armSpinner").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors.get("armSpinner").setPower(1);
        motors.get("armSpinner").setTargetPosition(motors.get("armSpinner").getTargetPosition());
        motors.get("armSpinner").setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motors.get("armSpinner").setTargetPositionTolerance(10); //5

        setBrake(true);

        // setup variables
        telemetry = telemetryInit;
        location = locationInit;
    }

    @Override
    public void updateTelemetry() {
        debug();
    }

    public void calibrate(){
        motors.get("armSpinner").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.get("armSpinner").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lookingAt = 0.0;
    }

    @Override
    public void checkController(Gamepad gamepad1, Gamepad gamepad2) {
        if (gamepad2.right_stick_y != 0 || gamepad2.right_stick_x != 0) {
            lookingAt = Math.atan2(-gamepad2.right_stick_y, gamepad2.right_stick_x);
        }
        double heading = lookingAt - Math.toRadians(location.getOrientation());
        while (heading <= 0){
            heading += 2*Math.PI;
        }
        while (heading > 2*Math.PI){
            heading -= 2*Math.PI;
        }
        double armPosition = motors.get("armSpinner").getCurrentPosition();
        double armHeading = armPosition % ENCODER_TICK_PER_ROUND / ENCODER_TICK_PER_ROUND * (2*Math.PI);
        while (armHeading <= 0){
            armHeading += 2*Math.PI;
        }
        while (armHeading > 2*Math.PI){
            armHeading -= 2*Math.PI;
        }

        double difference = heading - armHeading;
        while (difference > Math.PI){
            difference -= 2*Math.PI;
        }
        while (difference < -Math.PI){
            difference += 2*Math.PI;
        }
        telemetry.setValue(Math.round(Math.toDegrees(armHeading))+" "+Math.round(Math.toDegrees(heading))+ " "+Math.round(Math.toDegrees(difference)));

        double newArmPosition = armPosition + difference/(2*Math.PI)*ENCODER_TICK_PER_ROUND;
        motors.get("armSpinner").setTargetPosition((int) Math.round(newArmPosition));
        motors.get("armSpinner").setMode(DcMotor.RunMode.RUN_TO_POSITION);

//        debug();
//        telemetry.setValue(armPosition+" "+newArmPosition);
        motors.get("arm").setPower(gamepad2.left_stick_y);
//        motors.get("armSpinner").setPower(gamepad1.right_stick_x);
    }

}
