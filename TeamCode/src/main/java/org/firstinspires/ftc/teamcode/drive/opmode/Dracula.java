package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotParts.Drivetrain;

@TeleOp
public class Dracula extends LinearOpMode {
    Drivetrain drivetrain = new Drivetrain();
    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain.init(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // y direction is reversed
            double x = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;

            drivetrain.drive(y, x, rotate);
        }
    }
}
