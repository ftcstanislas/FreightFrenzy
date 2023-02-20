package org.firstinspires.ftc.teamcode.robotParts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Arm extends RobotPart {

    DcMotorEx armLeft;
    DcMotorEx armRight;

    int upperLimit = 3685;

    ArmHeight armHeight;

    private enum ArmHeight {
        INTAKE(0),
        LOW(150),
        MID(400),
        HIGH(650);
//        CUSTOM(0);

        private int position;
        public int getPosition() {
            return this.position;
        }
        public void setPosition(int position) {
            this.position = position;
        }
        ArmHeight(int position) {
            this.position = position;
        }
    }

    private int armTarget = 0;

    //    public void init(HardwareMap map, Telemetry.Item telemetry) {
    public void init(HardwareMap map) {
        armLeft = map.get(DcMotorEx.class, "arm1");
        armRight = map.get(DcMotorEx.class, "arm2");

        // reverse one motor
        armLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // make sure arm stands still with power = 0
        armLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // motors
        motors.put("armLeft", armLeft);
        motors.put("armRight", armRight);
        resetEncoders();
    }

    public void update(double power, Telemetry telemetry) {
        int position = armLeft.getCurrentPosition();

        if (position <= 0 && power <= 0) {
            setPower(0);
        }
        else if (position >= upperLimit && power >= 0) {
            setPower(0.001);
        } else {
            armLeft.setPower(power + 0.01);
            armRight.setPower((power) * 0.63 + 0.01);
        }
        telemetry.addData("arm", position);
    }
}