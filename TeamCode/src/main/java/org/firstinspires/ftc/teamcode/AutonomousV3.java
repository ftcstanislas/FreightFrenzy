package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.teamcode.Location.Location;
import org.firstinspires.ftc.teamcode.RobotParts.Arm;
import org.firstinspires.ftc.teamcode.RobotParts.Intake;
import org.firstinspires.ftc.teamcode.RobotParts.MecanumDrive;
import org.firstinspires.ftc.teamcode.RobotParts.Spinner;


@Autonomous(name="Final Autonomous 3.11", group="main")
public class AutonomousV3 extends OpMode {

    // Robot parts
    Location location = new Location();
    MecanumDrive drivetrain = new MecanumDrive();
    Arm arm = new Arm();
    Intake intake = new Intake();
    Spinner spinner = new Spinner();
    // ColorDetector colorSensor = new ColorDetector();

    //instructions
    Routes routes = new Routes();

    int instruction = 0;
    List<Object[]> unfinishedInstructions = new ArrayList<Object[]>();

    // make telemetry
    Telemetry.Item status = null;
    Telemetry.Item telemetryInstruction = null;
    Telemetry.Item telemetryUnfinishedInstructions = null;
    Telemetry.Item telemetryDrivetrain = null;
    Telemetry.Item telemetryArm = null;
    Telemetry.Item telemetryIntake = null;
    Telemetry.Item telemetrySpinner = null;
    Telemetry.Item telemetryLocation = null;
    Telemetry.Item telemetryColorSensor = null;
    Telemetry.Item telemetryDucks = null;
    Telemetry.Item telemetryTest = null;

    // Wich program to follow
    String[] program = {"blue", "onbekend"};
    Object[][] instructions = null;

    // sleeping
    double wakeUpTime = 0;

    // time out
    double timeOut = 0;

    // make runtime
    ElapsedTime runtime = new ElapsedTime();

    // variables
    long lastTime = 0;

    //position
    // OdometryGlobalCoordinatePosition globalPositionUpdate;
    // final double COUNTS_PER_INCH = 307.699557;
    // Thread positionThread;

    // File for position
    // File positionXFile = AppUtil.getInstance().getSettingsFile("positionX.txt");
    // File positionYFile = AppUtil.getInstance().getSettingsFile("positionY.txt");
    // File orientationFile = AppUtil.getInstance().getSettingsFile("positionOrientation.txt");

    @Override
    public void init() {

        //telemetry setup
        telemetry.setAutoClear(false);
        telemetry.setCaptionValueSeparator(": ");
        status = telemetry.addData("\uD83D\uDD34Status", "X");
        telemetryInstruction = telemetry.addData("\uD83D\uDD34Instruction", "X");
        telemetryUnfinishedInstructions = telemetry.addData("\uD83D\uDD34Unfinished Instructions", "X");
        telemetryDrivetrain = telemetry.addData("\uD83D\uDD34Robot", "X");
        telemetryArm = telemetry.addData("\uD83D\uDD34Arm", "X");
        telemetryIntake = telemetry.addData("\uD83D\uDD34Intake", "X");
        telemetrySpinner = telemetry.addData("\uD83D\uDD34Spinner", "X");
        telemetryLocation = telemetry.addData("\uD83D\uDD34Location", "X");
        telemetryColorSensor = telemetry.addData("\uD83D\uDD34Color Sensor", "X");
        telemetryDucks = telemetry.addData("\uD83D\uDD34Ducks", "X");
        telemetryTest = telemetry.addData("\uD83D\uDD34Test", "X");

        // Initialize objects
        drivetrain.init(hardwareMap, telemetryDrivetrain, location);
        drivetrain.setBrake(true);
        location.init(hardwareMap, true, new double[]{-1200, -600, 180}, drivetrain, telemetryLocation, telemetryDucks);
        arm.init(hardwareMap, telemetryArm);
        intake.init(hardwareMap, telemetryIntake);
        spinner.init(hardwareMap, telemetrySpinner);
        // colorSensor.init(hardwareMap, telemetryColorSensor);

        status.setValue("Initialized");
    }

    @Override
    public void init_loop() {

        //Telemetry update
        long time = System.nanoTime();
        status.setValue(String.format("Init looping for %.1fs in %.1fms",
                runtime.seconds(),  (double) (time - lastTime)/1000000));
        lastTime = time;

        // location
        location.update();


    }

    @Override
    public void start() {
        // Telemetry update
        status.setValue("Starting");
        
        // Reset
        runtime.reset();
        lastTime = System.nanoTime();

        // Correct instruction
        instructions = routes.getRoute(program[0], program[1]);

        //Telemetry update
        status.setValue("Started");
    }

    @Override
    public void loop() {
        // Telemetry update
        long time = System.nanoTime();
        status.setValue(String.format("Follwing program %s from %s for %.1fs in %.1fms",
                program[0], program[1], runtime.seconds(),  (double) (time - lastTime)/1000000));
        lastTime = time;

        // location
        location.update();

        // Execute instructions
        if (instruction<instructions.length){
            // Telemetry
            String text = instruction + "/" + (instructions.length-1) + "   " + java.util.Arrays.toString(instructions[instruction]);
            telemetryInstruction.setValue(text);

            // Execute instruction
            boolean result = executeFunction(instructions[instruction]);

            // Check if done
            if (result == true){
                instruction += 1;
            } else if (instructions[instruction][0].equals(false)){
                unfinishedInstructions.add(instructions[instruction]);
                instruction += 1;
            }
        } else {
            telemetryInstruction.setValue("Done with instructions");
        }

        // Execute unfinished instructions
        if (unfinishedInstructions.size()>0){
            telemetryUnfinishedInstructions.setValue(unfinishedInstructions.toString());
            for (int nummer=0; nummer<unfinishedInstructions.size();nummer++){
                boolean result = executeFunction(instructions[unfinishedInstructions.indexOf(nummer)]);
                if (result==true){
                    unfinishedInstructions.remove(unfinishedInstructions.get(nummer));
                    nummer--;
                }
            }
        } else {
            telemetryUnfinishedInstructions.setValue("Done with unfinished instructions");
        }
    }

    @Override
    public void stop() {
        //telemetry update
        status.setValue("Stopping");
        
        location.stop();
//        ReadWriteFile.writeFile(positionXFile, String.valueOf(globalPositionUpdate.returnXCoordinate()));
//        ReadWriteFile.writeFile(positionYFile, String.valueOf(globalPositionUpdate.returnYCoordinate()));
//        ReadWriteFile.writeFile(orientationFile, String.valueOf(globalPositionUpdate.returnOrientation()));

        //telemetry update
        status.setValue("Stopped");
    }

    public boolean executeFunction(Object[] instruction){

        boolean done = true;
        String part = (String) instruction[1]; //Cast to string (error fix)
        String function = (String) instruction[2];

        switch (part) {
            case "SPINNER":
                if (function == "mode") {
                    /*done =*/
                    spinner.setMode((String) instruction[3]);
                }
                break;

            case "INTAKE":
                if (function == "mode") {
                    /*done =*/
                    intake.setMode((String) instruction[3]);
                }
                break;

            case "ARM":
                if (function == "mode") {
                    /*done =*/
                    done = arm.setMode((String) instruction[3]);
                }
                ;
                break;

            case "WAIT":
                //Calculate wait time
                if (wakeUpTime == 0) {
                    wakeUpTime = (double) runtime.seconds() + (double) instruction[3];
                }

                //Check if time passed
                if (wakeUpTime < runtime.time()) {
                    wakeUpTime = 0;
                    done = true;
                } else {
                    done = false;
                }
                break;

            case "DRIVETRAIN":
                switch (function) {
                    case "toPosition":
                        //done = location.goToPosition(instruction[3], instruction[4], instruction[5], instruction[6]);  // x, y, rotation, speed
                        break;
                    case "toCircle":
                        //done = location.goToCircle(instruction[3], instruction[4], instruction[5]); // x, y, radius
                        break;

                    default: // if no match is found
                        throw new java.lang.Error("Part " + part + " does not exist");
                }
                break;
        }
        return done;
    }
}