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

@TeleOp(name="OpMode Iterative 3.12", group="main")
public class TeleOpV3 extends OpMode {
    // Init time longer
    {
        msStuckDetectInit = 10000;
    }
    
    //get objects
    Location location = new Location();
    MecanumDrive drivetrain = new MecanumDrive();
    Arm arm = new Arm();
    Intake intake = new Intake();
    Spinner spinner = new Spinner();
//    ColorDetector colorSensor = new ColorDetector();
    
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
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
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
        
        //Initialize objects
        drivetrain.init(hardwareMap, telemetryDrivetrain, location);
        drivetrain.setBrake(true);
        location.init(hardwareMap, true, new double[]{-1200, -1200, 180}, drivetrain, telemetryLocation, telemetryDucks);
        arm.init(hardwareMap, telemetryArm);
        intake.init(hardwareMap, telemetryIntake);
        spinner.init(hardwareMap, telemetrySpinner);
//        colorSensor.init(hardwareMap, telemetryColorSensor);

        status.setValue("Initialized");
    }
    
    @Override
    public void init_loop() {
        long time = System.nanoTime();
//        status.setValue("Init looping for " + runtime.toString());
        status.setValue(String.format("Init looping for %.1fs in %.1fms",
                runtime.seconds(),  (double) (time - lastTime)/1000000));
        lastTime = time;
        location.update();
    }

    
    @Override
    public void start() {
        status.setValue("Starting");
        runtime.reset();
//        String test = telemetry.addAction(getStatus);
//        status.setValue("%s", test);

        lastTime = System.nanoTime();
    }

    @Override
    public void loop() {
        // Telemetry
        long time = System.nanoTime();
        status.setValue(String.format("Looping for %.1fs in %.1fms",
                runtime.seconds(),  (double) (time - lastTime)/1000000));
        lastTime = time;

        // status.setValue("\nX:"+globalPositionUpdate.returnXCoordinate()+"\nY:"+
        // globalPositionUpdate.returnYCoordinate()+"\n O:"+globalPositionUpdate.returnOrientation());
        
        drivetrain.checkController(gamepad1, gamepad2);
        arm.checkController(gamepad1, gamepad2);
        intake.checkController(gamepad1, gamepad2);
        spinner.checkController(gamepad1, gamepad2);
//        colorSensor.update();
        location.update();

        // Calibrate
        if (gamepad1.left_stick_button && gamepad1.right_stick_button) {
            arm.calibrate();
        }

//        if (gamepad1.a) {
//            location.switchServo();
//        }

    }

    @Override
    public void stop() {
        status.setValue("Stopping");
    }
}