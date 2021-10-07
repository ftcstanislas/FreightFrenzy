package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.io.File;
import com.qualcomm.robotcore.util.ReadWriteFile;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ReadWriteFile;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.RobotParts.Arm;
import org.firstinspires.ftc.teamcode.RobotParts.Intake;
import org.firstinspires.ftc.teamcode.RobotParts.Spinner;
import org.firstinspires.ftc.teamcode.RobotParts.TankDrive;
import org.firstinspires.ftc.teamcode.testing.IMU_test;
import org.firstinspires.ftc.teamcode.Sensors.Camera;

@TeleOp(name="Final OpMode 3.6", group="Iterative Opmode")

public class TeleOpV3 extends OpMode {
    
    //get objects
    TankDrive drivetrain = new TankDrive();
    Arm arm = new Arm();
    Intake intake = new Intake();
    Spinner spinner = new Spinner();
    IMU_test imu = new IMU_test();
    Camera camera = new Camera(); // Dit afmaken later
    
    // telemetry
    Telemetry.Item status = null;
    Telemetry.Item telemetryDrivetrain = null;
    Telemetry.Item telemetryArm = null;
    Telemetry.Item telemetryIntake = null;
    Telemetry.Item telemetrySpinner = null;
    Telemetry.Item telemetryIMU = null;

    // make runtime
    ElapsedTime runtime = new ElapsedTime();
    
    // shit
    double previousPos = 0;
    double countPos = 0;
    
    @Override
    public void init() {
        
        
        //add telemetry
        telemetry.setAutoClear(false);
        String startInfo = "";
        status = telemetry.addData("Status", "X");
        telemetryDrivetrain = telemetry.addData("Robot", "X");
        telemetryArm = telemetry.addData("Arm", "X");
        telemetryIntake = telemetry.addData("Intake", "X");
        telemetrySpinner = telemetry.addData("Spinner", "X");
        telemetryIMU = telemetry.addData("IMU angle", "X");

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
        drivetrain.init(hardwareMap, telemetryDrivetrain, runtime);
        //drivetrain.setBrake(true);
        arm.init(hardwareMap, telemetryArm);
        intake.init(hardwareMap, telemetryIntake);
        spinner.init(hardwareMap, telemetrySpinner);
        imu.init(hardwareMap, telemetryIMU);
        
        status.setValue("Initialized");
    }
    
    @Override
    public void init_loop() {
        status.setValue("Init looping");
    }

    
    @Override
    public void start() {
        status.setValue("Starting");
        status.setValue("Started");
    }

    @Override
    public void loop() {
        // telemetry
        status.setValue("Looping for " + runtime.toString());
        // telemetryDrivetrain.setValue(drivetrain.getDisplay());
        
        // status.setValue("\nX:"+globalPositionUpdate.returnXCoordinate()+"\nY:"+
        // globalPositionUpdate.returnYCoordinate()+"\n O:"+globalPositionUpdate.returnOrientation());
        
        drivetrain.checkController(gamepad1, gamepad2);
        arm.checkController(gamepad1, gamepad2);
        intake.checkController(gamepad1, gamepad2);
        spinner.checkController(gamepad1, gamepad2);
        imu.getRotation();

        // telemetry.update();
    }

    @Override
    public void stop() {
        status.setValue("Stopping");
    }

}