package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotParts.Arm;
import org.firstinspires.ftc.teamcode.robotParts.Drivetrain;
import org.firstinspires.ftc.teamcode.robotParts.Intake;

@TeleOp
public class dracula extends LinearOpMode {
    Drivetrain drivetrain = new Drivetrain();
    Arm arm = new Arm();
    Intake intake = new Intake();

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain.init(hardwareMap);
        arm.init(hardwareMap);
        intake.init(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // y direction is reversed
            double x = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;

            double armPower = -gamepad2.right_stick_y;

            boolean up = gamepad2.dpad_up;
            boolean down = gamepad2.dpad_down;

            drivetrain.drive(y, x, rotate);
            arm.update(armPower, telemetry);
            intake.update(up, down);

            telemetry.addData("arm power", armPower);
            telemetry.update();
        }
    }
}