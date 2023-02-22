package org.firstinspires.ftc.teamcode.drive.opmode;

import static org.firstinspires.ftc.teamcode.robotParts.Arm.ArmHeight.HIGH;
import static org.firstinspires.ftc.teamcode.robotParts.Arm.ArmHeight.INTAKE;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robotParts.Arm;
import org.firstinspires.ftc.teamcode.robotParts.Intake;
import org.openftc.apriltag.AprilTagDetection;

@Autonomous(group = "autonomous", name = "Autonomous Test Arm")
public class AutonomousTestArm extends LinearOpMode {
    Arm arm = new Arm();
    Intake intake = new Intake();

    AprilTagDetection tagOfInterest = null;
    @Override
    public void runOpMode() throws InterruptedException {
        arm.init(hardwareMap);
        intake.init(hardwareMap);

        waitForStart();

        intake.update(true, false);
        sleep(200);
        intake.update(false, false);
        armGoTo(HIGH);
        sleep(3500);
        intake.update(false, true);
        sleep(700);
        intake.update(false, false);
        armGoTo(INTAKE);
        sleep(3500);
    }

    void armGoTo(Arm.ArmHeight height) {
        double distance = 101;
        while (distance > 100 & !isStopRequested()) {
            distance = arm.update(true, 0, height, telemetry);
            telemetry.addData("distance to goal", distance);
            telemetry.update();
            sleep(10);
        }
        arm.setPower(0.001);
    }
}