package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Location.Location;
import org.firstinspires.ftc.teamcode.RobotParts.Arm;
import org.firstinspires.ftc.teamcode.RobotParts.MecanumDrive;
import org.firstinspires.ftc.teamcode.RobotParts.Spinner;
import org.firstinspires.ftc.teamcode.Sensors.CustomElementDetection;

import java.io.File;


public abstract class DefaultOpMode extends OpMode {
    // Time before stuck longer
    {
        msStuckDetectInit = 10000;
        msStuckDetectInitLoop = 10000;
        msStuckDetectLoop = 10000;
    }

    // Settings
    boolean useCameras = true;
    boolean useInstructions = false;

    // Robot parts
    CustomElementDetection customElementDetection = new CustomElementDetection();
    Location location = new Location();
    MecanumDrive drivetrain = new MecanumDrive();
    Spinner spinner = new Spinner();
    Arm arm = new Arm();

    // Telemetry
    Telemetry.Item status = null;
    Telemetry.Item telemetryInstruction = null;
    Telemetry.Item telemetryUnfinishedInstructions = null;
    Telemetry.Item telemetryDrivetrain = null;
    Telemetry.Item telemetryArm = null;
    Telemetry.Item telemetrySpinner = null;
    Telemetry.Item telemetryLocation = null;
    Telemetry.Item telemetryCustomElement = null;

    // Runtime
    ElapsedTime runtime = new ElapsedTime();
    long lastTime = 0; // Last update of loopTime

    // Position
    double[] locationRobot = getLastLocation();

    public void setUseCameras(boolean use) {
        useCameras = use;
    }

    public void setLocationRobot(double[] location) {
        locationRobot = location;
    }

    public void setUseInstructions(boolean use) {
        useInstructions = use;
    }

    private double[] getLastLocation() {
        double x = openFile("positionX.txt");
        double y = openFile("positionY.txt");
        double o = openFile("positionOrientation.txt");
        return new double[]{x, y, o};
    }

    @Override
    public void init() {

        // Telemetry setup
        telemetry.setAutoClear(false);
        telemetry.setCaptionValueSeparator(": ");
        status = telemetry.addData("\uD83D\uDD34Status", "X");
        if (useInstructions) {
            telemetryInstruction = telemetry.addData("\uD83D\uDD34Instruction", "X");
            telemetryUnfinishedInstructions = telemetry.addData("\uD83D\uDD34Unfinished Instructions", "X");
        }
        telemetryDrivetrain = telemetry.addData("\uD83D\uDD34Robot", "X");
        telemetryArm = telemetry.addData("\uD83D\uDD34Arm", "X");
        telemetrySpinner = telemetry.addData("\uD83D\uDD34Spinner", "X");
        telemetryLocation = telemetry.addData("\uD83D\uDD34Location", "X");
        if (useCameras) {
            telemetryCustomElement = telemetry.addData("\uD83D\uDD34Custom element", "X");
        }

        // Initialize objects
        drivetrain.init(hardwareMap, telemetryDrivetrain, location);
        drivetrain.setBrake(true);
        if (useCameras) {
            customElementDetection.init(hardwareMap, telemetryCustomElement, "Webcam 2",true);
            customElementDetection.startStream();
        }
        location.init(hardwareMap, useCameras, locationRobot, drivetrain, telemetryLocation, telemetryCustomElement);
        spinner.init(hardwareMap, telemetrySpinner);
        arm.init(hardwareMap, telemetryArm, location);

        status.setValue("Initialized");
    }

    @Override
    public void init_loop() {

        // Telemetry update
        double loopTime = getLoopTime();
        status.setValue(String.format("Init looping for %.1fs in %.1fms",
                runtime.seconds(), loopTime));

        globalUpdate();

        if (useCameras) {
            customElementDetection.getLocation();
        }

        // Location
        location.update();
        location.debug(gamepad1, gamepad2);
    }

    @Override
    public void start() {
        // Telemetry update
        status.setValue("Starting");

        if (useCameras){
            customElementDetection.stopStream();
        }

        // Reset
        runtime.reset();
        lastTime = System.nanoTime();

        // Telemetry update
        status.setValue("Started");
    }

    @Override
    abstract public void loop();

    @Override
    public void stop() {
        // Telemetry update
        status.setValue("Stopping");

        location.stop();

        // Save location
        saveValue("positionX.txt", location.getXCoordinate());
        saveValue("positionY.txt", location.getYCoordinate());
        saveValue("positionOrientation.txt", location.getOrientation());

        // Telemetry update
        status.setValue("Stopped");
    }

    public void globalUpdate() {
        location.update();
        arm.update();
    }

    public double getLoopTime() {
        long time = System.nanoTime();
        double performance = (double) (time - lastTime) / 1000000;
        lastTime = time;

        return performance;
    }

    private double openFile(String filename){
        File file = AppUtil.getInstance().getSettingsFile(filename);
        double value;
        try {
            value = Double.parseDouble(ReadWriteFile.readFile(file).trim());
        } catch (NumberFormatException e){
            value = 0;
        }
        return value;
    }

    private void saveValue(String filename, double value){
        File file = AppUtil.getInstance().getSettingsFile(filename);
        ReadWriteFile.writeFile(file, String.valueOf(value));
    }
}
