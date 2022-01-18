
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

@TeleOp(name="Servo camera test", group="testing")
public class ServoTest extends OpMode {
    /* Declare OpMode members. */
    
    private Servo servo;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        servo = hardwareMap.get(Servo.class, "cameraPointer2");
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
        if (gamepad1.dpad_right){
            setServoAngle(0); //Naar zijkant door offset te veranderen
        } else if (gamepad1.dpad_up){
            setServoAngle(90); // Naar voorkant door total counts per round te veranderen
        } else if (gamepad1.dpad_left){
            setServoAngle(180); // Testen of die goed staat, zo niet is er iets raars
        }
        telemetry.addLine("Servo "+servo.getPosition());
    }

    public void setServoAngle(double angle) {
        final double TOTAL_COUNTS_PER_ROUND = 1.32;
        final double OFFSET = 0.41;
        double pointerPosition = TOTAL_COUNTS_PER_ROUND/360*(180-angle) + OFFSET;
        // while (pointerPosition < -0.19){
        //     pointerPosition+=2;
        // }
        // while (pointerPosition >= 1.19){
        //     pointerPosition-=2;
        // }
        servo.setPosition(pointerPosition);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}
