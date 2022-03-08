package org.firstinspires.ftc.teamcode.Testing;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class RectangleTest extends OpenCvPipeline {

    static final Rect LEFT_ROI = new Rect(
            new Point(10, 80),
            new Point(100, 160)
            );
    static final Rect MID_ROI = new Rect(
            new Point(105, 80),
            new Point(205, 160)
            );
    static final Rect RIGHT_ROI = new Rect(
            new Point(210, 80),
            new Point(310, 160)
            );

    public RectangleTest() {

    }

    @Override
    public Mat processFrame(Mat input) {
        Scalar rectangleColor = new Scalar(255,0,0);

        Imgproc.rectangle(input, LEFT_ROI, rectangleColor);
        Imgproc.rectangle(input, MID_ROI, rectangleColor);
        Imgproc.rectangle(input, RIGHT_ROI, rectangleColor);

        return input;
    }
}
