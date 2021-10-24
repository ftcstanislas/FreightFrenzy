

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Wave the arm", group="presentation")

public class WaveV2 extends OpMode{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor arm = null;
    private Servo gripper = null;
    private String mode = "right";
    private int[] positions = {350,600};

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        arm = hardwareMap.get(DcMotor.class, "arm");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setDirection(DcMotor.Direction.FORWARD);
        
        gripper = hardwareMap.get(Servo.class, "gripper");
        gripper.setDirection(Servo.Direction.FORWARD);
        

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setTargetPosition(100);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.3);
        gripper.setPosition(1);
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Mode", mode);
        telemetry.addData("Position", arm.getCurrentPosition());
        telemetry.addData("Go to Position", arm.getTargetPosition());
        wave();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        arm.setTargetPosition(0);
        arm.setPower(0.2);
    }
    
    public void wave(){
        
        if (arm.getCurrentPosition() > positions[1]-20 && mode == "left"){
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            arm.setTargetPosition(positions[0]);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.setPower(0.2);
            gripper.setPosition(1);
            mode = "right";
        }
        if (arm.getCurrentPosition() < positions[0]+20 && mode == "right"){
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            arm.setTargetPosition(positions[1]);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.setPower(0.2);
            gripper.setPosition(0);
            mode = "left";
        }
        
    }

}
