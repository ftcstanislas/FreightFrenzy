package org.firstinspires.ftc.teamcode.Calibrate;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

@TeleOp(name = "Calibrate", group = "calibrating")
public class CalibrateOpMode extends OpMode {
    boolean done = false;
    DcMotor arm;
    DcMotor armSpinner;

    @Override
    public void init() {
        arm = hardwareMap.get(DcMotor.class, "arm");
        arm.setDirection(DcMotor.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        armSpinner = hardwareMap.get(DcMotor.class, "armSpinner");
        armSpinner.setDirection(DcMotor.Direction.REVERSE);
        armSpinner.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {

        // Telemetry
        if (!done) {
            telemetry.addLine("Put the arm inside the drivetrain. So that is as far down as possible (it is not hitting the ground).\n" +
                    "Point the drivetrain towards the side that has the shared shipping hub.");
            telemetry.addLine("Press x/y/a/b when done with these steps.");
        } else {
            telemetry.addLine("You are finished!");
        }


        if (gamepad1.x || gamepad1.a || gamepad1.y || gamepad1.b) {
            // Arm height
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            arm.setTargetPosition(890);
            arm.setPower(0.1);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Arm spinner
            armSpinner.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            armSpinner.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            armSpinner.setTargetPosition(0);
            armSpinner.setPower(0.1);
            armSpinner.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Position
            saveValue("positionX.txt", 0.0);
            saveValue("positionY.txt", 0.0);
            saveValue("positionOrientation.txt", 0.0);

            done = true;
        }
    }

    private void saveValue(String filename, double value) {
        File file = AppUtil.getInstance().getSettingsFile(filename);
        ReadWriteFile.writeFile(file, String.valueOf(value));
    }
}