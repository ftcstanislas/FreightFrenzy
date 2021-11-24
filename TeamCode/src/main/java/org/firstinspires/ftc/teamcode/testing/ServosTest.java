package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TeleOp(name="Servos test", group="testing")
public class ServosTest extends OpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    Map<String, Servo> servos = new HashMap<String, Servo>();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        List<Servo> allServos = null;
        allServos = hardwareMap.getAll(Servo.class);
        for (Servo servo : allServos) {
            String name = hardwareMap.getNamesOf(servo).iterator().next();
            servos.put(name, servo);
//            telemetry.addData("motor",name);
        }
//        motor = hardwareMap.get(DcMotor.class, "arm");

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
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
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     *
     */
    @Override
    public void loop() {

        double position = gamepad1.right_stick_y/2+0.5;
        for (Map.Entry<String, Servo> entry : servos.entrySet()) {
            Servo motor = entry.getValue();
            String name = entry.getKey();
            motor.setPosition(position);
            telemetry.addData(name, position);
        }

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}