package org.firstinspires.ftc.teamcode.Location;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Location {
//    private Odometry odometry = null;
    private IMU IMU = null;
    private Camera camera = null;
    private Telemetry.Item telemetry = null;

    public void init(HardwareMap hardwareMap, Telemetry.Item telemetryInit, Telemetry.Item telemetryDucks){
        // Odometry
//        odometry = new Odometry(
//                hardwareMap.get(DcMotor.class, "leftFront"),
//                hardwareMap.get(DcMotor.class, "rightFront"),
//                hardwareMap.get(DcMotor.class, "leftBack"),
////                hardwareMap.get(Servo.class, "encoder"),
//                2048 * 9 * Math.PI);
//        odometry.setPosition(0,0,0);

        // IMU
        IMU = new IMU();
        IMU.init(hardwareMap);

        // Camera
        camera = new Camera();
        camera.init(hardwareMap, telemetryInit, telemetryDucks);
        
        telemetry = telemetryInit;
    }
    
    public void startDuckDetection() {
        camera.startDuckDetection();
    }

    public void stopDuckDetection() {
        camera.stopDuckDetection();
    }

    public void update(){
//        odometry.globalCoordinatePositionUpdate();
        camera.update();

        //Duck
//        camera.setZoom(true);
//        camera.detectDuck();
//        telemetry.setValue(odometry.getDisplay()+"\n"+IMU.getDisplay());
    }

    public void stop(){
        camera.stop();
    }

    public double getOrientation(){
        return IMU.getOrientation();
    }

}
