package org.firstinspires.ftc.teamcode.robotParts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Arm extends RobotPart {

    DcMotorEx armLeft;
    DcMotorEx armRight;

    int upperLimit = 3500;

//    double rightMultiplier = 0.63;
    double rightMultiplier = 0.63;

    ArmHeight armHeight;

    public enum ArmHeight {
        INTAKE(0),
        LOW(1300),
        MID(2300),
        HIGH(3350),
        STACK1(0),
        STACK2(0),
        STACK3(0),
        STACK4(0),
        STACK5(0);

        private int position;
        public int getPosition() {
            return this.position;
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

    public double goToHeight(int position, Telemetry telemetry) {
        double margin = 100;
        double currentPos = armLeft.getCurrentPosition();
        double distance = Math.abs(currentPos - position);
        if (currentPos < position) {
            if (distance > margin) {
                armLeft.setPower(1);
                armRight.setPower(rightMultiplier);
            } else {
                armLeft.setPower(1 * (distance/margin) * 0.4);
                armRight.setPower(rightMultiplier * (distance/margin) * 0.4);
            }
            telemetry.addLine("up");
        } else if (currentPos > position) {
            if (distance > margin) {
                armLeft.setPower(-1);
                armRight.setPower(-rightMultiplier);
            } else {
                armLeft.setPower(-1 * (distance/margin) * 0.4);
                armRight.setPower(-rightMultiplier * (distance/margin) * 0.4);
            }
            telemetry.addLine("down");
        } else if (position == 0 && currentPos <= 0) {
            setPower(0);
        } else {
            setPower(0.01);
        }
        return distance;
    }

    public void updateJoyMode(double power, Telemetry telemetry) {
        int position = armLeft.getCurrentPosition();

        if (position <= 0 && power <= 0) {
            setPower(0);
        }
        else if (position >= upperLimit && power >= 0) {
            setPower(0.001);
        } else {
            armLeft.setPower(power + 0.01);
            armRight.setPower((power) * rightMultiplier + 0.01);
        }
        telemetry.addData("arm", position);
        telemetry.addData("arm power", armLeft.getPower());
        telemetry.addData("arm Right", armRight.getCurrentPosition());
        telemetry.update();
    }

    public void upToLimit(double power, Telemetry telemetry) {
        int position = armLeft.getCurrentPosition();

        if (position <= 0 && power <= 0) {
            setPower(0);
        }
        else if (position >= upperLimit && power >= 0) {
            setPower(0.001);
        } else {
            armLeft.setPower(power + 0.01);
            armRight.setPower((power) * rightMultiplier + 0.01);
        }
        telemetry.addData("arm", position);
        telemetry.addData("arm power", armLeft.getPower());
        telemetry.addData("arm Right", armRight.getCurrentPosition());
        telemetry.update();
    }

    public void updateBtnMode(ArmHeight height, Telemetry telemetry) {
        goToHeight(height.getPosition(), telemetry);
        telemetry.addData("arm", armLeft.getCurrentPosition());
        telemetry.addData("arm goal", height.getPosition());
        telemetry.addLine(String.valueOf(height));
        telemetry.addData("arm power", armLeft.getPower());
    }

    public double update(boolean btns, double power,ArmHeight height, Telemetry telemetry) {
        double distance = 0;
        if (btns) {
            distance = goToHeight(height.getPosition(), telemetry);
            telemetry.addData("arm", armLeft.getCurrentPosition());
            telemetry.addData("arm goal", height.getPosition());
            telemetry.addLine(String.valueOf(height));
            telemetry.addData("arm power", armLeft.getPower());
        } else {
            int position = armLeft.getCurrentPosition();

            if (position <= 0 && power <= 0) {
                setPower(0);
            }
            else if (position >= upperLimit && power >= 0) {
                setPower(0.001);
            } else {
                armLeft.setPower(power + 0.01);
                armRight.setPower((power) * rightMultiplier + 0.01);
            }
            telemetry.addData("arm", position);
            telemetry.addData("arm power", armLeft.getPower());
            telemetry.addData("distance to goal", distance);
        }
        return distance;
    }
}