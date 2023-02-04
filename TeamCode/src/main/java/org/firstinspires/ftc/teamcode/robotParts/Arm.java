package org.firstinspires.ftc.teamcode.robotParts;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Arm extends RobotPart {

    DcMotorEx armLeft;
    DcMotorEx armRight;

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

        // motors
        motors.put("armLeft", armLeft);
        motors.put("armRight", armRight);
        resetEncoders();
    }

    public void update(double power) {
        setPower(power);
    }
}
