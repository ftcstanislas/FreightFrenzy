package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class Disco extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("left_front");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("left_back");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("right_front");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("right_back");
        DcMotor armMotor1 = hardwareMap.dcMotor.get("arm1");
        DcMotor armMotor2 = hardwareMap.dcMotor.get("arm2");

        CRServo intake = hardwareMap.crservo.get("intake");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y; // Remember, this is reversed!
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            double armPower = gamepad2.left_stick_y;
            boolean dpad2Up = gamepad2.dpad_up;
            boolean dpad2Down = gamepad2.dpad_down;

            double intakeMotorPos = -gamepad2.right_stick_y;

            if (dpad2Up) {
                intake.setPower(1);
            }
            if (dpad2Down) {
                intake.setPower(-1);
            } else {
                intake.setPower(0);
            }
            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

            armMotor1.setPower(armPower);
            armMotor2.setPower(-armPower);

//            runTo(intakeMotor, intakeMotorPos);

            telemetry.addData("armPosition1", armMotor1.getCurrentPosition());
            telemetry.addData("armPosition2", armMotor2.getCurrentPosition());
            telemetry.addData("arm top supposed", intakeMotorPos);
            telemetry.update();
        }
    }

    private void runTo(DcMotor motor, double pos) {
        while (Math.abs(motor.getCurrentPosition() - pos) < 20) {
            motor.setPower(pos/Math.abs(pos));
        }
        motor.setPower(0);
    }
}