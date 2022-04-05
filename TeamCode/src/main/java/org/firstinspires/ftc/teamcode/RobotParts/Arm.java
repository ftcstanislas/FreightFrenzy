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
    Start.TeamColor teamColor;

    public void init(HardwareMap map, Telemetry.Item telemetryInit, Location locationInit, Start.TeamColor teamColorInit) {
        // setup variables
        telemetry = telemetryInit;
        location = locationInit;
        teamColor = teamColorInit;

        // Setup armSpinner
        motors.put("armSpinner", map.get(DcMotorEx.class, "armSpinner"));
        motors.get("armSpinner").setDirection(DcMotor.Direction.REVERSE);
        motors.get("armSpinner").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors.get("armSpinner").setPower(0.7);
        motors.get("armSpinner").setTargetPositionTolerance(5); //5
        motors.get("armSpinner").setTargetPosition(motors.get("armSpinner").getCurrentPosition());
        motors.get("armSpinner").setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Setup arm
        motors.put("arm", map.get(DcMotorEx.class, "arm"));
        motors.get("arm").setDirection(DcMotor.Direction.FORWARD);
        motors.get("arm").setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors.get("arm").setPower(1);
        setHeight(motors.get("arm").getCurrentPosition());
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
        // Shortcut spinner positions
        if (teamColor  == Start.TeamColor.RED){
            if (gamepad2.dpad_down) {
                boolean result = setSpinnerAngle(-100);
                if (result) {
                    setHeight(50);
                    setMode("intaking");
                }
            } else if (gamepad2.dpad_up){
                setSpinnerAngle(152); //141
                setMode("stop");
                setHeight(470);
            } else if (gamepad2.dpad_right){
                boolean result = setSpinnerAngle(20);
                if (result) {
                    setHeight(50);
                    setMode("intaking");
                }
            } else if (gamepad2.dpad_left){
                setSpinnerAngle(125);
                setMode("stop");
                setHeight(980);
            }
        }
        if (teamColor  == Start.TeamColor.BLUE){
            if (gamepad2.dpad_down) {
                boolean result = setSpinnerAngle(100);
                if (result) {
                    setHeight(50);
                    setMode("intaking");
                }
            } else if (gamepad2.dpad_up){
                setSpinnerAngle(-152); // 141
                setMode("stop");
                setHeight(470);
            } else if (gamepad2.dpad_left){
                boolean result = setSpinnerAngle(-20);
                if (result) {
                    setHeight(50);
                    setMode("intaking");
                }
            } else if (gamepad2.dpad_right){
                setSpinnerAngle(-125);
                setMode("stop");
                setHeight(980);
            }
        }

        // Ducks
        if (gamepad2.x){
            setHeight(1885);
            if (teamColor == Start.TeamColor.RED){
                setSpinnerAngle(180);
            } else if (teamColor == Start.TeamColor.BLUE) {
                setSpinnerAngle(0);
            }
        }

        // Move arm
        if (Math.hypot(gamepad2.right_stick_y, gamepad2.right_stick_x) > 0.5) {
            double angle;
            if (teamColor == Start.TeamColor.RED) {
                angle = Math.toDegrees(Math.atan2(-gamepad2.right_stick_y, gamepad2.right_stick_x));
            } else {
                angle = Math.toDegrees(Math.atan2(gamepad2.right_stick_y, -gamepad2.right_stick_x));
            }
            setSpinnerAngle(angle);
        }
        double moveArm = gamepad2.left_trigger - gamepad2.right_trigger;
        setSpinnerAngle(getSpinnerTargetAngle() + 3 * moveArm);

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
        return Math.abs(motors.get("arm").getTargetPosition() - motors.get("arm").getCurrentPosition()) < 10;
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

        // Calculate arm position
        int targetArmPosition = (int) Math.round(armPosition + difference / 360 * ENCODER_TICK_PER_ROUND);

        // Fail safe for turning to much
        if (targetArmPosition > ENCODER_TICK_PER_ROUND){
            targetArmPosition = (int) ENCODER_TICK_PER_ROUND;
        } else if (targetArmPosition < -ENCODER_TICK_PER_ROUND){
            targetArmPosition = (int) -ENCODER_TICK_PER_ROUND;
        }

        // Update arm position
        motors.get("armSpinner").setTargetPosition(targetArmPosition);

        telemetry.setValue("Absolute angle spinner: " + Math.round(spinnerAngle) + " Encoder ticks" + targetArmPosition
                + "\nArm height:" + motors.get("arm").getCurrentPosition());

        // Close to correct position
        return Math.abs(difference) < 4;
    }
}
