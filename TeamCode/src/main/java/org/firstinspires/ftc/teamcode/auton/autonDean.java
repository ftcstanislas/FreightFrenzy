///*
// * Copyright (c) 2021 OpenFTC Team
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// * SOFTWARE.
// */
//
//package org.firstinspires.ftc.teamcode.auton;
//
//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.acmerobotics.roadrunner.geometry.Vector2d;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.Servo;
//
//import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
//import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
//
//@Autonomous(name = "Igor 1+1", group = "Concept")
//public class Igoroneplusone extends LinearOpMode {
//
//    private DcMotor armMotor1;
//    private DcMotor armMotor2;
//    private Servo intakeServo;
//
//    public void armMovement(int armTicks) throws InterruptedException {
//        double multiplier = 0;
//        if(armTicks >= 0){multiplier = 1.0;}
//        armMotor1.setTargetPosition(armTicks);
//        armMotor2.setTargetPosition(armTicks);
//        armMotor1.setPower(0.5 * multiplier);
//        armMotor2.setPower(0.5 * multiplier);
//        armMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        armMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        while (armMotor1.isBusy() && opModeIsActive()) {
//            idle();
//        }
//    }
//    public void servoPositioning(double servoPosition) {intakeServo.setPosition(servoPosition);}
//
//    @Override
//    public void runOpMode() throws InterruptedException{
//        armMotor1 = hardwareMap.get(DcMotor.class, "armmotor1");
//        armMotor2= hardwareMap.get(DcMotor.class, "armmotor2");
//
//        intakeServo = hardwareMap.get(Servo.class, "Intake");
//
//        armMotor1.setDirection(DcMotorSimple.Direction.FORWARD);
//        armMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
//
//        SampleMecanumDrive drivetrain = new SampleMecanumDrive(hardwareMap);
//
//        TrajectorySequence LeftPark = drivetrain.trajectorySequenceBuilder(new Pose2d(0, 0, Math.toRadians(0)))
//                .lineTo(new Vector2d(0, 20))
//                .lineTo(new Vector2d(20,20))
//                .build();
//
//        waitForStart();
//        drivetrain.followTrajectorySequence(LeftPark);
//    }
//}