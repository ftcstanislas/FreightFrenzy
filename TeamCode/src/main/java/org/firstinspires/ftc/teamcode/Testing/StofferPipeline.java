package org.firstinspires.ftc.teamcode.Testing;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class StofferPipeline extends OpenCvPipeline {
    Telemetry telemetry;
    String cameraName;
    Mat mat = new Mat();
    public enum Location {
        LEFT,
        MID,
        RIGHT,
    }

    private Location location;
    /* ===================================================== */
    /* =============== UPDATE THESE VALUES!!!! ============= */
    /* ===================================================== */
    static final Rect LEFT_ROI = new Rect(
            new Point(0, 100),
            new Point(120, 200)
    );
//    static final Rect MID_ROI = new Rect(
//            new Point(100, 100),
//            new Point(200, 200)
//    );
    static final Rect RIGHT_ROI = new Rect(
            new Point(200, 100),
            new Point(320, 200)
    );
    static double PERCENT_COLOR_THRESHOLD = 0.3;
    /* ===================================================== */
    /* =============== UPDATE THESE VALUES!!!! ============= */
    /* ===================================================== */

    public StofferPipeline(Telemetry t, String cn) {
        telemetry = t;
        cameraName = cn;
    }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        Scalar lowHSV = new Scalar(10, 100, 20);
        Scalar highHSV = new Scalar(25, 255, 255);

        Core.inRange(mat, lowHSV, highHSV, mat);

        Mat left = mat.submat(LEFT_ROI);
//        Mat mid = mat.submat(MID_ROI);
        Mat right = mat.submat(RIGHT_ROI);

        double leftValue = Core.sumElems(left).val[0] / LEFT_ROI.area() / 255;
//        double midValue = Core.sumElems(mid).val[0] / MID_ROI.area() / 255;
        double rightValue = Core.sumElems(right).val[0] / RIGHT_ROI.area() / 255;

        left.release();
//        mid.release();
        right.release();

        telemetry.addData("Left raw value", (int) Core.sumElems(left).val[0]);
//        telemetry.addData("Mid raw value", (int) Core.sumElems(mid).val[0]);
        telemetry.addData("Right raw value", (int) Core.sumElems(right).val[0]);

        telemetry.addData("Left percentage", Math.round(leftValue) * 100);
//        telemetry.addData("Mid percentage", Math.round(midValue) * 100);
        telemetry.addData("Right percentage", Math.round(rightValue) * 100);

        boolean elementLeft = leftValue > PERCENT_COLOR_THRESHOLD;
//        boolean elementMid = midValue > PERCENT_COLOR_THRESHOLD;
        boolean elementRight = rightValue > PERCENT_COLOR_THRESHOLD;

        if (elementLeft) {
            if (cameraName == "Webcam 1") {
                location = Location.MID;
                telemetry.addData("Element location:", "Mid");
            } else {
                location = Location.LEFT;
                telemetry.addData("Element location:", "Left");
            }
        }
//        else if (elementMid) {
//            location = Location.MID;
//            telemetry.addData("Element location:", "Mid");
//        }
        else if (elementRight) {
            if (cameraName == "Webcam 1") {
                location = Location.RIGHT;
                telemetry.addData("Element location:", "Right");
            } else {
                location = Location.MID;
                telemetry.addData("Element location:", "Mid");
            }
        } else {
            location = Location.LEFT;
            telemetry.addData("Element location:", "Left");
        }
        telemetry.update();

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

        Scalar colorElement = new Scalar(0,255,0);
        Scalar colorEmpty = new Scalar(255,0,0);

        Imgproc.rectangle(mat, LEFT_ROI, (location == Location.LEFT && cameraName == "Webcam 2") || (location == Location.MID && cameraName == "Webcam 1") ? colorElement : colorEmpty);
//        Imgproc.rectangle(mat, MID_ROI, location == Location.MID ? colorElement : colorEmpty);
        Imgproc.rectangle(mat, RIGHT_ROI, (location == Location.RIGHT && cameraName == "Webcam 1") || (location == Location.MID && cameraName == "Webcam 2") ? colorElement : colorEmpty);

        return mat;
    }

    public Location getLocation() {
        return location;
    }
}
