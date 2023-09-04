package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotParts.AlexSensorUtil;
import org.firstinspires.ftc.teamcode.robotParts.DrivetrainAlex;

@TeleOp
public class draculaMetAlexCode extends LinearOpMode {
    DrivetrainAlex drivetrain = new DrivetrainAlex();
    AlexSensorUtil distanceSensor = new AlexSensorUtil();

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain.init(hardwareMap);
        distanceSensor.init(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_x; // y direction is reversed
            double x = gamepad1.left_stick_y;
            double rotate = gamepad1.right_stick_x;
            double speedAdder = gamepad1.right_trigger * 0.4;

            DrivetrainAlex.maxSpeed = 0.6 + speedAdder;

            drivetrain.drive(x, y, rotate);
            distanceSensor.workplease(telemetry);
            telemetry.update();
        }
    }
}