
package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.text.SimpleDateFormat;
import java.util.Date;

@TeleOp(name="Pointer calibrate", group="calibrating")
public class PointerCalibrator extends OpMode {
    /* Declare OpMode members. */

    private Servo activeServo;
    private Servo servo1;
    private Servo servo2;

    double angle = 0;
    double multiplier = 0.001;

    // Calibrate these variables
    double TOTAL_COUNTS_PER_ROUND = 1.32;
    double OFFSET = 0.41;

    boolean xPressed = false;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        servo1 = hardwareMap.get(Servo.class, "cameraPointer1");
        servo2 = hardwareMap.get(Servo.class, "cameraPointer2");

        activeServo = servo1;
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (gamepad1.right_bumper){
            activeServo = servo1;
            OFFSET = -0.01;
        } else if (gamepad1.left_bumper){
            activeServo = servo2;
            OFFSET = 0.41;
        }

        if (gamepad1.dpad_right){
            angle = 0; // verander total counts
        } else if (gamepad1.dpad_up){
            angle = 90; // verander total counts
        } else if (gamepad1.dpad_left){
            angle = 180; // Verander offset
        }

        if (gamepad1.x) {
            multiplier *= 0.5;
        } else if (gamepad1.y){
            multiplier *= 2;
        }

        OFFSET += -gamepad1.right_stick_y * multiplier;
        TOTAL_COUNTS_PER_ROUND += -gamepad1.left_stick_y * multiplier;

        setServoAngle(angle);

        // Telemetry
        telemetry.addLine("Bumpers for selecting pointer.\nRight stick y for offset and left stick y for total counts per round\nx to make multiplier smaller and y to make bigger");
        telemetry.addLine("Servo "+activeServo.getPosition());
        telemetry.addLine("Multiplier "+multiplier);
        telemetry.addLine("Offset "+OFFSET);
        telemetry.addLine("Total counts "+TOTAL_COUNTS_PER_ROUND);
    }

    public void setServoAngle(double angle) {

        double pointerPosition = TOTAL_COUNTS_PER_ROUND / 360 * (180 - angle) + OFFSET;
        double outsideServoSize = (pointerPosition - 1)/2;
        if (pointerPosition < -outsideServoSize){
            pointerPosition += TOTAL_COUNTS_PER_ROUND;
        } else if (pointerPosition > 1+outsideServoSize){
            pointerPosition -= TOTAL_COUNTS_PER_ROUND;
        }

        activeServo.setPosition(pointerPosition);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}
