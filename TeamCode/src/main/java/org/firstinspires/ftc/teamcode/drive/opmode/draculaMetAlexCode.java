package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotParts.Drivetrain;
import org.firstinspires.ftc.teamcode.robotParts.AlexSensorUtil;

@TeleOp
public class draculaMetAlexCode extends LinearOpMode {
    Drivetrain drivetrain = new Drivetrain();
    AlexSensorUtil alexsensorutil = new AlexSensorUtil();

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain.init(hardwareMap);
        alexsensorutil.init(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // y direction is reversed
            double x = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;
            double speedAdder = gamepad1.right_trigger * 0.4;

            Drivetrain.maxSpeed = 0.6 + speedAdder;

            drivetrain.drive(x, y, rotate);
            alexsensorutil.workplease(telemetry);
            telemetry.update();
        }
    }
}