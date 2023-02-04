package org.firstinspires.ftc.teamcode.robotParts;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake extends RobotPart{

    CRServo intake;

    public void init(HardwareMap map) {
        intake = map.get(CRServo.class, "intake");

        // reverse one motor

        // motors
        crServos.put("intake", intake);
    }

    public void update(boolean up, boolean down) {
        double power;
        if (up) {
            power = 1;
        } else if (down) {
            power = -1;
        } else {
            power = 0;
        }
        crPower(power);
    }
}
