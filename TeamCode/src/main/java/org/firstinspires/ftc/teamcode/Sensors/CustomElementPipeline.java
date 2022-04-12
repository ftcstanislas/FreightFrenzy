package org.firstinspires.ftc.teamcode.Sensors;

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

    // Initialize values
    String cameraName;
    Start.StartLocation start;
    Mat mat = new Mat();

    private Start.CustomElement location;
    private Start.TeamColor teamColor;

    static final Rect LEFT_BOX_WAREHOUSE = new Rect(
            new Point(50, 120),
            new Point(150, 220)
    );
    static final Rect RIGHT_BOX_WAREHOUSE = new Rect(
            new Point(190, 120),
            new Point(290, 220)
    );
    static final Rect LEFT_BOX_SPINNER = new Rect(
            new Point(0, 140),
            new Point(100, 230)
    );
    static final Rect RIGHT_BOX_SPINNER = new Rect(
            new Point(200, 140),
            new Point(300, 230)
    );

    // Threshold for when element is considered visible
    static double PERCENT_COLOR_THRESHOLD = 0.2;

    static Rect LEFT_RECTANGLE;

    static Rect RIGHT_RECTANGLE;

    // Constructor
    public CustomElementPipeline(String cn, Start.StartLocation startInit, Start.TeamColor teamColorInit) {
//        telemetry = t;

        //  Update local variables
        cameraName = cn;
        if (startInit == Start.StartLocation.SPINNER){
            LEFT_RECTANGLE = LEFT_BOX_SPINNER;
            RIGHT_RECTANGLE = RIGHT_BOX_SPINNER;
        } else if (startInit == Start.StartLocation.WAREHOUSE){
            LEFT_RECTANGLE = LEFT_BOX_WAREHOUSE;
            RIGHT_RECTANGLE = RIGHT_BOX_WAREHOUSE;
        }
        teamColor = teamColorInit;
        start = startInit;
    }

    @Override
    public Mat processFrame(Mat input) {
        /* Create a monochrome image with orange areas white and non-orange areas black
        An area is considered orange when it's HSV lies between the lower and upper HSV threshold */
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

        Scalar lowHSV = new Scalar(10, 100, 20); //Lower HSV
        Scalar highHSV = new Scalar(25, 255, 255); //Upper HSV
        Core.inRange(mat, lowHSV, highHSV, mat); //Update mat to show black and white areas

        // Create two submats to read data from
        Mat left = mat.submat(LEFT_RECTANGLE);
        Mat right = mat.submat(RIGHT_RECTANGLE);

        // Calculate percent of orange in submats
        double leftValue = Core.sumElems(left).val[0] / LEFT_RECTANGLE.area() / 255;
        double rightValue = Core.sumElems(right).val[0] / RIGHT_RECTANGLE.area() / 255;

        left.release();
        right.release();

//        telemetry.addData("Left raw value", (int) Core.sumElems(left).val[0]);
//        telemetry.addData("Right raw value", (int) Core.sumElems(right).val[0]);
//        telemetry.addData("Left percentage", Math.round(leftValue) * 100);
//        telemetry.addData("Right percentage", Math.round(rightValue) * 100);

        // Compare percentages to the thresholds
        boolean elementLeft = leftValue > PERCENT_COLOR_THRESHOLD;
        boolean elementRight = rightValue > PERCENT_COLOR_THRESHOLD;

        switch (teamColor) {
            case RED:
                switch (start) {
                    case SPINNER:
                        if (elementLeft) {
                            location = Start.CustomElement.LEFT;
                        } else if (elementRight) {
                            location = Start.CustomElement.MID;
                        } else {
                            location = Start.CustomElement.RIGHT;
                        }
                        break;

                    case WAREHOUSE:
                        if (elementLeft) {
                            location = Start.CustomElement.LEFT;
                        } else if (elementRight) {
                            location = Start.CustomElement.MID;
                        } else {
                            location = Start.CustomElement.RIGHT;
                        }
                        break;
                }
                break;

            case BLUE:
                switch (start) {
                    case SPINNER:
                        if (elementLeft) {
                            location = Start.CustomElement.MID;
                        } else if (elementRight) {
                            location = Start.CustomElement.RIGHT;
                        } else {
                            location = Start.CustomElement.LEFT;
                        }
                        break;

                    case WAREHOUSE:
                        if (elementLeft) {
                            location = Start.CustomElement.LEFT;
                        } else if (elementRight) {
                            location = Start.CustomElement.MID;
                        } else {
                            location = Start.CustomElement.RIGHT;
                        }
                }
                break;
        }

//        if (elementLeft) {
//            if (cameraName == "Webcam 1") {
//                if (teamColor == Start.TeamColor.BLUE) {
//                    location = Start.CustomElement.LEFT;
//                } else {
//                    location = Start.CustomElement.MID;
//                }
////                location = Start.CustomElement.MID;
////                telemetry.addData("Element location:", "Mid");
//            } else {
//                if (teamColor == Start.TeamColor.BLUE) {
//                    location = Start.CustomElement.MID;
//                } else {
//                    location = Start.CustomElement.LEFT;
//                }
////                telemetry.addData("Element location:", "Left");
//            }
//        }
//
//        else if (elementRight) {
//            if (cameraName == "Webcam 1") {
//                if (teamColor == Start.TeamColor.BLUE) {
//                    location = Start.CustomElement.MID;
//                } else {
//                    location = Start.CustomElement.RIGHT;
//                }
////                location = Start.CustomElement.RIGHT;
////                telemetry.addData("Element location:", "Right");
//            } else {
//                if (teamColor == Start.TeamColor.BLUE) {
//                    location = Start.CustomElement.RIGHT;
//                } else {
//                    location = Start.CustomElement.MID;
//                }
////                telemetry.addData("Element location:", "Mid");
//            }
//        } else {
//            if (cameraName == "Webcam 1") {
//                if (teamColor == Start.TeamColor.BLUE) {
//                    location = Start.CustomElement.RIGHT;
//                } else {
//                    location = Start.CustomElement.LEFT;
//                }
//            } else {
//                if (teamColor == Start.TeamColor.BLUE) {
//                    location = Start.CustomElement.LEFT;
//                } else {
//                    location = Start.CustomElement.RIGHT;
//                }
//            }
////            telemetry.addData("Element location:", "Left");
//        }

//        telemetry.update();

        // Convert mat back to RGB
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

        // Draw rectangles around submats
        Scalar colorElement = new Scalar(0,255,0);
        Scalar colorEmpty = new Scalar(255,0,0);

        Scalar colorLeft = elementLeft ? colorElement : colorEmpty;
        Scalar colorRight = elementRight ? colorElement : colorEmpty;

        // Imgproc.rectangle(mat, LEFT_RECTANGLE, (location == Start.CustomElement.LEFT && cameraName == "Webcam 2") || (location == Start.CustomElement.LEFT && cameraName == "Webcam 1") ? colorElement : colorEmpty);
        // Imgproc.rectangle(mat, RIGHT_RECTANGLE, (location == Start.CustomElement.MID && cameraName == "Webcam 1") || (location == Start.CustomElement.MID && cameraName == "Webcam 2") ? colorElement : colorEmpty);

        Imgproc.rectangle(mat, LEFT_RECTANGLE, colorLeft);
        Imgproc.rectangle(mat, RIGHT_RECTANGLE, colorRight);

        return mat;
    }

    public Start.CustomElement getLocation() {
        return location;
    }
}












//Jaron was here