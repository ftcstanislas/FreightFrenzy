package org.firstinspires.ftc.teamcode.RobotParts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Location.Location;
import org.firstinspires.ftc.teamcode.Location.Start;

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
            put("intake", new Object[]{"power", -0.5});
        }});
    }

    @Override
    public void updateTelemetry() {
        debug();
    }

    public void checkController(Gamepad gamepad1, Gamepad gamepad2, Start.TeamColor teamColor) {
        // Update spinner
        if (gamepad2.dpad_down) {
            double target = 0;
            if (teamColor == Start.TeamColor.RED) {
                target = -90;
            } else if (teamColor == Start.TeamColor.BLUE) {
                target = 90;
            }
            boolean result = setSpinnerAngle(target);
            if (result){
                setHeight(10);
            }
        } else if (gamepad2.dpad_left && teamColor == Start.TeamColor.RED) {
            setHeight(330);
            setSpinnerAngle(157);
        } else if (gamepad2.dpad_right && teamColor == Start.TeamColor.BLUE) {
            setHeight(330);
            setSpinnerAngle(-157);
        } if (gamepad2.dpad_up) {
            setHeight(980);
            if (teamColor == Start.TeamColor.RED) {
                setSpinnerAngle(105);
            } else if (teamColor == Start.TeamColor.BLUE) {
                setSpinnerAngle(-105);
            }
        } else if (Math.hypot(gamepad2.right_stick_y, gamepad2.right_stick_x) > 0.5) {
            double angle = Math.toDegrees(Math.atan2(-gamepad2.right_stick_y, gamepad2.right_stick_x));
            setSpinnerAngle(angle);
        }
        double moveArm = gamepad2.right_trigger - gamepad2.left_trigger;
        setSpinnerAngle(getSpinnerTargetAngle() + 5 * moveArm);

        // Update intake
        switchMode(gamepad2.a, "stop","intaking");
        switchMode(gamepad2.y, "stop","outtaking");

        // Update arm height
        if (gamepad2.left_stick_y != 0) {
            int newHeight = (int) (motors.get("arm").getCurrentPosition() + 120 * -gamepad2.left_stick_y);
            if (newHeight < 0){
                newHeight = 0;
            }
            setHeight(newHeight);
        }
    }

    public boolean setHeight(int height){
        if (height < 0){
            height = 0;
        }
        motors.get("arm").setTargetPosition(height);
        return Math.abs(motors.get("arm").getTargetPosition() - motors.get("arm").getCurrentPosition()) < 5;
    }

    public boolean setSpinnerAngle(double angle) {
        spinnerAngle = angle;
        return updateSpinnerAngle();
    }

    public double getSpinnerTargetAngle(){
        return spinnerAngle;
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

        // Find fastest path to target heading
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

        // Close to correct position
        return difference < 4;
    }
}
