package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Kirby OpMode", group="Iterative Opmode")
// @Disabled
public class KirbyOpMode extends OpMode {
	// Declare OpMode members.
	private ElapsedTime runtime = new ElapsedTime();
	private DcMotor leftBack = null;
	private DcMotor rightBack = null;
	private DcMotor leftFront = null;
	private DcMotor rightFront = null;
	
	private DcMotor armMotor = null;
	// private Servo armServo = null;
	
	private CRServo carouselMotor = null;
	
	private DcMotor intakeMotor = null;
	
	/*
	 * Code to run ONCE when the driver hits INIT
	 */
	@Override
	public void init() {
		telemetry.addData("Status", "Initialized");

		// Initialize the hardware variables. Note that the strings used here as parameters
		// to 'get' must correspond to the names assigned during the robot configuration
		// step (using the FTC Robot Controller app on the phone).
		leftBack  = hardwareMap.get(DcMotor.class, "left_back");
		rightBack = hardwareMap.get(DcMotor.class, "right_back");
		leftFront  = hardwareMap.get(DcMotor.class, "left_front");
		rightFront = hardwareMap.get(DcMotor.class, "right_front");
		
		armMotor = hardwareMap.get(DcMotor.class, "arm");
		// armServo = hardwareMap.get(Servo.class, "servo");
		
		carouselMotor = hardwareMap.get(CRServo.class, "carousel");
		
		intakeMotor = hardwareMap.get(DcMotor.class, "intake_motor");

		// Most robots need the motor on one side to be reversed to drive forward
		// Reverse the motor that runs backwards when connected directly to the battery
		leftBack.setDirection(DcMotor.Direction.FORWARD);
		rightBack.setDirection(DcMotor.Direction.REVERSE);
		leftFront.setDirection(DcMotor.Direction.FORWARD);
		rightFront.setDirection(DcMotor.Direction.REVERSE);
		
		leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		
		armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		
		
		// armServo.setMode(Servo.RunMode);
		

		// Tell the driver that initialization is complete.
		telemetry.addData("Status", "Initialized");
	}

	/*
	 * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
	 */
	@Override
	public void init_loop() {
	}

	/*
	 * Code to run ONCE when the driver hits PLAY
	 */
	@Override
	public void start() {
		runtime.reset();
	}

	/*
	 * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
	 */
	@Override
	public void loop() {
		
		double armPosition = armMotor.getCurrentPosition();
		
		double x = gamepad1.left_stick_x;
		double y = -gamepad1.left_stick_y;
		double rightX = gamepad1.right_stick_x;
		double r = Math.hypot(x, y);
		double robotAngle = Math.atan2(y, x) - Math.PI / 4;
		
		double armBtn = gamepad2.left_stick_y;
		
		boolean containerToggle = gamepad1.y;
		
		boolean carouselBtn = gamepad2.a;
		double reverseBtn = gamepad2.left_trigger;
		
		boolean intakeBtn = gamepad2.b;
		
		double slow = 1 - gamepad2.left_trigger;
		
		//calculate power wheels
		double powerLeftFront = (r * Math.cos(robotAngle) + rightX);
		double powerRightFront = -(r * Math.sin(robotAngle) - rightX);
		double powerLeftBack = (r * Math.sin(robotAngle) + rightX);
		double powerRightBack = (r * Math.cos(robotAngle) - rightX);
		
		// double powerLeftFront = x + rightX;
		// double powerRightFront = y - rightX;
		// double powerLeftBack = y + rightX;
		// double powerRightBack = x - rightX;

		// Send calculated power to wheels
		leftFront.setPower(powerLeftFront);
		rightFront.setPower(powerRightFront);
		leftBack.setPower(powerLeftBack);
		rightBack.setPower(powerRightBack);
		
		if (armBtn < -0.3) {
			armMotor.setTargetPosition(-1);
			armMotor.setPower(-0.4);
			
		} else if (armBtn > 0.3) {
			armMotor.setTargetPosition(0);
			armMotor.setPower(0.3);
			
		} else {
			if (armPosition > -440) {
				if (armPosition > -100) {
					armMotor.setPower(0);
				} else {
					armMotor.setPower(-0.1);
				}
			} else if (armPosition < -440 && armPosition > -500) {
				armMotor.setPower(0);
			} else {
				armMotor.setPower(0.1);
			}
		}
		
		if (carouselBtn) {
			if (reverseBtn == 0) {
				carouselMotor.setPower(1);
			} else {
				carouselMotor.setPower(-1);
			}
		} else {
			carouselMotor.setPower(0);
		}
		
		if (intakeBtn) {
			if (reverseBtn == 0) {
				intakeMotor.setPower(-0.7);
			} else {
				intakeMotor.setPower(0.7);
			}
		} else {
			if (java.lang.Math.abs(intakeMotor.getCurrentPosition() % 144) > 10) {
				intakeMotor.setPower(-0.1);
			}
			else {
				intakeMotor.setPower(0);
			}
		}

		// Show the elapsed game time and wheel power.
		telemetry.addData("Status", "Run Time: " + runtime.toString()+"\ndrive angle"+robotAngle);
		telemetry.addData("Arm position", armPosition);
		telemetry.addData("Arm Button", armBtn);
		telemetry.addData("Intake turning", intakeMotor.getPower());
		telemetry.addData("Carousel turning", carouselMotor.getPower());
		telemetry.addData("Intake Position", intakeMotor.getCurrentPosition());
		telemetry.addData("Intake Correct Position", (java.lang.Math.abs(intakeMotor.getCurrentPosition() % 144) < 10));
		
		// telemetry.addData("Motors", "speed (%.2f), turn (%.2f)", speed, turn);
	}

	/*
	 * Code to run ONCE after the driver hits STOP
	 */
	@Override
	public void stop() {
	}

}
