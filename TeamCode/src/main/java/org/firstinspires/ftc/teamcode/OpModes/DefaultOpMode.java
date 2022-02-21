package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Location.Location;
import org.firstinspires.ftc.teamcode.RobotParts.Arm;
import org.firstinspires.ftc.teamcode.RobotParts.Intake;
import org.firstinspires.ftc.teamcode.RobotParts.MecanumDrive;
import org.firstinspires.ftc.teamcode.RobotParts.Spinner;

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
    Location location = new Location();
    MecanumDrive drivetrain = new MecanumDrive();
    Intake intake = new Intake();
    Spinner spinner = new Spinner();
    Arm arm = new Arm();

    // Telemetry
    Telemetry.Item status = null;
    Telemetry.Item telemetryInstruction = null;
    Telemetry.Item telemetryUnfinishedInstructions = null;
    Telemetry.Item telemetryDrivetrain = null;
    Telemetry.Item telemetryArm = null;
    Telemetry.Item telemetryIntake = null;
    Telemetry.Item telemetrySpinner = null;
    Telemetry.Item telemetryLocation = null;
    Telemetry.Item telemetryDucks = null;

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
        File positionXFile = AppUtil.getInstance().getSettingsFile("positionX.txt");
        File positionYFile = AppUtil.getInstance().getSettingsFile("positionY.txt");
        File orientationFile = AppUtil.getInstance().getSettingsFile("positionOrientation.txt");
        double x;
        try {
            x = Double.parseDouble(ReadWriteFile.readFile(positionXFile).trim());
        } catch (NumberFormatException e){
            x = 0;
        }
        double y;
        try {
            y = Double.parseDouble(ReadWriteFile.readFile(positionYFile).trim());
        } catch (NumberFormatException e){
            y = 0;
        }
        double o;
        try {
            o = Double.parseDouble(ReadWriteFile.readFile(orientationFile).trim());
        } catch (NumberFormatException e){
            o = 0;
        }
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
        telemetryIntake = telemetry.addData("\uD83D\uDD34Intake", "X");
        telemetrySpinner = telemetry.addData("\uD83D\uDD34Spinner", "X");
        telemetryLocation = telemetry.addData("\uD83D\uDD34Location", "X");
        telemetryDucks = telemetry.addData("\uD83D\uDD34Ducks", "X");

        // Initialize objects
        drivetrain.init(hardwareMap, telemetryDrivetrain, location);
        drivetrain.setBrake(true);
        location.init(hardwareMap, useCameras, locationRobot, drivetrain, telemetryLocation, telemetryDucks);
        intake.init(hardwareMap, telemetryIntake);
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

        // Location
        location.update();
        location.debug(gamepad1, gamepad2);
    }

    @Override
    public void start() {
        // Telemetry update
        status.setValue("Starting");

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
        File positionXFile = AppUtil.getInstance().getSettingsFile("positionX.txt");
        File positionYFile = AppUtil.getInstance().getSettingsFile("positionY.txt");
        File orientationFile = AppUtil.getInstance().getSettingsFile("positionOrientation.txt");
        ReadWriteFile.writeFile(positionXFile, String.valueOf(location.getXCoordinate()));
        ReadWriteFile.writeFile(positionYFile, String.valueOf(location.getYCoordinate()));
        ReadWriteFile.writeFile(orientationFile, String.valueOf(location.getOrientation()));

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
}
