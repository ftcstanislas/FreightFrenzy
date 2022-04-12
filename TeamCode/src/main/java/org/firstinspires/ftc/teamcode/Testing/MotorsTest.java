package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TeleOp(name="Motors test", group="testing")
public class MotorsTest extends OpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    Map<String, DcMotor> motors = new HashMap<String, DcMotor>();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        List<DcMotor> allMotors = null;
        allMotors = hardwareMap.getAll(DcMotor.class);
        for (DcMotor motor : allMotors) {
            String name = hardwareMap.getNamesOf(motor).iterator().next();
            motors.put(name, motor);
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
     */
    @Override
    public void loop() {

        double power = gamepad1.right_stick_y;
        for (Map.Entry<String, DcMotor> entry : motors.entrySet()) {
            DcMotor motor = entry.getValue();
            String name = entry.getKey();
            motor.setPower(power);
            telemetry.addData(name, power);
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