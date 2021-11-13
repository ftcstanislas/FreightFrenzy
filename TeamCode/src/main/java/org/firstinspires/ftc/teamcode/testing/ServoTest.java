
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

@TeleOp(name="Servos test", group="testing")
public class ServoTest extends OpMode {
    /* Declare OpMode members. */
    
    private Servo servo;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        servo = hardwareMap.get(Servo.class, "cameraPointer1");
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
        double position = 0.5;
        if (gamepad1.a) {
            position = 0.5 + gamepad1.left_stick_y / 2;
        } else if (gamepad1.b){
            position = 0.04;
        } else if (gamepad1.x){
            position = 0.73;
        }
        servo.setPosition(position);
        telemetry.addLine("Servo turned at "+position);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}
