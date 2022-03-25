package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModes.DefaultOpMode;

public class TeleOpOpmode extends DefaultOpMode {

    @Override
    public void init() {
        super.setUseCameras(false);
        super.setUseInstructions(false);
        super.init();
//        arm.setHeight(890);
    }

    @Override
    public void start() {
        arm.setHeight(890);
    }

    @Override
    public void loop() {
        // Telemetry
        double loopTime = getLoopTime();
        status.setValue(String.format("Looping for %.1fs in %.1fms",
                runtime.seconds(), loopTime));

        drivetrain.checkController(gamepad1, gamepad2);
        spinner.checkController(gamepad1, gamepad2);
        arm.checkController(gamepad1, gamepad2, teamColor);

        // Updates
        globalUpdate();
    }
}