package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.teamcode.utility.Globalvalues;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

// @Config //Disable if not using FTC Dashboard https://github.com/PinkToTheFuture/OpenCV_FreightFrenzy_2021-2022#opencv_freightfrenzy_2021-2022
@Autonomous(name="OpenCV_Contour_3954_Test", group="Tutorials")

public class YellowTest extends LinearOpMode {
    private OpenCvCamera webcam;

    private static final int CAMERA_WIDTH  = 320; // width  of wanted camera resolution
    private static final int CAMERA_HEIGHT = 240; // height of wanted camera resolution

    private double CrLowerUpdate = 160;
    private double CbLowerUpdate = 100;
    private double CrUpperUpdate = 255;
    private double CbUpperUpdate = 255;

    public static double borderLeftX    = 0.0;   //fraction of pixels from the left side of the cam to skip
    public static double borderRightX   = 0.0;   //fraction of pixels from the right of the cam to skip
    public static double borderTopY     = 0.0;   //fraction of pixels from the top of the cam to skip
    public static double borderBottomY  = 0.0;   //fraction of pixels from the bottom of the cam to skip

    private double lowerruntime = 0;
    private double upperruntime = 0;

    // Pink Range                                      Y      Cr     Cb
    // public static Scalar scalarLowerYCrCb = new Scalar(  0.0, 160.0, 100.0);
    // public static Scalar scalarUpperYCrCb = new Scalar(255.0, 255.0, 255.0);

    // Yellow Range
    public static Scalar scalarLowerYCrCb = new Scalar(0.0, 100.0, 0.0);
    public static Scalar scalarUpperYCrCb = new Scalar(255.0, 170.0, 120.0);

    // For calculating coordinates
    double rectArea = 0;
    double rectX = 0;
    double rectY = 0;
    double angleDegrees = 45;
    double angleRadians = Math.toRadians(angleDegrees);
    // calculation of angle would look like this:
    //            x - 160
    //  angle = ------------
    //            16 / 3
    double targetX = 0;
    double targetY = 0;
    double distance = 0;

    @Override
    public void runOpMode()
    {
        // OpenCV webcam
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        //OpenCV Pipeline
        YellowPipeline myPipeline;
        webcam.setPipeline(myPipeline = new YellowPipeline(borderLeftX,borderRightX,borderTopY,borderBottomY));
        // Configuration of Pipeline
        myPipeline.configureScalarLower(scalarLowerYCrCb.val[0],scalarLowerYCrCb.val[1],scalarLowerYCrCb.val[2]);
        myPipeline.configureScalarUpper(scalarUpperYCrCb.val[0],scalarUpperYCrCb.val[1],scalarUpperYCrCb.val[2]);
        // Webcam Streaming
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(CAMERA_WIDTH, CAMERA_HEIGHT, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });
        // Only if you are using ftcdashboard
//        FtcDashboard dashboard = FtcDashboard.getInstance();
//        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
//        FtcDashboard.getInstance().startCameraStream(webcam, 10);

        telemetry.update();
        waitForStart();

        while (opModeIsActive())
        {
            myPipeline.configureBorders(borderLeftX, borderRightX, borderTopY, borderBottomY);
            if(myPipeline.error){
                telemetry.addData("Exception: ", myPipeline.debug);
            }
            // Only use this line of the code when you want to find the lower and upper values
//            testing(myPipeline);

//            telemetry.addData("RectArea: ", myPipeline.getRectArea());
//            telemetry.update();


//            if(myPipeline.getRectArea() > 2000){
//                if(myPipeline.getRectMidpointX() > 400){
////                    AUTONOMOUS_C();
//                }
//                else if(myPipeline.getRectMidpointX() > 200){
////                    AUTONOMOUS_B();
//                }
//                else {
////                    AUTONOMOUS_A();
//                }
//            }

//            rectArea = myPipeline.getRectArea();
//
//            if (myPipeline.getRectArea() > 200) {
//                rectX = myPipeline.getRectMidpointX();
//                rectY = myPipeline.getRectMidpointY();
//
//                distance = rectY * 1.5; // placeholder, plz test actual value!
//                angleDegrees = (rectX-160) / (16.0/3.0); // convert [0,320] to [-30,30]
//                angleRadians = Math.toRadians(angleDegrees);
//                targetX = Math.cos(/* angleDegrees */ angleRadians) * distance;
//                targetY = Math.sin(/* angleDegrees */ angleRadians) * distance;
//                telemetry.addData("Target coords: ", "(" + targetX + "," + targetY + ")");
//            }

        }
    }
//    public void testing(YellowPipeline myPipeline){
//        if(lowerruntime + 0.05 < getRuntime()){
//            CrLowerUpdate += -gamepad1.left_stick_y;
//            CbLowerUpdate += gamepad1.left_stick_x;
//            lowerruntime = getRuntime();
//        }
//        if(upperruntime + 0.05 < getRuntime()){
//            CrUpperUpdate += -gamepad1.right_stick_y;
//            CbUpperUpdate += gamepad1.right_stick_x;
//            upperruntime = getRuntime();
//        }
//
//        CrLowerUpdate = inValues(CrLowerUpdate, 0, 255);
//        CrUpperUpdate = inValues(CrUpperUpdate, 0, 255);
//        CbLowerUpdate = inValues(CbLowerUpdate, 0, 255);
//        CbUpperUpdate = inValues(CbUpperUpdate, 0, 255);
//
//        myPipeline.configureScalarLower(0.0, CrLowerUpdate, CbLowerUpdate);
//        myPipeline.configureScalarUpper(255.0, CrUpperUpdate, CbUpperUpdate);
//
//        telemetry.addData("lowerCr ", (int)CrLowerUpdate);
//        telemetry.addData("lowerCb ", (int)CbLowerUpdate);
//        telemetry.addData("UpperCr ", (int)CrUpperUpdate);
//        telemetry.addData("UpperCb ", (int)CbUpperUpdate);
//    }
    public Double inValues(double value, double min, double max){
        if(value < min){ value = min; }
        if(value > max){ value = max; }
        return value;
    }
    public boolean inMargin(double value, double threshold, double margin) {
        return value <= threshold + margin && value >= threshold - margin;
    }
}