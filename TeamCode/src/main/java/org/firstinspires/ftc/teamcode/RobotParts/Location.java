package org.firstinspires.ftc.teamcode.RobotParts;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Arrays;

public class Location extends RobotPart{
    private BNO055IMU imu;
    double curHeading;

    public void init(HardwareMap map, Telemetry.Item telemetryInit){
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

        // telemetry setup
        telemetry = telemetryInit;
    }

    public double getRotation() {
        // read the orientation of the robot
        Orientation angles = new Orientation();
        angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        imu.getPosition();
        // and save the heading
        curHeading = angles.thirdAngle;
        updateTelemetry();
        return curHeading;
    }

    public void checkController(Gamepad gamepad1, Gamepad gamepad2){

    }

    public void updateTelemetry() {
        Orientation angles = new Orientation();
        angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);
        imu.getPosition();
        // and save the heading
        curHeading = angles.thirdAngle;

        telemetry.setValue(curHeading);
    }
}
