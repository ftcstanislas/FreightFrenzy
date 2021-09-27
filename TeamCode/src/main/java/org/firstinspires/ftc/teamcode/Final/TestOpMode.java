// /*
// Copyright 2021 FIRST Tech Challenge Team 15614

// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
// associated documentation files (the "Software"), to deal in the Software without restriction,
// including without limitation the rights to use, copy, modify, merge, publish, distribute,
// sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all copies or substantial
// portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
// NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// */
// package org.firstinspires.ftc.teamcode.Final;

// import com.qualcomm.robotcore.eventloop.opmode.Disabled;
// import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
// import org.firstinspires.ftc.robotcore.external.Telemetry;
// import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
// import com.qualcomm.robotcore.hardware.HardwareMap;
// import com.qualcomm.robotcore.util.Hardware;
// import com.qualcomm.robotcore.eventloop.opmode.OpMode;
// import com.qualcomm.robotcore.hardware.DcMotor;
// import com.qualcomm.robotcore.util.ElapsedTime;
// import com.qualcomm.robotcore.util.Range;

// /**
//  * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
//  * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
//  * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
//  * class is instantiated on the Robot Controller and executed.
//  *
//  * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
//  * It includes all the skeletal structure that all linear OpModes contain.
//  *
//  * Remove a @Disabled the on the next line or two (if present) to add this opmode to the Driver Station OpMode list,
//  * or add a @Disabled annotation to prevent this OpMode from being added to the Driver Station
//  */
// @TeleOp(name="TeleOp Snapshot 21w12a", group="Linear Opmode")
// public class TestOpMode extends LinearOpMode {
    
//     // get objects
//     PushBot motors = new PushBot();
//     Shooter shooter = new Shooter();
//     Controller controller = new Controller();
//     Drivetrain drivetrain = new Drivetrain();
//     RingPlacer ringPlacer = new RingPlacer();
//     Arm arm = new Arm();
//     Intake intake = new Intake();
    
//     // telemetry
//     Telemetry.Item status = null;
    
//     // make runtime
//     ElapsedTime runtime = new ElapsedTime();

//     @Override
//     public void runOpMode() {
        
//         //add telemetry
//         telemetry.setAutoClear(false);
//         status = telemetry.addData("Status","%12.3f", 0.0);
//         Telemetry.Item telemetryDrivetrain = telemetry.addData("Robot" , "%12.3f", 0.0);

//         //Initialize objects
//         motors.init(hardwareMap);
//         shooter.init(motors);
//         ringPlacer.init(motors);
//         drivetrain.init(motors, telemetryDrivetrain, runtime);
//         controller.init(drivetrain, shooter, ringPlacer, arm, intake);
//         arm.init(motors);
//         intake.init(motors);
        
//         status.setValue("Initialized");
        
//         // Wait for the game to start (driver presses PLAY)
//         waitForStart();
//         status.setValue("Starting");
//         status.setValue("Started");

//         // run until the end of the match (driver presses STOP)
//         while (opModeIsActive()) {
//             status.setValue("looping");
//             controller.check(gamepad1, gamepad2);
//         }
//         status.setValue("Stopping");
//     }
// }
