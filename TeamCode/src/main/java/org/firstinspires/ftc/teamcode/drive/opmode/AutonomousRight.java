package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.openCV.SignalDetection;
import org.firstinspires.ftc.teamcode.robotParts.Arm;
import org.firstinspires.ftc.teamcode.robotParts.Intake;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(group = "autonomous", name = "Autonomous")
public class AutonomousRight extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private SignalDetection pipeline = null;
    private String webcamName = "Webcam";
    private boolean useCameraStream = true;
    private OpenCvWebcam camera = null;
    private Telemetry.Item camTelemetry = null;
    private Telemetry.Item colorTelemetry = null;
    private Telemetry.Item timeTelemetry = null;
    private SignalDetection.Color color = null;
    private long msUntilDetected = 0;
    @Override
    public void runOpMode() throws InterruptedException {

        Arm arm = new Arm();
        Intake intake = new Intake();


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Trajectory Traj1 = drive.trajectoryBuilder(new Pose2d())
                .strafeTo(new Vector2d(0, -64.5))
                .build();

        Trajectory Traj2 = drive.trajectoryBuilder(Traj1.end())
                .strafeTo(new Vector2d(0, -27))
                .build();

        waitForStart();

        intake.update(true, false);
        wait(100);
        drive.followTrajectory(Traj1);
        intake.update(false, false);
        drive.followTrajectory(Traj2);
    }
}