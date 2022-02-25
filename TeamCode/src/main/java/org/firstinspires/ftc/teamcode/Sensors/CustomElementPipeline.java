package org.firstinspires.ftc.teamcode.Sensors;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Location.Start;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class CustomElementPipeline extends OpenCvPipeline {
//    Telemetry telemetry;
    String cameraName;
    Mat mat = new Mat();
    public enum Location {
        LEFT,
        MID,
        RIGHT,
    }

    private Start.CustomElement location;
    static final Rect LEFT_ROI = new Rect(
            new Point(20, 50),
            new Point(140, 150)
    );
    static final Rect RIGHT_ROI = new Rect(
            new Point(160, 50),
            new Point(240, 150)
    );
    static double PERCENT_COLOR_THRESHOLD = 0.2;

    public CustomElementPipeline(String cn) {
//        telemetry = t;
        cameraName = cn;
    }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        Scalar lowHSV = new Scalar(10, 100, 20);
        Scalar highHSV = new Scalar(25, 255, 255);

        Core.inRange(mat, lowHSV, highHSV, mat);

        Mat left = mat.submat(LEFT_ROI);
        Mat right = mat.submat(RIGHT_ROI);

        double leftValue = Core.sumElems(left).val[0] / LEFT_ROI.area() / 255;
        double rightValue = Core.sumElems(right).val[0] / RIGHT_ROI.area() / 255;

        left.release();
        right.release();

//        telemetry.addData("Left raw value", (int) Core.sumElems(left).val[0]);
//        telemetry.addData("Right raw value", (int) Core.sumElems(right).val[0]);
//
//        telemetry.addData("Left percentage", Math.round(leftValue) * 100);
//        telemetry.addData("Right percentage", Math.round(rightValue) * 100);

        boolean elementLeft = leftValue > PERCENT_COLOR_THRESHOLD;
        boolean elementRight = rightValue > PERCENT_COLOR_THRESHOLD;

        if (elementLeft) {
            if (cameraName == "Webcam 1") {
                location = Start.CustomElement.MID;
//                telemetry.addData("Element location:", "Mid");
            } else {
                location = Start.CustomElement.LEFT;
//                telemetry.addData("Element location:", "Left");
            }
        }

        else if (elementRight) {
            if (cameraName == "Webcam 1") {
                location = Start.CustomElement.RIGHT;
//                telemetry.addData("Element location:", "Right");
            } else {
                location = Start.CustomElement.MID;
//                telemetry.addData("Element location:", "Mid");
            }
        } else {
            if (cameraName == "Webcam 1") {
                location = Start.CustomElement.LEFT;
            } else {
                location = Start.CustomElement.RIGHT;
            }
//            telemetry.addData("Element location:", "Left");
        }

//        telemetry.update();

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

        Scalar colorElement = new Scalar(0,255,0);
        Scalar colorEmpty = new Scalar(255,0,0);

        Imgproc.rectangle(mat, LEFT_ROI, (location == Start.CustomElement.LEFT && cameraName == "Webcam 2") || (location == Start.CustomElement.MID && cameraName == "Webcam 1") ? colorElement : colorEmpty);
        Imgproc.rectangle(mat, RIGHT_ROI, (location == Start.CustomElement.RIGHT && cameraName == "Webcam 1") || (location == Start.CustomElement.MID && cameraName == "Webcam 2") ? colorElement : colorEmpty);

        return mat;
    }

    public Start.CustomElement getLocation() {
        return location;
    }
}
