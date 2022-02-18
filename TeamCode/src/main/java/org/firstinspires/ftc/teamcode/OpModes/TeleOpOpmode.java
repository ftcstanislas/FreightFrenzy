package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModes.DefaultOpMode;

@TeleOp(name = "OpMode Iterative 3.12", group = "main")
public class TeleOpOpmode extends DefaultOpMode {

    @Override
    public void init() {
        super.setUseCameras(false);
        super.setUseInstructions(false);
        super.init();
    }

    @Override
    public void loop() {
        // Telemetry
        double loopTime = getLoopTime();
        status.setValue(String.format("Looping for %.1fs in %.1fms",
                runtime.seconds(), loopTime));

        drivetrain.checkController(gamepad1, gamepad2);
        intake.checkController(gamepad1, gamepad2);
        spinner.checkController(gamepad1, gamepad2);
        arm.checkController(gamepad1, gamepad2);

        // Updates
        globalUpdate();

        // Calibrate
        if (gamepad1.left_stick_button && gamepad1.right_stick_button) {
            arm.calibrate();
        }
    }
}