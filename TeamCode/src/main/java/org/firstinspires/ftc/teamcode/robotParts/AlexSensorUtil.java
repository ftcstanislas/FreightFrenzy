package org.firstinspires.ftc.teamcode.robotParts;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class AlexSensorUtil extends RobotPart {

    private DistanceSensor sensorRange1;
    private DistanceSensor sensorRange2;

    public void init(HardwareMap map) {
        sensorRange1 = map.get(DistanceSensor.class, "sensor_range_1");
        sensorRange2 = map.get(DistanceSensor.class, "sensor_range_2");

        Rev2mDistanceSensor sensor1TimeOfFlight = (Rev2mDistanceSensor)sensorRange1;
        Rev2mDistanceSensor sensor2TimeOfFlight = (Rev2mDistanceSensor)sensorRange2;
    }


    public void workplease(Telemetry telemetry) {

        // generic DistanceSensor methods.
        telemetry.addData("range1", String.format("%.01f cm", sensorRange1.getDistance(DistanceUnit.CM)));
        telemetry.addData("range2", String.format("%.01f cm", sensorRange2.getDistance(DistanceUnit.CM)));
        telemetry.addData("range1", String.format("%.01f in", sensorRange1.getDistance(DistanceUnit.INCH)));
        telemetry.addData("range2", String.format("%.01f in", sensorRange2.getDistance(DistanceUnit.INCH)));
        // veld is in inches dus ook beter om met inches te rekenen
        double currentDistance1 = sensorRange1.getDistance(DistanceUnit.INCH);
        double currentDistance2 = sensorRange2.getDistance(DistanceUnit.INCH);
    }

}