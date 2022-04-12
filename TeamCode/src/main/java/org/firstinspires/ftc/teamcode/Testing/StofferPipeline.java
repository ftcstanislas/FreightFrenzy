package org.firstinspires.ftc.teamcode.Testing;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class StofferPipeline extends OpenCvPipeline {
    Telemetry telemetry;
    String cameraName;
    Mat mat = new Mat();

    static Rect[] ROIS = {
            new Rect(), new Rect(), new Rect(), new Rect(), new Rect(), new Rect(), new Rect(), new Rect(),
    };
    static Mat[] subs = {
            new Mat(), new Mat(), new Mat(), new Mat(), new Mat(), new Mat(), new Mat(), new Mat(),
    };
    static double[] subValues = {
            0, 0, 0, 0, 0, 0, 0, 0,
    };
    static boolean[] doesSubContainElement = {
            false, false, false, false, false, false, false, false,
    };

    static Rect ROI = new Rect(
            new Point(80,80),
            new Point(160, 240)
    );

    static double PERCENT_COLOR_THRESHOLD = 0.3;

    public StofferPipeline(Telemetry t, String cn) {
        telemetry = t;
        cameraName = cn;
        for (int x=0; x<8; x++) {
            ROIS[x] = new Rect(
                    new Point(x*40, 210),
                    new Point(x*40+40, 240)
            );
        }
    }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        Scalar lowHSV = new Scalar(10, 100, 20);
        Scalar highHSV = new Scalar(25, 255, 255);

        Core.inRange(mat, lowHSV, highHSV, mat);
        //remove random white spots
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(15, 15));
        Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_OPEN, kernel);

        for (int x=0; x<8; x++) {
            subs[x] = mat.submat(ROIS[x]);
        }
//        Mat sub = mat.submat(ROI)
//

//        double subValue = Core.sumElems(sub).val[0] / ROI.area() / 255;
//        sub.release();
        for (int x=0; x<8; x++) {
            subValues[x] = Core.sumElems(subs[x]).val[0] / ROIS[x].area() / 255;
            subs[x].release();
        }

//        telemetry.addData("Left raw value", (int) Core.sumElems(sub).val[0]);
//
//        telemetry.addData("Left percentage", Math.round(subValue) * 100);

//        boolean element = subValue < PERCENT_COLOR_THRESHOLD;
        for (int x=0; x<8; x++) {
            doesSubContainElement[x] = subValues[x] < PERCENT_COLOR_THRESHOLD;
        }

        telemetry.update();

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

        Scalar colorElement = new Scalar(0, 255, 0);
        Scalar colorEmpty = new Scalar(255, 0, 0);

//        Imgproc.rectangle(mat, ROI, (element ? colorElement : colorEmpty));
        for (int x=0; x<8; x++) {
                Imgproc.rectangle(mat, ROIS[x], (doesSubContainElement[x] ? colorElement : colorEmpty));
        }

        return mat;
    }
    public String getRectWithElement() {
        String locations = "";
        for (int x=0; x<8; x++) {
            if (doesSubContainElement[x]) {
                locations += "(" + x + ") ";
            }
        }
        return locations;
    }
}
