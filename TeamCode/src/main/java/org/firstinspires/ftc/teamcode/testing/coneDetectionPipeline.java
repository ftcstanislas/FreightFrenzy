package org.firstinspires.ftc.teamcode.testing;

import org.firstinspires.ftc.robotcore.exernal.Telemetry;
import org.opencv.core.mat;
import org.openftc.easyopencv.OpenCvPipeline;

public class coneDetectionPipeline extends openCvPipeline {
  
  Mat mat = new Mat();
  
  @Override
  public Mat processFrame(Mat input) {
    //Convert images to HSV
    Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
    
    //HSV Range (Hue, saturation, value)
    Scalar lowHSV = new Scalar(20, 255, 255);
    Scaler highHSV = new Scalar(40, 255, 255);
    
    //Create threshholding to greyscale
    Core.inRange(mat, lowHSV, highHSV, mat);
    
    // Remove Noise
    Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_OPEN, new Mat());
    Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_CLOSE, new Mat());
    
    // GaussianBlur
    Imgproc.GaussianBlur(mat, mat, new Size(5.0, 15.0), 0.00);
    
    // Find Contours
    List<MatOfPoint> contours = new ArrayList<>();
    Imgproc.findContours(mat, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
    
    // Draw Contours
    Imgproc.drawContours(input, contours, -1, new Scalar(255, 0, 0));
  }
}
