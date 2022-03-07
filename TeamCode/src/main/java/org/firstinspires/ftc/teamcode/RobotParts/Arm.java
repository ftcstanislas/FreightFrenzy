package org.firstinspires.ftc.teamcode.RobotParts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Location.Location;

import java.util.HashMap;

public class Arm extends RobotPart {
    private final double ENCODER_TICK_PER_ROUND = 2786.2;
    private double spinnerAngle = 90;
    Location location = null;

    public void init(HardwareMap map, Telemetry.Item telemetryInit, Location locationInit) {
        // setup variables
        telemetry = telemetryInit;
        location = locationInit;

        // Setup armSpinner
        motors.put("armSpinner", map.get(DcMotorEx.class, "armSpinner"));
        motors.get("armSpinner").setDirection(DcMotor.Direction.REVERSE);
        motors.get("armSpinner").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors.get("armSpinner").setPower(1);
        motors.get("armSpinner").setTargetPositionTolerance(10); //5
        updateSpinnerAngle();
        motors.get("armSpinner").setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Setup arm
        motors.put("arm", map.get(DcMotorEx.class, "arm"));
        motors.get("arm").setDirection(DcMotor.Direction.FORWARD);
        motors.get("arm").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors.get("arm").setPower(1);
        setHeight(10);
        motors.get("arm").setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Setup intake
        motors.put("intake", map.get(DcMotorEx.class, "intake"));
        motors.get("intake").setDirection(DcMotor.Direction.REVERSE);

        // Break
        setBrake(true);

        // set modes
        currentMode = "stop";
        modes.put("stop", new HashMap<String, Object[]>() {{
            put("intake", new Object[]{"power", 0.0});
        }});

        modes.put("intaking", new HashMap<String, Object[]>() {{
            put("intake", new Object[]{"power", 1.0});
        }});

        modes.put("outtaking", new HashMap<String, Object[]>() {{
            put("intake", new Object[]{"power", -1.0});
        }});
    }

    @Override
    public void updateTelemetry() {
        debug();
    }

    @Override
    public void checkController(Gamepad gamepad1, Gamepad gamepad2) {
        // Update spinner
        if (Math.hypot(gamepad2.right_stick_y, gamepad2.right_stick_x) > 0.1) {
            double angle = Math.toDegrees(Math.atan2(-gamepad2.right_stick_y, gamepad2.right_stick_x));
            setSpinnerAngle(angle);
        }

        // Update intake
        switchMode(gamepad2.a, "stop","intaking");
        switchMode(gamepad2.y, "stop","outtaking");

        // Update arm height
        if (gamepad2.left_stick_y != 0) {
            setHeight((int) (motors.get("arm").getCurrentPosition() + 120 * -gamepad2.left_stick_y));
        }
    }

    public boolean setHeight(int height){
        motors.get("arm").setTargetPosition(height);
        if (Math.abs(motors.get("arm").getTargetPosition() - motors.get("arm").getCurrentPosition()) < 5){
            return true;
        } else {
            return false;
        }
    }

    public boolean setSpinnerAngle(double angle) {
        spinnerAngle = angle;
        return updateSpinnerAngle();
    }

    public void setIntake(String mode){
        if (mode == "stop"){
            motors.get("intake").setPower(0);
        } else if (mode == "intaking") {
            motors.get("intake").setPower(1);
        } else if (mode == "outtaking"){
            motors.get("intake").setPower(-1);
        } else {
            throw new java.lang.Error(mode + " is not an mode");
        }
    }

    public void update() {
        updateSpinnerAngle();

        // Telemetry
        telemetry.setValue("Absolute angle spinner: " + spinnerAngle
                + "\nArm height:" + motors.get("arm").getCurrentPosition());
    }

    public boolean updateSpinnerAngle() {
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
        if (difference < 4){
            return true;
        } else {
            return false;
        }
    }
}
