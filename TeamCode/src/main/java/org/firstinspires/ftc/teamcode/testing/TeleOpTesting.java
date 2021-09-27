// // =========================================== //
// // ==================Testing================== //
// // =========================================== //

// package org.firstinspires.ftc.teamcode.testing;

// import com.qualcomm.robotcore.eventloop.opmode.Disabled;
// import org.firstinspires.ftc.robotcore.external.Telemetry;
// import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
// import com.qualcomm.robotcore.hardware.HardwareMap;
// import com.qualcomm.robotcore.util.Hardware;
// import com.qualcomm.robotcore.eventloop.opmode.OpMode;
// import com.qualcomm.robotcore.hardware.DcMotor;
// import com.qualcomm.robotcore.util.ElapsedTime;
// import com.qualcomm.robotcore.util.Range;
// import org.firstinspires.ftc.teamcode.Final.PushBot;
// import org.firstinspires.ftc.robotcore.external.Telemetry.Item;
// import com.qualcomm.robotcore.util.ElapsedTime;


// // Comment out here if something is replaced with a test version
// import org.firstinspires.ftc.teamcode.Final.Drivetrain;
// import org.firstinspires.ftc.teamcode.Final.Shooter;
// import org.firstinspires.ftc.teamcode.Final.Controller;
// import org.firstinspires.ftc.teamcode.Final.PushBot;

// @TeleOp(name="Testing OpMode 21w10a", group="Iterative Opmode")

// public class TeleOpTesting extends OpMode {
    
//     //get objects
//     PushBot motors = new PushBot();
//     Shooter shooter = new Shooter();
//     Controller controller = new Controller();
//     Drivetrain drivetrain = new Drivetrain();
    
//     // telemetry
//     Telemetry.Item status = null;
    
//     @Override
//     public void init() {
        
//         //add telemetry
//         telemetry.setAutoClear(false);
//         status = telemetry.addData("Status","%12.3f", 0.0);
//         Telemetry.Item telemetryDrivetrain = telemetry.addData("Robot" , "%12.3f", 0.0);

//         //Initialize objects
//         motors.init(hardwareMap);
//         shooter.init(motors);
//         drivetrain.init(motors, telemetryDrivetrain);
//         controller.init(drivetrain);
        
//         status.setValue("Initialized");
//     }

    
//     @Override
//     public void init_loop() {
//         status.setValue("Init looping");
//     }

    
//     @Override
//     public void start() {
//         status.setValue("Starting");
//         status.setValue("Started");
//     }

//     @Override
//     public void loop() {
//         status.setValue("Init looping");
//         controller.check(gamepad1, gamepad2);
        
//     }

//     @Override
//     public void stop() {
//         status.setValue("Stopping");
        
//     }

// }
