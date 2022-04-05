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
        arm.setSpinnerAngle(180);
    }

    @Override
    public void loop() {
        // Telemetry
        double loopTime = getLoopTime();
        status.setValue(String.format("Looping for %.1fs in %.1fms",
                runtime.seconds(), loopTime));

        drivetrain.checkController(gamepad1, gamepad2);
        spinner.checkController(gamepad1, gamepad2, teamColor);
        arm.checkController(gamepad1, gamepad2, teamColor);

        // Party mode
        if (gamepad1.touchpad_finger_1 && gamepad1.touchpad_finger_2 && gamepad1.right_bumper && gamepad1.left_bumper){
            if (Math.round(runtime.seconds()/5) % 2 == 1) {
                drivetrain.setPowerDirection(0, 0, 1, 1);
            } else {
                drivetrain.setPowerDirection(0, 0, -1, 1);
            }
            if (Math.round(runtime.seconds()/4) % 2 == 1) {
                arm.setSpinnerAngle(0);
            } else {
                arm.setSpinnerAngle(180);
            }
            if (Math.round(runtime.seconds()/6) % 2 == 1) {
                arm.setHeight(500);
            } else {
                arm.setSpinnerAngle(800);
            }
            if (Math.round(runtime.seconds()/8) % 2 == 1) {
                spinner.setMode("spinRed");
            } else {
                spinner.setMode("spinBlue");
            }
            if (Math.round(runtime.seconds()/3) % 2 == 1) {
                arm.setMode("intaking");
            } else {
                arm.setMode("stop");
            }
        }

        // Tell drivers the time
        if (runtime.seconds() == 83) {
            gamepad1.rumble(1,1,1000);
            gamepad2.rumble(1,1,1000);
        }

        // Updates
        globalUpdate();
    }
}