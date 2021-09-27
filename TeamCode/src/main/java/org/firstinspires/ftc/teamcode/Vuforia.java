
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;



/*
@TeleOp(name="Main opmode", group="Linear Opmode")

public class Drivetrain extends LinearOpMode {
    
    //initialising hardware variables
    //drive motors
    public DcMotor  leftDrive   = null;
    public DcMotor  rightDrive  = null;
    //intake motors
    public DcMotor leftInHex = null;
    public DcMotor rightInHex = null;
    //public DcMotor rightInHex = null;
    //arm motor
    //public DcMotor armHex = null;
    //arm servos
    //public Servo armBase = null;
    //building platform movement servo
    public Servo buildMover1 = null;
    public Servo buildMover2 = null;
    //block grab servos
    //public Servo blockGrab1 = null;
    //public Servo blockGrab2 = null;
    //intake servo
    //public CRServo intakeServo;
    public Servo HookServo = null;
    
    
    //basic servo variables
    //armbBase servo variables
    public final static double ARMBASE_HOME = 0.0; //servo ref point
    public final static double ARMBASE_MIN_RANGE = 0.8; //servo minimum range 
    public final static double ARMBASE_MAX_RANGE = 0.0; //servo max range 
    public final double ARM_SPEED = 0.01; //movement speed
    public double armBasePosition = ARMBASE_HOME; //main positioning
    
    //buildMover servo variables
    public final static double BUILDMOVER_HOME = 0; //ref point
    public final static double BUILDMOVER_MIN_RANGE = 0; //min pos
    public final static double BUILDMOVER_MAX_RANGE = 1.0; //max pos
    
    //blockGrab servo variables
    public static final double BLOCKGRAB_HOME = 0; //ref point
    public static final double BLOCKGRAB_MIN_RANGE = 0; //min pos
    public static final double BLOCKGRAB_MAX_RANGE = 5.0; //max pos
    
    
    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        //grab hardware from controller
        // Define and Initialize Motors
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        leftInHex  = hardwareMap.get(DcMotor.class, "intake_left");
        rightInHex = hardwareMap.get(DcMotor.class, "intake_right");
        //rightInHex = hardwareMap.get(DcMotor.class, "right_intake");
        leftDrive.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightDrive.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        leftInHex.setDirection(DcMotor.Direction.FORWARD);
        rightInHex.setDirection(DcMotor.Direction.FORWARD);
        //armHex.setDirection(DcMotor.Direction.FORWARD);
        
        HookServo  = hardwareMap.servo.get("AutoBlockMove");

        // Set all motors to zero power
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        leftInHex.setPower(0);
        rightInHex.setPower(0);
        //rightInHex.setPower(0);

        // init servos
        // intaker servoer init
        //intakeServo = hardwareMap.get(CRServo.class, "intaker_servoer");
        
        //armbase servo init
        //armBase = hardwareMap.servo.get("armBaseServo");
        //armBase.setPosition(ARMBASE_MAX_RANGE);
        // Build mover servo init
        buildMover1 = hardwareMap.servo.get("buildMover1");
        buildMover1.setPosition(BUILDMOVER_HOME);
        buildMover2 = hardwareMap.servo.get("buildMover2");
        buildMover2.setPosition(BUILDMOVER_MAX_RANGE);
        // Grab servo init 
        /*blockGrab1 = hardwareMap.servo.get("blockGrab1");
        blockGrab1.setPosition(BLOCKGRAB_MAX_RANGE);
        blockGrab2 = hardwareMap.servo.get("blockGrab2");
        blockGrab2.setPosition(BLOCKGRAB_MIN_RANGE);
        //grab servo direction
        blockGrab1.setDirection(Servo.Direction.REVERSE);
        blockGrab2.setDirection(Servo.Direction.FORWARD);
        */
        /*
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            
            //armHex.setPower(0);
            
            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;

            // - This uses basic math to combine motions and is easier to drive straight.
            double turn =  gamepad1.right_stick_x;
            double drive=  -gamepad1.left_stick_y;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            // Send calculated power to wheels
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Drive motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
            
            
            
            // Code for intake motors
            double leftIntake;
            double rightIntake;
            //double rightIntake2;
            // Calc how fast the motors run and what direction
            double Out= gamepad2.left_trigger;
            double In = gamepad2.right_trigger;
            leftIntake = Range.clip(Out - In, -1.0, 1.0);
            rightIntake = Range.clip(Out - In, -1.0, 1.0);
            // Execute
            leftInHex.setPower(-leftIntake);
            //rightIntake2 = rightIntake *2;
            rightInHex.setPower(-rightIntake);
            //rightInHex.setPower(rightIntake);
            // Telemetry data
            telemetry.addData("Intake ", "(%.2f)", leftIntake);
            
            
            
            
            // armbasecode
            
            armHex.setPower(0);
            double armMov;
            double UpDown = gamepad2.left_stick_y;
            armMov = Range.clip(UpDown, -1.0, 1.0);
            armHex.setPower(armMov);
            
            
            // Servo code buildMover
            
            if (gamepad1.a) {
                buildMover1.setPosition(BUILDMOVER_MIN_RANGE);
                buildMover2.setPosition(BUILDMOVER_MAX_RANGE);
            }else if (gamepad1.b) {
                buildMover1.setPosition(BUILDMOVER_MAX_RANGE);
                buildMover2.setPosition(BUILDMOVER_MIN_RANGE);
            }
            
            if (gamepad2.y) {
                
            }else if (gamepad2.x) {
                
            }
            
        }
    }
    
}
*/
