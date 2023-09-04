package org.firstinspires.ftc.teamcode.robotParts;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Locale;


public class AlexSensorUtil extends RobotPart {

    private DistanceSensor sensorRange1;
    private DistanceSensor sensorRange2;
    double currentDistance1CM;
    double currentDistance2CM;
    double currentDistance1Inch;
    double currentDistance2Inch;

    public void init(HardwareMap map) {
        sensorRange1 = map.get(DistanceSensor.class, "sensor_range_1");
        sensorRange2 = map.get(DistanceSensor.class, "sensor_range_2");
    }


    public void workplease(Telemetry telemetry) {

        currentDistance1CM = sensorRange1.getDistance(DistanceUnit.CM);
        currentDistance1Inch = sensorRange1.getDistance(DistanceUnit.INCH);

        currentDistance2CM = sensorRange2.getDistance(DistanceUnit.CM);
        currentDistance2Inch = sensorRange2.getDistance(DistanceUnit.INCH);

        telemetry.addData("SensorBackCM", String.format(Locale.ENGLISH,"%.01f cm", currentDistance1CM));
        telemetry.addData("SensorLeftCM", String.format(Locale.ENGLISH,"%.01f cm", currentDistance2CM));
        telemetry.addData("SensorBackIN", String.format(Locale.ENGLISH,"%.01f in", currentDistance1Inch));
        telemetry.addData("SensorLeftIN", String.format(Locale.ENGLISH,"%.01f in", currentDistance2Inch));
        // veld is in inches dus ook beter om met inches te rekenen
    }

}