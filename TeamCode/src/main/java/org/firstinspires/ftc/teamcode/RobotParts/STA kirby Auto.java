/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name="Kirby Auto", group="Linear Opmode")

public class KirbyAuto extends LinearOpMode {

	// Declare OpMode members.
	private ElapsedTime runtime = new ElapsedTime();
	private DcMotor leftBack = null;
	private DcMotor rightBack = null;
	private DcMotor leftFront = null;
	private DcMotor rightFront = null;
	
	private DcMotor armMotor = null;
	
	private DcMotor carouselMotor = null;
	
	private DcMotor intakeMotor = null;
	
	private int tprLF = 390;
	private int tprLB = 560;
	private int tprRF = 1120;
	private int tprRB = 580;

	@Override
	public void runOpMode() {
		telemetry.addData("Status", "Initialized");
		telemetry.update();

		// Initialize the hardware variables. Note that the strings used here as parameters
		// to 'get' must correspond to the names assigned during the robot configuration
		// step (using the FTC Robot Controller app on the phone).
		leftBack  = hardwareMap.get(DcMotor.class, "left_back");
		rightBack = hardwareMap.get(DcMotor.class, "right_back");
		leftFront  = hardwareMap.get(DcMotor.class, "left_front");
		rightFront = hardwareMap.get(DcMotor.class, "right_front");
		
		armMotor = hardwareMap.get(DcMotor.class, "arm");
		
		carouselMotor = hardwareMap.get(DcMotor.class, "carousel");

		intakeMotor = hardwareMap.get(DcMotor.class, "intake_motor");

		// Most robots need the motor on one side to be reversed to drive forward
		// Reverse the motor that runs backwards when connected directly to the battery
		leftBack.setDirection(DcMotor.Direction.FORWARD);
		rightBack.setDirection(DcMotor.Direction.REVERSE);
		leftFront.setDirection(DcMotor.Direction.FORWARD);
		rightFront.setDirection(DcMotor.Direction.REVERSE);
		
		leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		
		leftFront.setTargetPosition(-tprLF * 5);
		leftBack.setTargetPosition(-tprLB * 5);
		rightFront.setTargetPosition(-tprRF * 5);
		rightBack.setTargetPosition(-tprRB * 5);
		
		leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		// Wait for the game to start (driver presses PLAY)
		waitForStart();
		runtime.reset();
		
		leftFront.setPower(-0.5);
		rightFront.setPower(-1);
		leftBack.setPower(-0.5);
		rightBack.setPower(-0.5);
		
		sleep(10000);
		

		// Show the elapsed game time and wheel power.
		telemetry.addData("Status", "Run Time: " + runtime.toString());
		telemetry.update();
	}
}
