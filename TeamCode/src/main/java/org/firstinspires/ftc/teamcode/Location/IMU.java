package org.firstinspires.ftc.teamcode.Location;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import java.util.Arrays;

public class IMU {
    private BNO055IMU imu;
    double offset = 0;

    public void init(HardwareMap map){
        //Init IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES; // set gyro angles to degrees
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC; // set gyro acceleration to m/s/s
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // get calibration file
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = map.get(BNO055IMU.class, "imu"); // retrieve gyro
        imu.initialize(parameters); // set parameters to gyro
    }

    public void setHeading(double heading){
        offset = heading - getHeading();
    }

    public double getHeading() {
        // Read the orientation of the robot
        Orientation angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        imu.getPosition();

        // Return the heading
        double curHeading = angles.thirdAngle;
        return curHeading + offset;
    }

    public String getDisplay() {
        double curHeading = getHeading();
        return "Orientation: " + curHeading;
    }

    public double[] getPosition() {
        Position position = imu.getPosition();
        position = position.toUnit(DistanceUnit.MM);
        double[] pos = {position.x, position.y};
        return pos;
    }
}
