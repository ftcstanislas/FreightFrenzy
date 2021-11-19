package org.firstinspires.ftc.teamcode.Location;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Gyro {

    private ModernRoboticsI2cGyro gyro;
    private String calibrating = "no";

    public void init(HardwareMap map) {
        gyro = map.get(ModernRoboticsI2cGyro.class, "gyro");
    }

    public double[] getPosition() {
        double x = gyro.rawX();
        double y = gyro.rawY();
        double heading = gyro.getIntegratedZValue();

        double[] position = {x, y, heading};
        return position;
    }

    public boolean calibrate() {
        if (calibrating == "no"){
            gyro.calibrate();
            calibrating = "yes";
        }
        if (calibrating == "yes"){
            if (!gyro.isCalibrating()){
                calibrating = "finished";
            }
        }
        if (calibrating == "finished"){
            return true;
        } else {
            return false;
        }
    }
}
