package org.firstinspires.ftc.teamcode.Sensors;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class CustomElementDetection {

    // Telemetry
    Telemetry.Item telemetry;

    public CustomElementPipeline pipeline;
    OpenCvCamera camera;

    public void init(HardwareMap hardwareMap, Telemetry.Item telemetryInit, String webcamName, boolean useCameraStream) {
        telemetry = telemetryInit;
        pipeline = new CustomElementPipeline(webcamName);
        WebcamName webcam = hardwareMap.get(WebcamName.class, webcamName);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        if (useCameraStream) {
            camera = OpenCvCameraFactory.getInstance().createWebcam(webcam, cameraMonitorViewId);
        } else {
            camera = OpenCvCameraFactory.getInstance().createWebcam(webcam);
        }
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

    public CustomElementPipeline.Location getLocation() {
        CustomElementPipeline.Location location = pipeline.getLocation();
        telemetry.setValue("Element position: " + location);
        return location;
    }

    public void stopStream() {
        camera.stopStreaming();
    }
}
