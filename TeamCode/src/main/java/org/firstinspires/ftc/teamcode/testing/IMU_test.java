package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

@Autonomous(name="Imu test", group="testing")
public class IMU_test extends OpMode {
    private BNO055IMU imu;
    double curHeading;

    @Override
    public void init(){
        //Init IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES; // set gyro angles to degrees
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC; // set gyro acceleration to m/s/s
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // get calibration file
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hardwareMap.get(BNO055IMU.class, "imu"); // retrieve gyro
        imu.initialize(parameters); // set parameters to gyro

    }

    @Override
    public void init_loop(){
//        imu.calibrate();

        // make sure the gyro is calibrated before continuing
//        if (!imu.isCalibrating())  {
//            telemetry.addData(">", "Robot Ready.");
//        }


    }

    public double getRotation() {
        // read the orientation of the robot
        Orientation angles = new Orientation();
        angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        imu.getPosition();
        // and save the heading
        curHeading = angles.thirdAngle;
        return curHeading;
    }

    public double[] getPosition() {
        Position position = imu.getPosition();
        position = position.toUnit(DistanceUnit.MM);
        double[] pos = {position.x, position.y};
        return pos;
    }

    @Override
    public void loop() {
        double[] position = getPosition();
        telemetry.addData("position",position[0] + " "+position[1]);

        telemetry.addData("heading", getRotation());

        telemetry.addData("temperature", imu.getTemperature().temperature);
    }
}
