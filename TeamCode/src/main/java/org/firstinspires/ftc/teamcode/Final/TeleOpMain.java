package org.firstinspires.ftc.teamcode.Final;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import java.io.File;
import com.qualcomm.robotcore.util.ReadWriteFile;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.Odometry_Sample.OdometryGlobalCoordinatePosition;
import com.qualcomm.robotcore.util.ReadWriteFile;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;


@TeleOp(name="Final OpMode 2.0", group="Iterative Opmode")

public class TeleOpMain extends OpMode {
    
    //get objects
    PushBot motors = new PushBot();
    Shooter shooter = new Shooter();
    Controller controller = new Controller();
    Drivetrain drivetrain = new Drivetrain();
    RingPlacer ringPlacer = new RingPlacer();
    Arm arm = new Arm();
    DriveTo driveTo = new DriveTo();
    Intake intake = new Intake();
    IntakeLock intakeLock = new IntakeLock();
    Wings wing = new Wings();
    
    // telemetry
    Telemetry.Item status = null;
    Telemetry.Item telemetryDrivetrain = null;
    Telemetry.Item telemetryArm = null;
    Telemetry.Item telemetryShooting = null;
    Telemetry.Item telemetryLocation = null;
    Telemetry.Item telemetryIntakeLock = null;
    Telemetry.Item telemetryInformation = null;
    Telemetry.Item telemetryTesten = null;
    
    // make runtime
    ElapsedTime runtime = new ElapsedTime();
    
    // position
    OdometryGlobalCoordinatePosition globalPositionUpdate;
    final double COUNTS_PER_INCH = 360;
    Thread positionThread;
    
    // shit
    double previousPos = 0;
    double countPos = 0;
    
    @Override
    public void init() {
        
        
        //add telemetry
        telemetry.setAutoClear(false);
        String startInfo = "";
        status = telemetry.addData("Status", "X");
        telemetryLocation = telemetry.addData(startInfo+"location", "X");
        telemetryDrivetrain = telemetry.addData("Robot", "X");
        telemetryArm = telemetry.addData("Arm", "X");
        telemetryShooting = telemetry.addData("Shooting","X");
        telemetryIntakeLock = telemetry.addData("IntakeLock","X");
        telemetryInformation = telemetry.addData(startInfo+"Informaton", "X");
        telemetryTesten = telemetry.addData("Testen", "X");
        
        // initialize part 1
        motors.init(hardwareMap);
        
        // position
        globalPositionUpdate = new OdometryGlobalCoordinatePosition(motors.leftBackDrive, motors.rightBackDrive, motors.rightFrontDrive, COUNTS_PER_INCH, 10);
        motors.resetEncoders();
        File xFile = AppUtil.getInstance().getSettingsFile("positionX.txt");
        File yFile = AppUtil.getInstance().getSettingsFile("positionY.txt");
        File OrientationFile = AppUtil.getInstance().getSettingsFile("positionOrientation.txt");
        double x = Double.parseDouble(ReadWriteFile.readFile(xFile).trim());
        double y = Double.parseDouble(ReadWriteFile.readFile(yFile).trim());
        double o = Double.parseDouble(ReadWriteFile.readFile(OrientationFile).trim());
        globalPositionUpdate.setPosition(x,y,o);
        positionThread = new Thread(globalPositionUpdate);
        positionThread.start();
        // globalPositionUpdate.reverseNormalEncoder();
        // globalPositionUpdate.reverseRightEncoder();
        // globalPositionUpdate.reverseLeftEncoder();
        
        //Initialize objects part 2
        ringPlacer.init(motors);
        shooter.init(motors, ringPlacer, intake, telemetryTesten, globalPositionUpdate);
        drivetrain.init(motors, telemetryDrivetrain, runtime);
        controller.init(gamepad1, gamepad2, drivetrain, shooter, ringPlacer, arm, intake, intakeLock, driveTo, wing);
        arm.init(motors);
        intake.init(motors);
        intakeLock.init(motors);
        driveTo.init(motors, drivetrain, telemetryInformation, globalPositionUpdate);
        wing.init(motors);
        
        // open gripper so it wont get stuck
        arm.setGripperMode(0);
        motors.intakeLock.setPosition(0);
        
        status.setValue("Initialized");
    }
    
    @Override
    public void init_loop() {
        status.setValue("Init looping");
    }

    
    @Override
    public void start() {
        status.setValue("Starting");
        motors.resetEncoders();
        status.setValue("Started");
    }

    @Override
    public void loop() {
        // telemetry
        status.setValue("Looping for " + runtime.toString());
        telemetryLocation.setValue(globalPositionUpdate.getDisplay());
        telemetryDrivetrain.setValue(drivetrain.getDisplay());
        telemetryArm.setValue(arm.getDisplay());
        telemetryShooting.setValue(shooter.getDisplay());
        telemetryIntakeLock.setValue(intakeLock.getDisplay());
        
        controller.check();
        
        // WHAT IS THIS
        // if (Math.abs(previousPos - arm.getArmCurrentPos()) < 1) {
        //     countPos++;
        //     if (countPos == 10) {
        //         motors.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //         status.setValue("doen");
        //         arm.setGripperPos(1);
        //     }
        // } else {
        //     previousPos = arm.getArmCurrentPos();
        // }
        
        // status.setValue("\nX:"+globalPositionUpdate.returnXCoordinate()+"\nY:"+
        // globalPositionUpdate.returnYCoordinate()+"\n O:"+globalPositionUpdate.returnOrientation());
        
        
        // telemetry.update();
    }

    @Override
    public void stop() {
        status.setValue("Stopping");
        
    }

}
