package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotParts.AlexSensorUtil;
import org.firstinspires.ftc.teamcode.robotParts.Arm;
import static org.firstinspires.ftc.teamcode.robotParts.Arm.ArmHeight.HIGH;
import static org.firstinspires.ftc.teamcode.robotParts.ArmAlex.ArmHeightAlex.INTAKE2;
import static org.firstinspires.ftc.teamcode.robotParts.ArmAlex.ArmHeightAlex.LOW;
import static org.firstinspires.ftc.teamcode.robotParts.ArmAlex.ArmHeightAlex.MID;

import org.firstinspires.ftc.teamcode.robotParts.ArmAlex;
import org.firstinspires.ftc.teamcode.robotParts.DrivetrainAlex;
import org.firstinspires.ftc.teamcode.robotParts.Intake;

@TeleOp
public class draculaMetAlexCode extends LinearOpMode {
    DrivetrainAlex drivetrain = new DrivetrainAlex();
    AlexSensorUtil distanceSensor = new AlexSensorUtil();
    ArmAlex arm = new ArmAlex();
    Intake intake = new Intake();

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain.init(hardwareMap);
        arm.init(hardwareMap);
        intake.init(hardwareMap);
        distanceSensor.init(hardwareMap);

        ArmAlex.ArmHeightAlex height = INTAKE2;

        boolean buttonMode = false;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_x; // y direction is reversed
            double x = gamepad1.left_stick_y;
            double rotate = gamepad1.right_stick_x;
            double speedAdder = gamepad1.right_trigger * 0.4;

            boolean intakeBtn = gamepad2.x;
            boolean low = gamepad2.a;
            boolean mid = gamepad2.b;
            boolean high = gamepad2.y;

            double armPower = -gamepad2.right_stick_y;

            boolean up = gamepad2.dpad_up;
            boolean down = gamepad2.dpad_down;

            DrivetrainAlex.maxSpeed = 0.6 + speedAdder;

            if (intakeBtn) {
                buttonMode = true;
                height = INTAKE2;
            } else if (low) {
                buttonMode = true;
                height = ArmAlex.ArmHeightAlex.LOW;
            } else if (mid) {
                buttonMode = true;
                height = ArmAlex.ArmHeightAlex.MID;
            } else if (high) {
                buttonMode = true;
                height = ArmAlex.ArmHeightAlex.HIGH;
            }

            if (Math.abs(armPower) > 0.1) {
                buttonMode = false;
            }

            drivetrain.drive(x, y, rotate);
            distanceSensor.workplease(telemetry);
            telemetry.update();
        }
    }
}