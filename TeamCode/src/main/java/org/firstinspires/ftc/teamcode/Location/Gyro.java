package org.firstinspires.ftc.teamcode.Location;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Gyro {

    private ModernRoboticsI2cGyro gyro;
    public Telemetry.Item telemetry = null;

    public void init(HardwareMap map, Telemetry.Item telemetryInit) {
        gyro = map.get(ModernRoboticsI2cGyro.class, "gyro");
        telemetry = telemetryInit;
    }

    public double[] getPosition() {
        double x = gyro.rawX();
        double y = gyro.rawY();
        double heading = gyro.getIntegratedZValue();

        double[] position = {x, y, heading};
        return position;
    }

    public void calibrate() {
        telemetry.setValue("Calibrating");
        gyro.calibrate();
    }
}
