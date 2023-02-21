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
public class DraculaBTN extends LinearOpMode {
    Drivetrain drivetrain = new Drivetrain();
    Arm arm = new Arm();
    Intake intake = new Intake();

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain.init(hardwareMap);
        arm.init(hardwareMap);
        intake.init(hardwareMap);

        Arm.ArmHeight height = INTAKE;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // y direction is reversed
            double x = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;

            boolean intakeBtn = gamepad2.x;
            boolean low = gamepad2.a;
            boolean mid = gamepad2.b;
            boolean high = gamepad2.y;

            if (intakeBtn) {
                height = INTAKE;
            } else if (low) {
                height = LOW;
            } else if (mid) {
                height = MID;
            } else if (high) {
                height = HIGH;
            }

            boolean up = gamepad2.dpad_up;
            boolean down = gamepad2.dpad_down;

            drivetrain.drive(y, x, rotate);
            arm.updateBtnMode(height, telemetry);
            intake.update(up, down);

            telemetry.update();
        }
    }
}
