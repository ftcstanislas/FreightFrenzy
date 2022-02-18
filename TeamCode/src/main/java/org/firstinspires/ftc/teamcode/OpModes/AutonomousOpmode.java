package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.List;

import org.firstinspires.ftc.teamcode.OpModes.DefaultOpMode;
import org.firstinspires.ftc.teamcode.Routes;

public class AutonomousOpmode extends DefaultOpMode {
    // Instructions
    Routes routes = new Routes();

    int instruction = 0;
    List<Object[]> unfinishedInstructions = new ArrayList<Object[]>();

    // Wich program to follow
    public String[] program = {"unknown", "unknown", "unknown"};
    Object[][] instructions = null;

    // sleeping
    double wakeUpTime = 0;

    // make runtime
    ElapsedTime runtime = new ElapsedTime();

    public void setTeam(String team){
        program[0] = team;
    }

    public void setStart(String startPosition){
        program[1] = startPosition;
    }

    @Override
    public void init() {
        super.setUseCameras(true);
        super.setUseInstructions(true);

        // Location robot
        double[] locationRobot = routes.getStartPosition(program[0], program[1]);
        super.setLocationRobot(locationRobot);

        super.init();
    }

    @Override
    public void start() {
        // Correct instruction
        instructions = routes.getRoute(program[0], program[1], program[2]);

        super.start();
    }

    @Override
    public void loop() {
        // Telemetry update
        double loopTime = getLoopTime();
        status.setValue(String.format("Following program %s from %s with %s for %.1fs in %.1fms",
                program[0], program[1], program[2], runtime.seconds(),  loopTime));

        // Updates
        globalUpdate();

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
//        if (unfinishedInstructions.size() > 0){
//            telemetryUnfinishedInstructions.setValue(unfinishedInstructions.toString());
//            for (int nummer=0; nummer<unfinishedInstructions.size();nummer++){
//                boolean result = executeFunction(instructions[unfinishedInstructions.indexOf(nummer)]);
//                if (result==true){
//                    unfinishedInstructions.remove(unfinishedInstructions.get(nummer));
//                    nummer--;
//                }
//            }
//        } else {
//            telemetryUnfinishedInstructions.setValue("Done with unfinished instructions");
//        }
    }

    public boolean executeFunction(Object[] instruction){
        boolean done = true;
        String part = (String) instruction[1];
        String function = (String) instruction[2];

        switch (part) {
            case "SPINNER":
                if (function == "mode") {
                    spinner.setMode((String) instruction[3]);
                }
                break;

            case "INTAKE":
                if (function == "mode") {
                    intake.setMode((String) instruction[3]);
                }
                break;

            case "ARM":
                if (function == "toAngle"){
                    arm.setSpinnerAngle((double) instruction[3]);
                }
                break;

            case "WAIT":
                //Calculate wait time
                if (wakeUpTime == 0) {
                    wakeUpTime = (double) runtime.time() + (double) instruction[3];
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
                        done = location.goToPosition((double) instruction[3], (double) instruction[4], (double) instruction[5], (double) instruction[6]);  // x, y, rotation, speed
                        break;
                    case "toCircle":
                        done = location.goToCircle((double) instruction[3], (double) instruction[4], (double) instruction[5]); // x, y, radius
                        break;

                    case "setPower":
                        drivetrain.setPowerDirection((double) instruction[3], (double) instruction[4], (double) instruction[5], (double) instruction[6]);
                        break;

                    case "driveImu":
                        location.driveImu((double) instruction[3], (double) instruction[4], (double) instruction[5],  (double) instruction[6]);
                        break;

                    default: // if no match is found
                        throw new java.lang.Error("Part " + function + " does not exist");
                }
                break;
        }
        return done;
    }
}