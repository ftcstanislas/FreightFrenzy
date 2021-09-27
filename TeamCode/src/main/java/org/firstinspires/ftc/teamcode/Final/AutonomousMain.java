package org.firstinspires.ftc.teamcode.Final;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;
import java.io.File;
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

@Autonomous(name="Final Autonomous 2.0", group="Iterative Opmode")

public class AutonomousMain extends OpMode {
    
    //instructions
    
    // *****PARAMETER 1*****
    // 0: wait until finished, 1: with next instustruction
    
    // *****PARAMETER 2*****
    // 1: driving, 0: fast, 1: slow, 2: checkpoint, 3: turn 
    // 2: shooter 0: set velocity, 1: set mode, 2: shoot
    // 3: arm 0: set pos, 1: set mode
    // 4: gripper 0: set pos (0.7,1), 1: set mode
    // 5: intake 0: power, 1: set mode
    // 6: ringplacer 0: power, 1: set mode
    // 7: intakeLock 0: power, 1: set mode
    // 8: wing 0: set pos, 1: set mode
    // 9: wait
    
    // *****PARAMETER 3...*****
    // options
    
    // final instructions
    double[][] instructions;
    
    // part of instructions
    
    double[][] instruction1 = {
        // {0,21, 1}, {0,10,60000,0,0},
        {0,80,0.9},{0,80,0.5},{0,80,0.9},{0,80,0.5},
        // {0,22}, {0,22}, {0,22},
        // {0,10,0,60000,0}, {0,10,30000,60000,0}, {0,10,30000,0,0}, {0,10,0,0,0},
        // {0,10,0,60000,0}, {0,10,30000,60000,0}, {0,10,30000,0,0}, {0,10,0,0,0},
        // {0,10,0,60000,0}, {0,10,30000,60000,0}, {0,10,30000,0,0}, {0,10,0,0,0}
        // {0,10,34710,Math.sqrt(Math.pow(69420,2)-Math.pow(34710,2)),0},{0,10,69420,0,0},{0,10,0,0,0},
        // {0,10,34710,Math.sqrt(Math.pow(69420,2)-Math.pow(34710,2)),0},{0,10,69420,0,0},{0,10,0,0,0},
        // {0,10,34710,Math.sqrt(Math.pow(69420,2)-Math.pow(34710,2)),0},{0,10,69420,0,0},{0,10,0,0,0}
    };
    
    double[][] instruction2 = {
        {0,92}
    };
    
    double[][] instruction3 = {
        {0,92}
    };
    
    // instuction different amount of rings V2
    double[][] instruction1V2 = {
        {0,71,1}, {0,10,0,45000,-10}, {0,22}, {0,22}, {1,31,1}, {0,22}, {0,71,0},//shooting
        {0,10,10000,59000,0}, {0,31,0}, {0,41,0}, {0,90,0.1}, {0,31,1}, //deliver 1st wobble goal
        {0,10, 0, 20000, 0}, {0,10,0,9000,0}, {0,31,0}, {0,10,3000,12000,0}, {0,41,1}, {0,90,1}, {0,31,1}, //get 2nd wobble goal
        {0,10,10000,53000,0}, {0,31,0}, {0,41,0}, {0,90,0.1}, {0,31,1}, {0,41,1}, // deliver 2nd wobble goal
        {0,10,0,54000,0}, // drive to line
    };
    
    double[][] instruction2V2 = {
        {0,71,1}, {0,12,-11000,25000,0}, {0,10,0,45000,-10}, {0,22}, {0,22}, {1,31,1}, {0,22}, {0,71,0}, //shooting and prep arm
        {0,10,-6000,71000,0}, {0,31,0}, {0,41,0}, {0,90,0.1}, {0,31,1}, //deliver 1st wobble goal
        {0,51,1}, {0,61,1}, {0,10,-2000,9000,0}, {0,51,0}, {0,61,0},//get ring 
        {0,31,0}, {0,10,4000,11000,0}, {0,41,1}, {0,90,1}, {1,31,1}, //get 2nd wobble goal
        {0,10,8000,58000,-90}, {0,31,0}, {0,41,0}, {0,90,0.1}, {1,31,1}, //deliver 2nd wobble goal
        {0,10,0,53000,-90}, // drive to line
    };
    
    double[][] instruction3V2 = {
        {0,71,1}, {0,12,-11000,25000,0}, {0,10,0,45000,-10}, {0,22}, {0,22}, {1,31,1}, {0,22}, {0,71,0}, //shooting
        {0,12,7000,45000,0}, {0,10,7000,88000,0}, {0,31,0}, {0,41,0}, {0,90,0.1}, {0,31,1}, //deliver 1st wobble goal
        //{0,12,7000,40000,0}, {0,11,7000,36000,0}, {0,51,1}, {0,11,7000,24000,0}, {0,50,1}, //get rings
        {0,12,-2000,88000,0}, {0,12,-2000,20000,0},//{0,10,0,20000,0}, 
        {0,10,0,9000,0}, {0,31,0}, {0,10,12000,13000,0}, {0,41,1}, {0,90,1}, {0,31,1}, //get 2nd wobble goal
        // {0,10,0,44000,-15}, {0,22}, {0,22}, {0,22}, //2nd time shoot
        {0,12,1000,12000,0}, {0,10,1000,90000,0}, {0,31,0}, {0,41,0}, {0,90,0.1}, {0,31,1}, //deliver 2nd wobble goal 
        {0,10,0,53000,0}, //drive to line
    };
    
    // double[][] instruction3V3 = {
    //     {0,12,0,20000,0}, {0,12,15000,20000,0}, {0,80,0.4}, {0,12,15000,45000,0}, {0,71,1}, {0,81,0}, {0,10,0,45000,-10}, {0,22}, {0,22}, {1,31,1}, {0,22}, {0,71,0}, //shooting
    //     {0,12,45000,88000,0}, {0,10,15000,88000,0}, {0,31,0}, {0,41,0}, {0,90,0.1}, {0,31,1}, //deliver 1st wobble goal
    //     {0,12,2000,40000,0}, {0,10,5000,30000,0}, {0,51,1}, {0,10,5000,28000,0}, {0,50,0}, //get rings
    //     {0,10,0,45000,-10}, {0,22}, {0,22}, {0,22}, //2nd time shoot
    //     {0,10,0,53000,0}, //drive to line
    // };
    
    // double[][] instruction3V4 = {
    //     {0,12,0,20000,0}, {0,12,15000,20000,0}, {0,12,15000,45000,0}, {0,71,1}, {0,10,0,45000,-10}, {0,22}, {0,22}, {1,31,1}, {0,22}, {0,71,0}, //shooting
    //     {0,10,10000,88000,0}, {0,31,0}, {0,41,0}, {0,90,0.1}, {0,31,1}, //deliver 1st wobble goal {0,12,45000,88000,0}, 
    //     {0,12,2000,40000,0}, {0,51,1}, {0,11,5000,30000,0}, {0,50,0}, //get rings
    //     {0,10, 0, 20000, 0}, {0,10,0,9000,0}, {0,31,0}, {0,10,3000,12000,0}, {0,41,1}, {0,90,1}, {0,31,1}, //get 2nd wobble goal
    //     {0,10,0,45000,-10}, {0,22}, {0,22}, {0,22}, //2nd time shoot
    //     {0,10,0,53000,-90}, //drive to line
    // };
    
    int instruction = 0;
    List<Integer> unfinishedInstructions = new ArrayList<Integer>();
    
    
    //get objects
    PushBot motors = new PushBot();
    Shooter shooter = new Shooter();
    Drivetrain drivetrain = new Drivetrain();
    DriveTo driveTo = new DriveTo();
    Camera4 camera = new Camera4();
    Arm arm = new Arm();
    IntakeLock intakeLock = new IntakeLock();
    Intake intake = new Intake();
    RingPlacer ringPlacer = new RingPlacer();
    Wings wings = new Wings();
    
    // make telemetry
    Telemetry.Item telemetryStatus = null;
    Telemetry.Item telemetryLocation = null;
    Telemetry.Item telemetryRuntime = null;
    Telemetry.Item telemetryInstruction = null;
    Telemetry.Item telemetryUnfinishedInstructions = null;
    Telemetry.Item telemetryInformation = null;
    Telemetry.Item telemetryDrivetrain = null;
    Telemetry.Item telemetryDriveTo = null;
    Telemetry.Item telemetryTesten = null;
    
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
        telemetryStatus = telemetry.addData("⚫️ Status" , "X");
        telemetryLocation = telemetry.addData(startInfo+"location","X");
        telemetryInstruction = telemetry.addData("⚫️ Instructions" , "X");
        telemetryUnfinishedInstructions = telemetry.addData(startInfo+"Other instruction" , "X");
        telemetryInformation = telemetry.addData(startInfo+"Informaton" , "X");
        telemetryDrivetrain = telemetry.addData("⚫️ Robot" , "X");
        telemetryTesten = telemetry.addData("⚫️ Testen" , "X");


        //Initialize objects
        motors.init(hardwareMap);
        motors.resetEncoders();
        
        globalPositionUpdate = new OdometryGlobalCoordinatePosition(motors.leftBackDrive, motors.rightBackDrive, motors.rightFrontDrive, COUNTS_PER_INCH, 20);
        positionThread = new Thread(globalPositionUpdate);
        positionThread.start();
        
        
        drivetrain.init(motors, telemetryDrivetrain, runtime);
        drivetrain.setBrake(true);
        driveTo.init(motors, drivetrain, telemetryInformation, globalPositionUpdate);
        arm.init(motors);
        arm.setStartPos(800);
        camera.init(hardwareMap);
        intake.init(motors);
        ringPlacer.init(motors);
        shooter.init(motors, ringPlacer, intake, telemetryTesten, globalPositionUpdate);
        intakeLock.init(motors);
        wings.init(motors);
        wings.setMode(0);
        
        arm.setGripperPos(1);
        
        
        // position
        // globalPositionUpdate.reverseNormalEncoder();
        // globalPositionUpdate.reverseRightEncoder();
        // globalPositionUpdate.reverseLeftEncoder();
        
        //telemetry update
        telemetryStatus.setValue("Initialized");
    }

    @Override
    public void init_loop() {
        //telemetry update
        telemetryStatus.setValue("Init looping for "+runtime.toString());
        
        // get camera
        String output = camera.getRings();
        if (output != "niks" && finalOutput != "Quad"){
            finalOutput = output;
        };
        
        telemetryInformation.setValue(finalOutput);
        
    }

    @Override
    public void start() {
        //telemetry update
        telemetryStatus.setValue("Starting");
        
        // reset
        // motors.resetEncoders();
        runtime.reset();
        
        // Lift arm a little bit
        arm.resetEncoder();
        arm.setArmMode(2);
        
        if (finalOutput == "niks") {
            instructions = instruction1V2;
        } else if (finalOutput == "Single") {
            instructions = instruction2V2;
        } else if (finalOutput == "Quad") {
            instructions = instruction3V2;
        }
        
        //telemetry update
        telemetryStatus.setValue("Started");
    }

    @Override
    public void loop() {
        //telemetry update
        telemetryStatus.setValue("Following program "+ finalOutput + " for "+runtime.toString());
        telemetryLocation.setValue(globalPositionUpdate.getDisplay());
        
        
        if (instruction<instructions.length){
            //telemetry
            String text = instruction+"/"+(instructions.length-1)+"   "+java.util.Arrays.toString(instructions[instruction]);
            telemetryInstruction.setValue(text);
            
            //execute instruction
            boolean result = executeFunction(instructions[instruction]);
            
            // check if done
            if (result == true){
                instruction += 1;
            } else if (instructions[instruction][0] == 1){
                unfinishedInstructions.add(instruction);
                instruction += 1;
            }
        } else {
            telemetryInstruction.setValue("Done");
        }
        
        //excute unfinished instructions
        if (unfinishedInstructions.size()>0){
            telemetryUnfinishedInstructions.setValue(unfinishedInstructions.toString());
            for (int nummer=0; nummer<unfinishedInstructions.size();nummer++){
                boolean result = executeFunction(instructions[unfinishedInstructions.get(nummer)]);
                if (result==true){
                    unfinishedInstructions.remove(unfinishedInstructions.get(nummer));
                    nummer--;
                }
            }
        } else {
            telemetryUnfinishedInstructions.setValue("Done");
        }
    }

    @Override
    public void stop() {
        //telemetry update
        telemetryStatus.setValue("Stopping");
        
        globalPositionUpdate.stop();
        
        ReadWriteFile.writeFile(positionXFile, String.valueOf(globalPositionUpdate.returnXCoordinate()));
        ReadWriteFile.writeFile(positionYFile, String.valueOf(globalPositionUpdate.returnYCoordinate()));
        ReadWriteFile.writeFile(orientationFile, String.valueOf(globalPositionUpdate.returnOrientation()));
        
        // for testing only (drive back to start position)
        // int result = 0;
        // while (result == 0){
        //     result = driveTo.goToPositionNew(0, 0, 0.5, 0, 1000);
        // 4
        
        //telemetry update
        telemetryStatus.setValue("Stopped");
    }
    
    public boolean executeFunction(double[] instruction){
        boolean done = true;
        String text = "";
        
        double actionType = instruction[1];
        
        //execute instuction
        if (actionType == 10){
            done = driveTo.goToPositionNew3(instruction[2], instruction[3], 1, instruction[4], 500);
        } else if (actionType == 11){
            done = driveTo.goToPositionNew3(instruction[2], instruction[3], 0.3, instruction[4], 500);
        } else if (actionType == 12){
            done = driveTo.goToCheckpoint(instruction[2], instruction[3], 1, instruction[4], 4000);
        } else if (actionType == 13){
            done = driveTo.goToOrientation(instruction[2], 1, 2);
        } else if (actionType == 20){
            done = shooter.setPower(instruction[2]);
            text = shooter.getDisplay();
        } else if (actionType == 21) {
            done = shooter.setModeHighGoal((int) instruction[2]);
            text = shooter.getDisplay();
        } else if (actionType == 22){
            if (timeOut == 0) {
                timeOut = runtime.time() + 6;
            }
            done = shooter.shoot(false, true);
            text = shooter.getDisplay();
            if (done) {
                timeOut = 0;
            } else if (timeOut < runtime.time()) {
                timeOut = 0;
                ringPlacer.setMode(0);
                intake.setMode(0);
                shooter.setModeHighGoal(0);
                text += "\nDone, took to long";
                done = true;
            }
        } else if (actionType == 30){
            done = arm.setArmPos((int) instruction[2]);
            text = arm.getDisplay();
        } else if (actionType == 31){
            done = arm.setArmMode((int) instruction[2]);
            text = arm.getDisplay();
        } else if (actionType == 40){
            done = arm.setGripperPos((int) instruction[2]);
            text = arm.getDisplay();
        } else if (actionType == 41){
            done = arm.setGripperMode((int) instruction[2]);
            text = arm.getDisplay();
        } else if (actionType == 50){
            done = intake.setPower(instruction[2]);
            text = shooter.getDisplay();
        } else if (actionType == 51){
            done = intake.setMode((int) instruction[2]);
            text = shooter.getDisplay();
        } else if (actionType == 60){
            done = ringPlacer.setPower(instruction[2]);
            text = shooter.getDisplay();
        } else if (actionType == 61){
            done = ringPlacer.setMode((int) instruction[2]);
            text = shooter.getDisplay();
        } else if (actionType == 70){
            done = intakeLock.setPos(instruction[2]);
            text = intakeLock.getDisplay();
        } else if (actionType == 71) {
            done = intakeLock.setMode((int) instruction[2]);
            text = intakeLock.getDisplay();
        } else if (actionType == 80){
            wings.setPos(instruction[2]);
        } else if (actionType == 81){
            wings.setMode((int) instruction[2]);
        } else if (actionType == 90){
            // calculate stop time
            if (wakeUpTime == 0){
                wakeUpTime = (double) runtime.time() + (double) instruction[2];
            }
            
            // check if time passed
            if (wakeUpTime < runtime.time()){
                wakeUpTime = 0;
                done = true;
            } else {
                done = false;
            }
        } else {
            // throw new java.lang.Error("Invalid action tupe " + actionType);
        }
        
        if (text != ""){
            telemetryInformation.setValue(text);
        }
        
        return done;
    }

}