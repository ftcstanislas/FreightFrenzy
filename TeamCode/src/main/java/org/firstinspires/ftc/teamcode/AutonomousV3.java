package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.teamcode.Location.Location;
import org.firstinspires.ftc.teamcode.RobotParts.Intake;
import org.firstinspires.ftc.teamcode.RobotParts.MecanumDrive;
import org.firstinspires.ftc.teamcode.RobotParts.Spinner;


@Autonomous(name="Final Autonomous 3.11", group="main")
public class AutonomousV3 extends OpMode {

    // Robot parts
    Location location = new Location();
    MecanumDrive drivetrain = new MecanumDrive();
    // Arm arm = new Arm();
    Intake intake = new Intake();
    Spinner spinner = new Spinner();
    // ColorDetector colorSensor = new ColorDetector();

    //instructions
//    Routes routes = new Routes();

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
        String startInfo = "      ";
        telemetry.setAutoClear(false);
        telemetry.setCaptionValueSeparator(": ");
        status = telemetry.addData("Status", "X");
        telemetryInstruction = telemetry.addData("Instruction", "X");
        telemetryUnfinishedInstructions = telemetry.addData("Unfinished Instructions", "X");
        telemetryDrivetrain = telemetry.addData("Robot", "X");
        telemetryArm = telemetry.addData("Arm", "X");
        telemetryIntake = telemetry.addData("Intake", "X");
        telemetrySpinner = telemetry.addData("Spinner", "X");
        telemetryLocation = telemetry.addData("Location", "X");
        telemetryColorSensor = telemetry.addData("Color Sensor", "X");
        telemetryDucks = telemetry.addData("Ducks", "X");
        telemetryTest = telemetry.addData("Test", "X");

        // Initialize objects
        drivetrain.init(hardwareMap, telemetryDrivetrain, location);
        drivetrain.setBrake(true);
        location.init(hardwareMap, drivetrain, telemetryLocation, telemetryDucks);
        // arm.init(hardwareMap, telemetryArm);
        intake.init(hardwareMap, telemetryIntake);
        spinner.init(hardwareMap, telemetrySpinner);
        // colorSensor.init(hardwareMap, telemetryColorSensor);

        status.setValue("Initialized");
    }

    @Override
    public void init_loop() {
        //Telemetry update
        status.setValue("Init looping for " + runtime.toString());
        
    }

    @Override
    public void start() {
        // Telemetry update
        status.setValue("Starting");
        
        // Reset
        runtime.reset();

        // Correct instruction
        instructions = Routes.getRoute(program[0], program[1]);

        //Telemetry update
        status.setValue("Started");
    }

    @Override
    public void loop() {
        // Telemetry update
        status.setValue("Following program " + program[0] + " from " + program[1] + " for " + runtime.toString());

        // location
        location.update();

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

        // Excute unfinished instructions
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
                    spinner.setMode((String) instruction[3]);
                }
                ;
                break;

            case "WAIT":
                //Calculate wait time
                if (wakeUpTime == 0) {
                    wakeUpTime = (double) runtime.time() + (double) instruction[2];
                }

                //Check if time passed
                if (wakeUpTime < runtime.time()) {
                    wakeUpTime = 0;
                    // done = true;
                } else {
                    // done = false;
                }

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
        }
        return done;
    }
}