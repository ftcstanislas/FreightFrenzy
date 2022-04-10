package org.firstinspires.ftc.teamcode.Sensors;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Location.Start;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class CustomElementDetection {

    // Telemetry
    Telemetry.Item telemetry;

    public CustomElementPipeline pipeline;
    OpenCvCamera camera;

    public void init(HardwareMap hardwareMap, Telemetry.Item telemetryInit, Start.StartLocation startLocation, String webcamName, boolean useCameraStream, Start.TeamColor teamColor) {
        telemetry = telemetryInit;

        // Create new pipeline
        pipeline = new CustomElementPipeline(webcamName, startLocation, teamColor);

        //Get webcam and init OpenCV on webcam
        WebcamName webcam = hardwareMap.get(WebcamName.class, webcamName);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        if (useCameraStream) {
            camera = OpenCvCameraFactory.getInstance().createWebcam(webcam, cameraMonitorViewId);
        } else {
            camera = OpenCvCameraFactory.getInstance().createWebcam(webcam);
        }

        // Specify the pipeline the camera is going to use to detect special elements
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

    public Start.CustomElement getLocation() {
        Start.CustomElement location = pipeline.getLocation();
        telemetry.setValue("Element position: " + location);
        return location;
    }

    public void stopStream() {
        camera.stopStreaming();
    }
}
