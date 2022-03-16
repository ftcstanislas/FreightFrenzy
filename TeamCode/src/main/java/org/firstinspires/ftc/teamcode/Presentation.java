package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

@TeleOp(name = "Presentation", group = "calibrating")
public class Presentation extends OpMode {
    State state = State.START;
    DcMotor arm;
    DcMotor armSpinner;

    Servo pointer1;
    Servo pointer2;

    DcMotor intake;

    boolean xPressed = false;

    @Override
    public void init() {
        arm = hardwareMap.get(DcMotor.class, "arm");
        arm.setDirection(DcMotor.Direction.FORWARD);
        arm.setTargetPosition(0);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(1);

        armSpinner = hardwareMap.get(DcMotor.class, "armSpinner");
        armSpinner.setDirection(DcMotor.Direction.REVERSE);
        armSpinner.setTargetPosition(0);
        armSpinner.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armSpinner.setPower(1);

        pointer1 = hardwareMap.get(Servo.class, "cameraPointer1");
        pointer2 = hardwareMap.get(Servo.class, "cameraPointer2");

        pointer1.setPosition(0);
        pointer2.setPosition(1);

        intake = hardwareMap.get(DcMotor.class, "intake");
    }

    @Override
    public void loop() {
        // Telemetry
        telemetry.addLine("Press x to continue");
        telemetry.addLine(state.toString());
        telemetry.addLine("Arm position:" + arm.getCurrentPosition());
        telemetry.addLine("Arm Spinner position:" + armSpinner.getCurrentPosition());


        // Starting
        if (state == State.START){
            if (gamepad1.x && !xPressed) {
                arm.setTargetPosition(900);
                xPressed = true;
                state = State.ARM_1;
            } else if (!gamepad1.x){
                xPressed = false;
            }
        }

        if (state == State.ARM_1) {
            if (gamepad1.x && !xPressed) {
                armSpinner.setTargetPosition(-400);
                xPressed = true;
                state = State.ARM_2;
            } else if (!gamepad1.x){
                xPressed = false;
            }
        }

        if (state == State.ARM_2) {
            if (gamepad1.x && !xPressed) {

                armSpinner.setTargetPosition(400);
                xPressed = true;
                state = State.ARM_3;
            } else if (!gamepad1.x){
                xPressed = false;
            }
        }

        if (state == State.ARM_3) {
            if (gamepad1.x && !xPressed) {
                armSpinner.setTargetPosition(-400);
                xPressed = true;
                state = State.CAMERAS;
            } else if (!gamepad1.x){
                xPressed = false;
            }
        }

        if (state == State.CAMERAS) {
            if (gamepad1.x && !xPressed) {
                pointer1.setPosition(0.8);
                pointer2.setPosition(0.3);
                xPressed = true;
                state = State.ARM_ROTATE;
            } else if (!gamepad1.x){
                xPressed = false;
            }
        }

//        if (state == State.ARM_PREPARE) {
//            if (gamepad1.x && !xPressed) {
//
//                arm.setTargetPosition(700);
//                armSpinner.setTargetPosition(0);
//                xPressed = true;
//                state = State.ARM_ROTATE;
//            } else if (!gamepad1.x){
//                xPressed = false;
//            }
//        }

        if (state == State.ARM_ROTATE) {
            if (gamepad1.x && !xPressed) {
                xPressed = true;
                armSpinner.setTargetPosition(-400);
                state = State.ARM_HEIGHT;
            } else if (!gamepad1.x){
                xPressed = false;
            }
        }

        if (state == State.ARM_HEIGHT) {
            if (gamepad1.x && !xPressed) {
                xPressed = true;

                arm.setTargetPosition(0);

                state = State.INTAKING;
            } else if (!gamepad1.x){
                xPressed = false;
            }
        }

        if (state == State.INTAKING) {
            if (gamepad1.x && !xPressed) {
                xPressed = true;

                intake.setPower(-1);

                state = State.END;
            } else if (!gamepad1.x){
                xPressed = false;
            }
        }

        if (state == State.END) {
            if (gamepad1.x && !xPressed) {
                xPressed = true;

                intake.setPower(0);

            } else if (!gamepad1.x){
                xPressed = false;
            }
        }



        if (state == State.END){
            telemetry.addLine("You are done!");
        }
    }

    private void saveValue(String filename, double value){
        File file = AppUtil.getInstance().getSettingsFile(filename);
        ReadWriteFile.writeFile(file, String.valueOf(value));
    }
}


enum State{
    START,
    ARM_1,
    ARM_2,
    ARM_3,
    ARM_PREPARE,
    ARM_ROTATE,
    ARM_HEIGHT,
    INTAKING,
    CAMERAS,
    END
}