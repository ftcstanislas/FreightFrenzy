// ik heb wat code gekopieerd van het internet en de imports werken daar
// dus ik fix later wel de code zodat het deed wat het deed
// ez




// code van v1 die ooit werkte
/*package org.firstinspires.ftc.teamcode.util;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;


public class AlexDistanceSensorAgainAgain {
        private Rev2mDistanceSensor distOnboard;
        private Rev2mDistanceSensor distMXP;

        @Override
        public void robotInit() {
            distSensR = new Rev2mDistanceSensor(Port.kOnboard);
            distSensL = new Rev2mDistanceSensor(Port.kMXP);
            distSensB = new Rev2mDistanceSensor(Port.placeholder1);
            distSensF = new Rev2mDistanceSensor(Port.placeholder2);
        }

        @Override
        public void robotPeriodic() {}

        @Override
        public void autonomousInit() {
            private double DistanceR;
            private double DistanceL;
            private double DistanceB;
            private double DistanceF;
            if distSensR.getRange() == wishedvalue{
                dosomething();
            }
            //repeat
        }

        @Override
        public void autonomousPeriodic() {}

        @Override
        public void teleopInit() {
            distOnboard.setAutomaticMode(true);
        }

        @Override
        /**
         public void teleopPeriodic() {
         if(distOnboard.isRangeValid()) {
         SmartDashboard.putNumber("Range Onboard", distOnboard.getRange());
         SmartDashboard.putNumber("Timestamp Onboard", distOnboard.getTimestamp());
         }

         if(distMXP.isRangeValid()) {
         SmartDashboard.putNumber("Range MXP", distMXP.getRange());
         SmartDashboard.putNumber("Timestamp MXP", distMXP.getTimestamp());
         }
         }
         *//*        @Override

        public void checkDistance {

        }

        @Override
        public void disabledInit() {
            distOnboard.setAutomaticMode(false);
        }
}
*/

package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp(name = "Sensor: REV2mDistance", group = "Sensor")
public class AlexDistanceSensorAngryAgain extends LinearOpMode {

    private DistanceSensor sensorRange1;
    private DistanceSensor sensorRange2;

    @Override
    public void runOpMode() {
        // you can use this as a regular DistanceSensor.
        sensorRange1 = hardwareMap.get(DistanceSensor.class, "sensor_range_1");
        sensorRange2 = hardwareMap.get(DistanceSensor.class, "sensor_range_2");


        // you can also cast this to a Rev2mDistanceSensor if you want to use added
        // methods associated with the Rev2mDistanceSensor class.
        Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor)sensorRange1;

        telemetry.addData(">>", "Press start to continue");
        telemetry.update();

        waitForStart();
        while(opModeIsActive()) {
            // generic DistanceSensor methods.
            telemetry.addData("deviceName1",sensorRange1.getDeviceName() );
            telemetry.addData("deviceName2",sensorRange2.getDeviceName() );
            telemetry.addData("range1", String.format("%.01f cm", sensorRange1.getDistance(DistanceUnit.CM)));
            telemetry.addData("range2", String.format("%.01f cm", sensorRange2.getDistance(DistanceUnit.CM)));
            telemetry.addData("range1", String.format("%.01f in", sensorRange1.getDistance(DistanceUnit.INCH)));
            telemetry.addData("range2", String.format("%.01f in", sensorRange2.getDistance(DistanceUnit.INCH)));
            // veld is in inches dus ook beter om met inches te rekenen
            double currentDistance1 = sensorRange1.getDistance(DistanceUnit.INCH);
            double currentDistance2 = sensorRange2.getDistance(DistanceUnit.INCH);
            // Rev2mDistanceSensor specific methods.
            telemetry.addData("ID", String.format("%x", sensorTimeOfFlight.getModelID()));
            telemetry.addData("did time out", Boolean.toString(sensorTimeOfFlight.didTimeoutOccur()));

            telemetry.update();
        }
    }

}