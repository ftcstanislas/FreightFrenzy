package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import java.lang.reflect.Array;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.teamcode.Odometry_Sample.OdometryGlobalCoordinatePosition;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

@Autonomous(name="Final Autonomous 3.∞", group="Iterative Opmode")
public class AutonomousV3 extends OpMode {

    //instructions
    String path = String.format("%sroute1.txt", Environment.getExternalStorageDirectory().getAbsolutePath());
    File routeFolder = new File(path);

    // *****PARAMETER 1*****

    // *****PARAMETER 2*****

    // *****PARAMETER 3...*****



    int instruction = 0;
    List<Integer> unfinishedInstructions = new ArrayList<Integer>();


    //get objects


    // make telemetry
    Telemetry.Item status = null;
    Telemetry.Item telemetryDrivetrain = null;
    Telemetry.Item telemetryArm = null;
    Telemetry.Item telemetryIntake = null;
    Telemetry.Item telemetrySpinner = null;
    Telemetry.Item telemetryLocation = null;
    Telemetry.Item telemetryColorSensor = null;
    Telemetry.Item telemetryTest = null;

    //Output amount of rings
    String finalOutput = "niks";

    // sleeping
    double wakeUpTime = 0;

    // time out
    double timeOut = 0;

    // make runtime
    ElapsedTime runtime = new ElapsedTime();

    //position
    OdometryGlobalCoordinatePosition globalPositionUpdate;
    final double COUNTS_PER_INCH = 307.699557;
    Thread positionThread;

    //File for position
    File positionXFile = AppUtil.getInstance().getSettingsFile("positionX.txt");
    File positionYFile = AppUtil.getInstance().getSettingsFile("positionY.txt");
    File orientationFile = AppUtil.getInstance().getSettingsFile("positionOrientation.txt");

    @Override
    public void init() {

        //telemetry setup
        String startInfo = "      ";
        telemetry.setAutoClear(false);
        telemetry.setCaptionValueSeparator(": ");
        status = telemetry.addData("Status", "X");
        telemetryDrivetrain = telemetry.addData("Robot", "X");
        telemetryArm = telemetry.addData("Arm", "X");
        telemetryIntake = telemetry.addData("Intake", "X");
        telemetrySpinner = telemetry.addData("Spinner", "X");
        telemetryLocation = telemetry.addData("Location", "X");
        telemetryColorSensor = telemetry.addData("Color Sensor", "X");
        telemetryTest = telemetry.addData("Test: ", String.format("%sroute1.txt", Environment.getExternalStorageDirectory().getAbsolutePath()));

        //Initialize routes


        //Initialize objects
//        motors.init(hardwareMap);
//        motors.resetEncoders();
//
//        globalPositionUpdate = new OdometryGlobalCoordinatePosition(motors.leftBackDrive, motors.rightBackDrive, motors.rightFrontDrive, COUNTS_PER_INCH, 20);
//        positionThread = new Thread(globalPositionUpdate);
//        positionThread.start();
//
//
//        drivetrain.init(motors, telemetryDrivetrain, runtime);
//        drivetrain.setBrake(true);
//        driveTo.init(motors, drivetrain, telemetryInformation, globalPositionUpdate);
//        arm.init(motors);
//        arm.setStartPos(800);
//        camera.init(hardwareMap);
//        intake.init(motors);
//        ringPlacer.init(motors);
//        shooter.init(motors, ringPlacer, intake, telemetryTesten, globalPositionUpdate);
//        intakeLock.init(motors);
//        wings.init(motors);
//        wings.setMode(0);
//
//        arm.setGripperPos(1);
//
//
//        // position
//        // globalPositionUpdate.reverseNormalEncoder();
//        // globalPositionUpdate.reverseRightEncoder();
//        // globalPositionUpdate.reverseLeftEncoder();
//
//        //telemetry update
//        telemetryStatus.setValue("Initialized");
    }

    @Override
    public void init_loop() {
        //telemetry update
//        telemetryStatus.setValue("Init looping for "+runtime.toString());
//
//        // get camera
//        String output = camera.getRings();
//        if (output != "niks" && finalOutput != "Quad"){
//            finalOutput = output;
//        };
//
//        telemetryInformation.setValue(finalOutput);

    }

    @Override
    public void start() {
        //telemetry update
//        telemetryStatus.setValue("Starting");
//
//        // reset
//        // motors.resetEncoders();
//        runtime.reset();
//
//        // Lift arm a little bit
//        arm.resetEncoder();
//        arm.setArmMode(2);
//
//        if (finalOutput == "niks") {
//            instructions = instruction1V2;
//        } else if (finalOutput == "Single") {
//            instructions = instruction2V2;
//        } else if (finalOutput == "Quad") {
//            instructions = instruction3V2;
//        }
//
//        //telemetry update
//        telemetryStatus.setValue("Started");
    }

    @Override
    public void loop() {
        try {
            List<String> fileLines = Files.readAllLines(routeFolder.toPath());
            telemetryTest.setValue(fileLines);
        } catch (IOException e) {

        }
//        //telemetry update
//        telemetryStatus.setValue("Following program "+ finalOutput + " for "+runtime.toString());
//        telemetryLocation.setValue(globalPositionUpdate.getDisplay());
//
//
//        if (instruction<instructions.length){
//            //telemetry
//            String text = instruction+"/"+(instructions.length-1)+"   "+java.util.Arrays.toString(instructions[instruction]);
//            telemetryInstruction.setValue(text);
//
//            //execute instruction
//            boolean result = executeFunction(instructions[instruction]);
//
//            // check if done
//            if (result == true){
//                instruction += 1;
//            } else if (instructions[instruction][0] == 1){
//                unfinishedInstructions.add(instruction);
//                instruction += 1;
//            }
//        } else {
//            telemetryInstruction.setValue("Done");
//        }
//
//        //excute unfinished instructions
//        if (unfinishedInstructions.size()>0){
//            telemetryUnfinishedInstructions.setValue(unfinishedInstructions.toString());
//            for (int nummer=0; nummer<unfinishedInstructions.size();nummer++){
//                boolean result = executeFunction(instructions[unfinishedInstructions.get(nummer)]);
//                if (result==true){
//                    unfinishedInstructions.remove(unfinishedInstructions.get(nummer));
//                    nummer--;
//                }
//            }
//        } else {
//            telemetryUnfinishedInstructions.setValue("Done");
//        }
    }

    @Override
    public void stop() {
        //telemetry update
//        telemetryStatus.setValue("Stopping");
//
//        globalPositionUpdate.stop();
//
//        ReadWriteFile.writeFile(positionXFile, String.valueOf(globalPositionUpdate.returnXCoordinate()));
//        ReadWriteFile.writeFile(positionYFile, String.valueOf(globalPositionUpdate.returnYCoordinate()));
//        ReadWriteFile.writeFile(orientationFile, String.valueOf(globalPositionUpdate.returnOrientation()));
//
//        // for testing only (drive back to start position)
//        // int result = 0;
//        // while (result == 0){
//        //     result = driveTo.goToPositionNew(0, 0, 0.5, 0, 1000);
//        // 4
//
//        //telemetry update
//        telemetryStatus.setValue("Stopped");
    }

    public boolean executeFunction(double[] instruction){
//        boolean done = true;
//        String text = "";
//
//        double actionType = instruction[1];
//
//        //execute instuction
//        if (actionType == 10){
//            done = driveTo.goToPositionNew3(instruction[2], instruction[3], 1, instruction[4], 500);
//        } else if (actionType == 11){
//            done = driveTo.goToPositionNew3(instruction[2], instruction[3], 0.3, instruction[4], 500);
//        } else if (actionType == 12){
//            done = driveTo.goToCheckpoint(instruction[2], instruction[3], 1, instruction[4], 4000);
//        } else if (actionType == 13){
//            done = driveTo.goToOrientation(instruction[2], 1, 2);
//        } else if (actionType == 20){
//            done = shooter.setPower(instruction[2]);
//            text = shooter.getDisplay();
//        } else if (actionType == 21) {
//            done = shooter.setModeHighGoal((int) instruction[2]);
//            text = shooter.getDisplay();
//        } else if (actionType == 22){
//            if (timeOut == 0) {
//                timeOut = runtime.time() + 6;
//            }
//            done = shooter.shoot(false, true);
//            text = shooter.getDisplay();
//            if (done) {
//                timeOut = 0;
//            } else if (timeOut < runtime.time()) {
//                timeOut = 0;
//                ringPlacer.setMode(0);
//                intake.setMode(0);
//                shooter.setModeHighGoal(0);
//                text += "\nDone, took to long";
//                done = true;
//            }
//        } else if (actionType == 30){
//            done = arm.setArmPos((int) instruction[2]);
//            text = arm.getDisplay();
//        } else if (actionType == 31){
//            done = arm.setArmMode((int) instruction[2]);
//            text = arm.getDisplay();
//        } else if (actionType == 40){
//            done = arm.setGripperPos((int) instruction[2]);
//            text = arm.getDisplay();
//        } else if (actionType == 41){
//            done = arm.setGripperMode((int) instruction[2]);
//            text = arm.getDisplay();
//        } else if (actionType == 50){
//            done = intake.setPower(instruction[2]);
//            text = shooter.getDisplay();
//        } else if (actionType == 51){
//            done = intake.setMode((int) instruction[2]);
//            text = shooter.getDisplay();
//        } else if (actionType == 60){
//            done = ringPlacer.setPower(instruction[2]);
//            text = shooter.getDisplay();
//        } else if (actionType == 61){
//            done = ringPlacer.setMode((int) instruction[2]);
//            text = shooter.getDisplay();
//        } else if (actionType == 70){
//            done = intakeLock.setPos(instruction[2]);
//            text = intakeLock.getDisplay();
//        } else if (actionType == 71) {
//            done = intakeLock.setMode((int) instruction[2]);
//            text = intakeLock.getDisplay();
//        } else if (actionType == 80){
//            wings.setPos(instruction[2]);
//        } else if (actionType == 81){
//            wings.setMode((int) instruction[2]);
//        } else if (actionType == 90){
//            // calculate stop time
//            if (wakeUpTime == 0){
//                wakeUpTime = (double) runtime.time() + (double) instruction[2];
//            }
//
//            // check if time passed
//            if (wakeUpTime < runtime.time()){
//                wakeUpTime = 0;
//                done = true;
//            } else {
//                done = false;
//            }
//        } else {
//            // throw new java.lang.Error("Invalid action tupe " + actionType);
//        }
//
//        if (text != ""){
//            telemetryInformation.setValue(text);
//        }
//
//        return done;
        return false; //VERWIJDEREN
    }

}