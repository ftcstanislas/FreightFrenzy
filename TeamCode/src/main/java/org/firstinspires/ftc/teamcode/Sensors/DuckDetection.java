package org.firstinspires.ftc.teamcode.Sensors;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Location.Start;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class DuckDetection {

    // Telemetry
    Telemetry.Item telemetry;

    public DuckPipeline pipeline;
    OpenCvCamera camera;

    double rectX = 0;
    double rectY = 0;
    double angleDegrees = 45;
    double angleRadians = Math.toRadians(angleDegrees);
    double targetX = 0;
    double targetY = 0;
    double distance = 0;

    public void init(HardwareMap hardwareMap, Telemetry.Item telemetryInit, Start.StartLocation startLocation, String webcamName, boolean useCameraStream, Start.TeamColor teamColor) {
        telemetry = telemetryInit;

        // Create new pipeline
        pipeline = new DuckPipeline();

        //Get webcam and init OpenCV on webcam
        WebcamName webcam = hardwareMap.get(WebcamName.class, webcamName);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        if (useCameraStream) {
            camera = OpenCvCameraFactory.getInstance().createWebcam(webcam, cameraMonitorViewId);
        } else {
            camera = OpenCvCameraFactory.getInstance().createWebcam(webcam);
        }

        // Specify the pipeline the camera is going to use to detect elements
        camera.setPipeline(pipeline);
    }

    public void startStream() {
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode)
            {
                telemetry.setValue("Camera error: " + errorCode);
            }
        });
    }

    public Object[] getDuckLocation() {
        rectX = pipeline.getRectMidpointX();
        rectY = pipeline.getRectMidpointY();

        distance = rectY * 1.5; // placeholder, plz test actual value!
        angleDegrees = (rectX - 160) / (16.0 / 3.0) * -1; // convert [0,320] to [-30,30]
        angleRadians = Math.toRadians(angleDegrees);
        double multiplier = angleDegrees / Math.abs(angleDegrees); // -1 or 1
        targetX = Math.cos(/* angleRadians */ angleDegrees) * distance * multiplier;
        targetY = Math.sin(/* angleRadians */ angleDegrees) * distance;
        telemetry.setValue("(" + targetX + "," + targetY + ")");
        return new Object[]{true, targetX, targetY, Math.round(angleDegrees)};
    }

    public void stopStream() {
        camera.stopStreaming();
    }
}
