package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Location.Location;
import org.firstinspires.ftc.teamcode.RobotParts.Arm;
import org.firstinspires.ftc.teamcode.RobotParts.Intake;
import org.firstinspires.ftc.teamcode.RobotParts.MecanumDrive;
import org.firstinspires.ftc.teamcode.RobotParts.Spinner;
import org.firstinspires.ftc.teamcode.Sensors.ColorDetector;

import java.util.ArrayList;
import java.util.Collections;

@TeleOp(name="OpMode 3.10", group="Iterative Opmode")
public class TeleOpV3 extends OpMode {
    
    //get objects
    Location location = new Location();
    MecanumDrive drivetrain = new MecanumDrive();
    Arm arm = new Arm();
    Intake intake = new Intake();
    Spinner spinner = new Spinner();
    ColorDetector colorSensor = new ColorDetector();
    
    // telemetry
    Telemetry.Item status = null;
    Telemetry.Item telemetryDrivetrain = null;
    Telemetry.Item telemetryArm = null;
    Telemetry.Item telemetryIntake = null;
    Telemetry.Item telemetrySpinner = null;
    Telemetry.Item telemetryLocation = null;
    Telemetry.Item telemetryDucks = null;
    Telemetry.Item telemetryColorSensor = null;

    // make runtime
    ElapsedTime runtime = new ElapsedTime();

    // variables
    long lastTime = 0;

    @Override
    public void init() {
        
        //add telemetry
        telemetry.setAutoClear(false);
        telemetry.setCaptionValueSeparator(": ");
        status = telemetry.addData("Status", "X");
        telemetryDrivetrain = telemetry.addData("Robot", "X");
        telemetryArm = telemetry.addData("Arm", "X");
        telemetryIntake = telemetry.addData("Intake", "X");
        telemetrySpinner = telemetry.addData("Spinner", "X");
        telemetryLocation = telemetry.addData("Location", "X");
        telemetryColorSensor = telemetry.addData("Color Sensor", "X");
        telemetryDucks = telemetry.addData("Ducks", "X");

        // position
        // File xFile = AppUtil.getInstance().getSettingsFile("positionX.txt");
        // File yFile = AppUtil.getInstance().getSettingsFile("positionY.txt");
        // File OrientationFile = AppUtil.getInstance().getSettingsFile("positionOrientation.txt");
        // double x = Double.parseDouble(ReadWriteFile.readFile(xFile).trim());
        // double y = Double.parseDouble(ReadWriteFile.readFile(yFile).trim());
        // double o = Double.parseDouble(ReadWriteFile.readFile(OrientationFile).trim());
        // globalPositionUpdate.setPosition(x,y,o);
        // positionThread = new Thread(globalPositionUpdate);
        // positionThread.start();
        // globalPositionUpdate.reverseNormalEncoder();
        // globalPositionUpdate.reverseRightEncoder();
        // globalPositionUpdate.reverseLeftEncoder();
        
        //Initialize objects part 2
        location.init(hardwareMap, telemetryLocation, telemetryDucks);
        drivetrain.init(hardwareMap, telemetryDrivetrain, location);
        drivetrain.setBrake(true);
        arm.init(hardwareMap, telemetryArm);
        intake.init(hardwareMap, telemetryIntake);
//        spinner.init(hardwareMap, telemetrySpinner);
//        colorSensor.init(hardwareMap, telemetryColorSensor);

        //Start duck detection
        location.startDuckDetection();

        status.setValue("Initialized");
    }
    
    @Override
    public void init_loop() {
        status.setValue("Init looping");
        location.update();
    }

    
    @Override
    public void start() {
        status.setValue("Starting");
        runtime.reset();
//        String test = telemetry.addAction(getStatus);
//        status.setValue("%s", test);
        lastTime = System.nanoTime();

        //Stop duck detection
        location.stopDuckDetection();
    }

    @Override
    public void loop() {
        // telemetry
        long time = System.nanoTime();
        status.setValue("Looping for " + runtime.toString() + " in "+(time - lastTime)/1000000+"ms");
        lastTime = time;

        // status.setValue("\nX:"+globalPositionUpdate.returnXCoordinate()+"\nY:"+
        // globalPositionUpdate.returnYCoordinate()+"\n O:"+globalPositionUpdate.returnOrientation());
        
        drivetrain.checkController(gamepad1, gamepad2);
        arm.checkController(gamepad1, gamepad2);
        intake.checkController(gamepad1, gamepad2);
        spinner.checkController(gamepad1, gamepad2);
//        colorSensor.update();
        location.update();

//        if (gamepad1.a) {
//            location.switchServo();
//        }

    }

    @Override
    public void stop() {
        status.setValue("Stopping");
    }
}