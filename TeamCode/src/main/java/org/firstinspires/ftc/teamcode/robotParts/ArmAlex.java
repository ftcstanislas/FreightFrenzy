package org.firstinspires.ftc.teamcode.robotParts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArmAlex extends RobotPart {

    DcMotorEx arm;
    int upperLimit = 3500;

//    double rightMultiplier = 0.63;
    double rightMultiplier = 0.63;

    ArmHeightAlex armHeight;

    public enum ArmHeightAlex {
        INTAKE2(0),
        LOW(1400),
        MID(2500),
        HIGH(3450),
        STACK1(0),
        STACK2(0),
        STACK3(0),
        STACK4(0),
        STACK5(550);

        private int position;
        public int getPosition() {
            return this.position;
        }
        ArmHeightAlex(int position) {
            this.position = position;
        }
    }

    private int armTarget = 0;

    //    public void init(HardwareMap map, Telemetry.Item telemetry) {
    public void init(HardwareMap map) {
        arm = map.get(DcMotorEx.class, "arm1");

        // reverse one motor
        arm.setDirection(DcMotorSimple.Direction.REVERSE);

        // make sure arm stands still with power = 0
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // motors
        motors.put("armLeft", arm);
        resetEncoders();
    }

    public double goToHeight(int position, Telemetry telemetry) {
        double margin = 100;
        double currentPos = arm.getCurrentPosition();
        double distance = Math.abs(currentPos - position);
        if (currentPos < position) {
            if (distance > margin) {
                arm.setPower(1);
            } else {
                arm.setPower(1 * (distance/margin) * 0.4);
            }
            telemetry.addLine("up");
        } else if (currentPos > position) {
            if (distance > margin) {
                arm.setPower(-1);
            } else {
                arm.setPower(-1 * (distance/margin) * 0.4);
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
        int position = arm.getCurrentPosition();

        if (position <= 0 && power <= 0) {
            setPower(0);
        }
        else if (position >= upperLimit && power >= 0) {
            setPower(0.001);
        } else {
            arm.setPower(power + 0.01);
        }
        telemetry.addData("arm", position);
        telemetry.addData("arm power", arm.getPower());
        telemetry.update();
    }

    public void upToLimit(double power, Telemetry telemetry) {
        int position = arm.getCurrentPosition();

        if (position <= 0 && power <= 0) {
            setPower(0);
        }
        else if (position >= upperLimit && power >= 0) {
            setPower(0.001);
        } else {
            arm.setPower(power + 0.01);
        }
        telemetry.addData("arm", position);
        telemetry.addData("arm power", arm.getPower());
        telemetry.update();
    }

    public void updateBtnMode(ArmHeightAlex height, Telemetry telemetry) {
        goToHeight(height.getPosition(), telemetry);
        telemetry.addData("arm", arm.getCurrentPosition());
        telemetry.addData("arm goal", height.getPosition());
        telemetry.addLine(String.valueOf(height));
        telemetry.addData("arm power", arm.getPower());
    }

    public double update(boolean btns, double power,ArmHeightAlex height, Telemetry telemetry) {
        double distance = 0;
        if (btns) {
            distance = goToHeight(height.getPosition(), telemetry);
            telemetry.addData("arm", arm.getCurrentPosition());
            telemetry.addData("arm goal", height.getPosition());
            telemetry.addLine(String.valueOf(height));
            telemetry.addData("arm power", arm.getPower());
        } else {
            int position = arm.getCurrentPosition();

            if (position <= 0 && power <= 0) {
                setPower(0);
            }
            else if (position >= upperLimit && power >= 0) {
                setPower(0.001);
            } else {
                arm.setPower(power + 0.01);
            }
            telemetry.addData("arm", position);
            telemetry.addData("arm power", arm.getPower());
            telemetry.addData("distance to goal", distance);
        }
        return distance;
    }
}