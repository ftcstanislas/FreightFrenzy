package org.firstinspires.ftc.teamcode.robotParts;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Map;

public abstract class RobotPart {

    protected Telemetry.Item telemetry;

    protected Map<String, DcMotorEx> motors = new HashMap<String, DcMotorEx>();
    protected Map<String, Servo> servos = new HashMap<String, Servo>();
    protected Map<String, CRServo> crServos = new HashMap<String, CRServo>();



    public void resetEncoders() {
        for (DcMotorEx motor : motors.values()) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    public void setPower(double power) {
        for (DcMotorEx motor : motors.values()) {
            motor.setPower(power);
        }
    }

    public void crPower(double power) {
        for (CRServo servo : crServos.values()) {
            servo.setPower(power);
        }
    }

}
