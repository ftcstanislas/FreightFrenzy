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

    boolean xPressed = false;

    @Override
    public void init() {
        arm = hardwareMap.get(DcMotor.class, "arm");
        arm.setDirection(DcMotor.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        armSpinner = hardwareMap.get(DcMotor.class, "armSpinner");
        armSpinner.setDirection(DcMotor.Direction.REVERSE);
        armSpinner.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        pointer1 = hardwareMap.get(Servo.class, "cameraPointer1");
        pointer2 = hardwareMap.get(Servo.class, "cameraPointer2");
    }

    @Override
    public void loop() {
        // Telemetry
        telemetry.addLine("Press x to continue");
        telemetry.addLine(state.toString());

        // Starting
        if (state == State.START){
            if (gamepad1.x && !xPressed) {
                xPressed = true;
                state = State.CAMERAS;
            } else if (!gamepad1.x){
                xPressed = false;
            }
        }

        if (state == State.CAMERAS) {
            if (gamepad1.x && !xPressed) {
                xPressed = true;
                pointer1.setPosition(0.6);
                pointer2.setPosition(0.6);
                state = State.END;
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
    POINTS_1,
    POINTS_2,
    POINTS_3,
    CAMERAS,
    END
}