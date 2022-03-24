package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.List;

import org.firstinspires.ftc.teamcode.Location.Start;
import org.firstinspires.ftc.teamcode.OpModes.DefaultOpMode;
import org.firstinspires.ftc.teamcode.Routes;
import org.firstinspires.ftc.teamcode.Sensors.CustomElementPipeline;

public class AutonomousOpmode extends DefaultOpMode {
    // Instructions
    Routes routes = new Routes();

    int instruction = 0;

    // Wich program to follow
    Object[][] instructions = null;

    // sleeping
    double wakeUpTime = 0;

    // make runtime
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        super.setUseCameras(true);
        super.setUseInstructions(true);

        // Location robot
        double[] locationRobot = routes.getStartPosition(teamColor, startLocation);
        super.setLocationRobot(locationRobot);

        super.init();

        // Arm
        arm.setHeight(10);
        arm.setSpinnerAngle(locationRobot[2]);
    }

    @Override
    public void start() {
        customElement = customElementDetection.getLocation();

        // Correct instruction
        instructions = routes.getRoute(teamColor, startLocation, customElement);

        super.start();
    }

    @Override
    public void loop() {
        // Telemetry update
        double loopTime = getLoopTime();
        status.setValue(String.format("Following %s program %s with %s for %.1fs in %.1fms",
                teamColor, startLocation, customElement, runtime.seconds(),  loopTime));

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
            if (result == true ||  (boolean) instructions[instruction][0] == false){
                instruction += 1;
            }
        } else {
            telemetryInstruction.setValue("Done with instructions");
        }
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

            case "ARM":
                if (function == "toAngle"){
                    done = arm.setSpinnerAngle((double) instruction[3]);
                } else if (function == "toHeight") {
                    done = arm.setHeight((int) instruction[3]);
                } else if (function == "setIntake") {
                    arm.setMode((String) instruction[3]);
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
                        //Calculate wait time
                        if (wakeUpTime == 0) {
                            wakeUpTime = (double) runtime.time() + (double) instruction[7];
                        }

                        //Check if time passed
                        if (wakeUpTime < runtime.time()) {
                            wakeUpTime = 0;
                            drivetrain.setPowerDirection(0,0,0,0);
                            done = true;
                        } else {
                            done = false;
                        }
                        break;

                    default: // if no match is found
                        throw new java.lang.Error("Part " + function + " does not exist");
                }
                break;
        }
        return done;
    }
}