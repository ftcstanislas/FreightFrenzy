package org.firstinspires.ftc.teamcode.RobotParts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Location.Location;
import org.firstinspires.ftc.teamcode.Location.Start;

import java.util.HashMap;

public class Arm extends RobotPart {
    private final double ENCODER_TICK_PER_ROUND = 2786.2;
    private double spinnerAngle = 90;
    Location location = null;
    Start.TeamColor teamColor;

    // Variables
    private final int intakeHeight = 0;
    private final int intakeAllianceAngle = 125;
    private final int intakeSharedAngle = -100;
    private final int outtakeShardedHeight = 470;
    private final int outtakeShardedAngle = 152;
    private final int outtakeAllianceHeight = 980;
    private final int outtakeAllianceAngle = 20;

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

        // Setup capping
        servos.put("capping", map.get(Servo.class, "capping"));


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

        // set servo modes
        currentServoMode = "retract";
        servoModes.put("retract", new HashMap<String, Object[]>() {{
            put("capping", new Object[]{"position", 0.0});
        }});

        servoModes.put("cap", new HashMap<String, Object[]>() {{
            put("capping", new Object[]{"position", 0.5});
        }});

//        servoModes.put("lowcap", new HashMap<String, Object[]>() {{
//            put("capping", new Object[]{"position", 0.7});
//        }});
//
//        servoModes.put("highcap", new HashMap<String, Object[]>() {{
//            put("capping", new Object[]{"position", 0.9});
//        }});

    }

    @Override
    public void updateTelemetry() {
        debug();
    }

    public void checkController(Gamepad gamepad1, Gamepad gamepad2, Start.TeamColor teamColor) {
        // Shortcut spinner positions
        if (teamColor  == Start.TeamColor.RED){
            if (gamepad2.dpad_down) {
                boolean result = setSpinnerAngle(intakeSharedAngle);
                if (result) {
                    setHeight(intakeHeight);
                    setMode("intaking");
                }
            } else if (gamepad2.dpad_up){
                setSpinnerAngle(outtakeShardedAngle);
                setMode("stop");
                setHeight(outtakeShardedHeight);
            } else if (gamepad2.dpad_right){
                boolean result = setSpinnerAngle(outtakeAllianceAngle);
                if (result) {
                    setHeight(intakeHeight);
                    setMode("intaking");
                }
            } else if (gamepad2.dpad_left){
                setSpinnerAngle(intakeAllianceAngle);
                setMode("stop");
                setHeight(outtakeAllianceHeight);
            }
        }
        if (teamColor  == Start.TeamColor.BLUE){
            if (gamepad2.dpad_down) {
                boolean result = setSpinnerAngle(-intakeSharedAngle);
                if (result) {
                    setHeight(intakeHeight);
                    setMode("intaking");
                }
            } else if (gamepad2.dpad_up){
                setSpinnerAngle(-outtakeShardedAngle); // 141
                setMode("stop");
                setHeight(outtakeShardedHeight);
            } else if (gamepad2.dpad_left){
                boolean result = setSpinnerAngle(-outtakeAllianceAngle);
                if (result) {
                    setHeight(intakeHeight);
                    setMode("intaking");
                }
            } else if (gamepad2.dpad_right){
                setSpinnerAngle(-intakeAllianceAngle);
                setMode("stop");
                setHeight(outtakeAllianceHeight);
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

        // Update servo
        switchMode(gamepad2.b, "retract", "cap");

        // Update arm height
        if (gamepad2.left_stick_y != 0) {
            int newHeight = (int) (motors.get("arm").getCurrentPosition() + 120 * -gamepad2.left_stick_y);
            setHeight(newHeight);
        }
    }

    public boolean setHeight(int height){
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
