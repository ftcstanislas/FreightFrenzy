/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.testing;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

/**
 * This 2020-2021 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Freight Frenzy game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@TeleOp(name = "Duck Testing", group = "testing")
public class duckTest extends LinearOpMode {
    private String TFOD_MODEL_FILE = String.format("%s/FIRST/tflitemodels/goompa.tflite", Environment.getExternalStorageDirectory().getAbsolutePath());
    private String TFOD_MODEL_LABELS = String.format("%s/FIRST/tflitemodels/labels.txt", Environment.getExternalStorageDirectory().getAbsolutePath());
    private String[] labels;

    private static final String VUFORIA_KEY =
            "AYijR7b/////AAABmQXodEBY4E1gjxKsoSygzWsm4RFjj/z+nzPa0q3oo3vJNz51j477KysEdl4h1YfezCokKxkUeK3ARNjE1tH80M5gZbubu2bkdF6Ja8gINhJTAY/ZJrFkPGiiLfausXsCWAygHW7ufeu3FLIDp1DN2NHj4rzDP4vRv5z/0T0deRLucpRcv36hqUkJ1N6Duumo0se+BCmdAh7ycUW2wteJ3T1e/LxuO5sI6qtwnJW64fe2n6cehk5su76c9t45AcBfod6f0txGezdzpqY5NoHjz0G/gLvah0vAYW+/0x3yaWy8thEd64OVMVb2q37TsJ1UDjl5qupztdG7nXkRGYF5oaR8CGkm2lqPyugJuRFNMRcM";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        // read the label map text files.
        readLabels();

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
//            tfod.setZoom(2.5, 16.0/9.0);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                      telemetry.addData("# Object Detected", updatedRecognitions.size());
                      // step through the list of recognitions and display boundary info.
                      int i = 0;
                      for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        i++;
                      }
                      telemetry.update();
                    }
                }
            }
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
       tfodParameters.minResultConfidence = 0.8f;
//       tfodParameters.isModelTensorFlow2 = true;
//       tfodParameters.inputSize = 320;
       tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
       if (labels != null) {
           tfod.loadModelFromFile(TFOD_MODEL_FILE, labels);
       }
    }


    /**
     * Read the labels for the object detection model from a file.
     */
    private void readLabels() {
        ArrayList<String> labelList = new ArrayList<>();

        telemetry.addData("path", TFOD_MODEL_LABELS);
        telemetry.update();

        // try to read in the the labels.
        try (BufferedReader br = new BufferedReader(new FileReader(TFOD_MODEL_LABELS))) {
            int index = 0;
            while (br.ready()) {
                // skip the first row of the labelmap.txt file.
                // if you look at the TFOD Android example project (https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection/android)
                // you will see that the labels for the inference model are actually extracted (as metadata) from the .tflite model file
                // instead of from the labelmap.txt file. if you build and run that example project, you'll see that
                // the label list begins with the label "person" and does not include the first line of the labelmap.txt file ("???").
                // i suspect that the first line of the labelmap.txt file might be reserved for some future metadata schema
                // (or that the generated label map file is incorrect).
                // for now, skip the first line of the label map text file so that your label list is in sync with the embedded label list in the .tflite model.
                if(index == 0) {
                    // skip first line.
                    br.readLine();
                } else {
                    labelList.add(br.readLine());
                }
                index++;
            }
        } catch (Exception e) {
            telemetry.addData("Exception", e.getLocalizedMessage());
            telemetry.update();
        }

        if (labelList.size() > 0) {
            labels = getStringArray(labelList);
            RobotLog.vv("readLabels()", "%d labels read.", labels.length);
            for (String label : labels) {
                RobotLog.vv("readLabels()", " " + label);
            }
        } else {
            RobotLog.vv("readLabels()", "No labels read!");
        }
    }

    private String[] getStringArray(ArrayList<String> arr)
    {
        // declaration and initialize String Array
        String str[] = new String[arr.size()];

        // Convert ArrayList to object array
        Object[] objArr = arr.toArray();

        // Iterating and converting to String
        int i = 0;
        for (Object obj : objArr) {
            str[i++] = (String)obj;
        }

        return str;
    }

}
