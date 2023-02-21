package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotParts.Arm;
import org.firstinspires.ftc.teamcode.robotParts.Drivetrain;
import org.firstinspires.ftc.teamcode.robotParts.Intake;

@TeleOp
public class DraculaJOY extends LinearOpMode {
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
            double speedAdder = gamepad1.right_trigger * 0.4;

            double armPower = -gamepad2.right_stick_y;

            boolean up = gamepad2.dpad_up;
            boolean down = gamepad2.dpad_down;
            Drivetrain.maxSpeed = 0.6 + speedAdder;

            drivetrain.drive(y, x, rotate);
            arm.updateJoyMode(armPower, telemetry);
            intake.update(up, down);
            telemetry.update();
        }
    }
}
