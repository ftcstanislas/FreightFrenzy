package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

@TeleOp
public class BallOrCube extends LinearOpMode {
    OpenCvCamera webcam;
    SamplePipeline pipeline;

    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        pipeline = new SamplePipeline();
        webcam.setPipeline(pipeline);


        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Type", pipeline.getType());
            telemetry.addData("Average", pipeline.getAverage());
            telemetry.addData("Test", pipeline.getContours());
            telemetry.update();
            sleep(50);
        }
    }

    public static class SamplePipeline extends OpenCvPipeline {
        private static final Scalar BLUE = new Scalar(0, 0, 255);

        private static final int THRESHOLD = 107;

        Point topLeft = new Point(80, 80);
        Point bottomRight = new Point(100, 100);

        Mat region1_Cb;
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();

        List<MatOfPoint> contours = new ArrayList<>();

        Mat mat = new Mat();

        private volatile int average;
        private volatile TYPE type = TYPE.BALL;

        private void inputToCb(Mat input) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);

            Core.extractChannel(YCrCb, Cb, 2);
        }

        @Override
        public void init(Mat input) {
//            inputToCb(input);

            processFrame(input);

//            region1_Cb = Cb.submat(new Rect(topLeft, bottomRight));
        }

        @Override
        public Mat processFrame(Mat input) {
//            inputToCb(input);
//
//            average = (int) Core.mean(region1_Cb).val[0];
//
//            Imgproc.rectangle(input, topLeft, bottomRight, BLUE, 2);
//
//            if (average > THRESHOLD) {
//                type = TYPE.BALL;
//            } else {
//                type = TYPE.CUBE;
//            }


            //Convert images to HSV
            Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

            //HSV Range (Hue, saturation, value)
            Scalar lowHSV = new Scalar(0, 255, 255);
            Scalar highHSV = new Scalar(255, 255, 255);

            //Create threshholding to greyscale
            Core.inRange(mat, lowHSV, highHSV, mat);

            // Remove Noise
            Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_OPEN, new Mat());
            Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_CLOSE, new Mat());

            // GaussianBlur
            Imgproc.GaussianBlur(mat, mat, new Size(5.0, 15.0), 0.00);

            // Find Contours
            contours = new ArrayList<MatOfPoint>();
            Imgproc.findContours(mat, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

            // Draw Contours
            Imgproc.drawContours(input, contours, -1, new Scalar(255, 0, 0));

            return input;
        }

        public TYPE getType() {
            return type;
        }

        public int getAverage() {
            return average;
        }

        public String getContours() {
            return contours.toString();
        }

        public enum TYPE {
            BALL, CUBE
        }
    }
}