package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotParts.Drivetrain;

@TeleOp
public class Dracula extends LinearOpMode {
    Drivetrain drivetrain = new Drivetrain();
//    Arm arm = new Arm();

    DcMotor armRight = hardwareMap.dcMotor.get("arm1");
    DcMotor armLeft = hardwareMap.dcMotor.get("arm2");

    CRServo intake = hardwareMap.crservo.get("intake");

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain.init(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // y direction is reversed
            double x = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;

            double armPower = -gamepad2.left_stick_y;

            boolean up = gamepad2.dpad_up;
            boolean down = gamepad2.dpad_down;

            double intakePower;

            drivetrain.drive(y, x, rotate);
//            arm.update(armPower);
//            intake.update(up, down);
            armRight.setPower(armPower);
            armLeft.setPower(-armPower);
            if (up) {
                intakePower = 1;
            } else if (down) {
                intakePower = -1;
            } else {
                intakePower = 0;
            }
            intake.setPower(intakePower);
        }
    }
}
