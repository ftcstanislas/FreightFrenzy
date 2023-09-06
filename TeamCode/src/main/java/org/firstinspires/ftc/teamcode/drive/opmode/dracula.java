package org.firstinspires.ftc.teamcode.drive.opmode;

import static org.firstinspires.ftc.teamcode.robotParts.Arm.ArmHeight.HIGH;
import static org.firstinspires.ftc.teamcode.robotParts.Arm.ArmHeight.INTAKE;
import static org.firstinspires.ftc.teamcode.robotParts.Arm.ArmHeight.LOW;
import static org.firstinspires.ftc.teamcode.robotParts.Arm.ArmHeight.MID;

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

        Arm.ArmHeight height = INTAKE;

        boolean buttonMode = false;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y; // y direction is reversed
            double x = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;
            double speedAdder = gamepad1.right_trigger * 0.4;

            boolean intakeBtn = gamepad1.x;
            boolean low = gamepad1.a;
            boolean mid = gamepad1.b;
            boolean high = gamepad2.y;

            double armPower = gamepad1.right_trigger - gamepad1.left_trigger;


            boolean up = gamepad1.right_bumper;
            boolean down = gamepad1.left_bumper;
            Drivetrain.maxSpeed = 0.6 + speedAdder;

            if (intakeBtn) {
                buttonMode = true;
                height = INTAKE;
            } else if (low) {
                buttonMode = true;
                height = LOW;
            } else if (mid) {
                buttonMode = true;
                height = MID;
            } else if (high) {
                buttonMode = true;
                height = HIGH;
            }

            if (Math.abs(armPower) > 0.1) {
                buttonMode = false;
            }

            drivetrain.drive(y, x, rotate);
            arm.update(buttonMode, armPower, height, telemetry);
            intake.update(up, down);

            telemetry.update();
        }
    }
}