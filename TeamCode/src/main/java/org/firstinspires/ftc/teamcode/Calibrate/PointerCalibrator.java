
package org.firstinspires.ftc.teamcode.Calibrate;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Pointer calibrate", group = "calibrating")
public class PointerCalibrator extends OpMode {
    /* Declare OpMode members. */

    private Servo activeServo;
    private Servo servo1;
    private Servo servo2;

    double angle = 0;
    double multiplier = 0.001;

    // Calibrate these variables
    double TOTAL_COUNTS_PER_ROUND = 1.32;
    double OFFSET1 = 0.449;
    double OFFSET2 = 1.291;
    double OFFSET;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        servo1 = hardwareMap.get(Servo.class, "cameraPointer1");
        servo2 = hardwareMap.get(Servo.class, "cameraPointer2");

        activeServo = servo1;
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        //Selecting active servo
        if (gamepad1.right_bumper) {
            activeServo = servo1;
            OFFSET = OFFSET1;
        } else if (gamepad1.left_bumper) {
            activeServo = servo2;
            OFFSET = OFFSET2;
        }

        if (gamepad1.dpad_right) {
            angle = 0; // Change total counts
        } else if (gamepad1.dpad_up) {
            angle = 90; // Change total counts
        } else if (gamepad1.dpad_left) {
            angle = 180; // Change offset
        } else if (gamepad1.dpad_down) {
            angle = 270; // Change total counts
        }

        //Manually finding correct position using controller
        OFFSET += -gamepad1.right_stick_y * multiplier;
        TOTAL_COUNTS_PER_ROUND += -gamepad1.left_stick_y * multiplier;

        //Turn servo to angle in degrees
        setServoAngle(angle);

        // Telemetry
        telemetry.addLine("Bumpers for selecting pointer.\nRight stick y: offset\n Left stick y: total counts\nLeft for offset");
        telemetry.addLine("Servo " + activeServo.getPosition());
        telemetry.addLine("Multiplier " + multiplier);
        telemetry.addLine("Offset " + OFFSET);
        telemetry.addLine("Total counts " + TOTAL_COUNTS_PER_ROUND);
    }

    //Turn servo angle in degrees using the calibrated constants
    public void setServoAngle(double angle) {
        double pointerPosition = TOTAL_COUNTS_PER_ROUND / 360 * angle + OFFSET;
        double outsideServoSize = (TOTAL_COUNTS_PER_ROUND - 1) / 2;
        while (pointerPosition < -outsideServoSize) {
            pointerPosition += TOTAL_COUNTS_PER_ROUND;
        }
        while (pointerPosition > 1 + outsideServoSize) {
            pointerPosition -= TOTAL_COUNTS_PER_ROUND;
        }

        activeServo.setPosition(pointerPosition);
    }

    @Override
    public void stop() {

    }
}
