package org.firstinspires.ftc.teamcode.RobotParts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Location.Location;

public class Arm extends RobotPart {
    private final double ENCODER_TICK_PER_ROUND = 2786.2;
    private double spinnerAngle = 0;
    Location location = null;

    public void init(HardwareMap map, Telemetry.Item telemetryInit, Location locationInit) {
        // Setup armSpinner
        motors.put("armSpinner", map.get(DcMotorEx.class, "armSpinner"));
        motors.get("armSpinner").setDirection(DcMotor.Direction.REVERSE);
        motors.get("armSpinner").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors.get("armSpinner").setPower(1);
//        motors.get("armSpinner").setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motors.get("armSpinner").setTargetPositionTolerance(10); //5
        setSpinnerAngle(0);

        // Setup arm
        motors.put("arm", map.get(DcMotorEx.class, "arm"));
        motors.get("arm").setDirection(DcMotor.Direction.REVERSE);
        motors.get("arm").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.get("arm").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors.get("arm").setPower(1);
        motors.get("arm").setTargetPosition(0);
        motors.get("arm").setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Break
        setBrake(true);

        // setup variables
        telemetry = telemetryInit;
        location = locationInit;
    }

    @Override
    public void updateTelemetry() {
        debug();
    }

    public void calibrate() {
        motors.get("armSpinner").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.get("armSpinner").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spinnerAngle = 0.0;
    }

    @Override
    public void checkController(Gamepad gamepad1, Gamepad gamepad2) {
        // Update spinner
        if (gamepad2.right_stick_y != 0 || gamepad2.right_stick_x != 0) {
            double angle = Math.toDegrees(Math.atan2(-gamepad2.right_stick_y, gamepad2.right_stick_x));
            setSpinnerAngle(angle);
        }

        // Update arm
        motors.get("arm").setTargetPosition((int) (motors.get("arm").getCurrentPosition() + 120 * gamepad2.left_stick_y));
    }

    public void setSpinnerAngle(double angle) {
        spinnerAngle = angle;
    }

    public void update() {
        updateSpinnerAngle();
    }

    public void updateSpinnerAngle() {
        // Get relative heading
        double targetHeading = spinnerAngle - location.getOrientation();

        // Get heading of arm
        double armPosition = motors.get("armSpinner").getCurrentPosition();
        double currentHeading = armPosition % ENCODER_TICK_PER_ROUND / ENCODER_TICK_PER_ROUND * 360;

        // Find faster path to target heading
        double difference = targetHeading - currentHeading;
        while (difference < -180) {
            difference += 360;
        }
        while (difference >= 180) {
            difference -= 360;
        }

        // Update arm
        double targetArmPosition = armPosition + difference / 360 * ENCODER_TICK_PER_ROUND;
        motors.get("armSpinner").setTargetPosition((int) Math.round(targetArmPosition));
        motors.get("armSpinner").setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Telemetry
        telemetry.setValue("Absolute angle: " + spinnerAngle
                + "\nRelative angel: " + targetHeading
                + "\n" + armPosition + "->" + targetArmPosition);
    }
}
