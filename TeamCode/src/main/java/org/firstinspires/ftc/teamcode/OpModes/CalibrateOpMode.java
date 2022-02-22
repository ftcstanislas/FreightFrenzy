package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.OpModes.DefaultOpMode;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Calibrate", group = "calibrating")
public class CalibrateOpMode extends OpMode {
    State state = State.STARTING;
    int check;
    int lastPosition;
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
        telemetry.addLine(state.toString() + " CHECK " + check);

        // Starting
        if (state == State.STARTING){
            telemetry.addLine("Press x to start");
            if (gamepad1.x) {
                state = State.CALIBRATE_HEIGHT_ARM_START;
            }
        }

        if (state == State.CALIBRATE_HEIGHT_ARM_START) {
            telemetry.addLine("Let the arm of the robot touch to floor.");
            telemetry.addLine("Press y to continue");
            if (gamepad1.y) {
                arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                arm.setTargetPosition(890);
                arm.setPower(0.1);
                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                state = State.CALIBRATE_SPINNER_ARM;
            }
        }

        if (state == State.CALIBRATE_SPINNER_ARM){
            telemetry.addLine("Rotate the arm so it is facing exactly to the right.");
            telemetry.addLine("Press x to continue");
            if (gamepad1.x) {
                armSpinner.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                armSpinner.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                armSpinner.setTargetPosition(0);
                armSpinner.setPower(0.1);
                armSpinner.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                state = State.END;
            }
        }

        if (state == State.END){
            telemetry.addLine("You are done!");
        }
    }
}


enum State{
    STARTING,
    CALIBRATE_HEIGHT_ARM_START,
    CALIBRATE_HEIGHT_ARM,
    CALIBRATE_SPINNER_ARM_START,
    CALIBRATE_SPINNER_ARM,
    END
}