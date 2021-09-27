package org.firstinspires.ftc.teamcode.Final;

import com.qualcomm.robotcore.hardware.DcMotor;


public class Arm {
    
    public PushBot motors = null; 
    private int offset = 0;
    private int[] armModes = {150, 450, 650}; //(200, 350, 560)
    private double[] gripperModes = {0, 1}; 
    
    public void init(PushBot motorsInit) {
        motors = motorsInit;
        
        // setArmMode(0);
        // setGripperMode(1);
    }
    
    public void setStartPos(int position){
        offset = position;
    }
    
    public void switchArm(){
        if (getArmTargetPos() > (armModes[0]+armModes[1])/2){
            setArmMode(0);
        } else {
            setArmMode(1);
        }
    }
    
    public boolean setArmMode(int mode){
        int position = armModes[mode];
        return setArmPos(position);
    }
    
    // public boolean armUp() {
    //     double position;
    //     if (Math.abs(650 - getArmPos()) < 50) {
    //         return true;
    //     } else {
    //         position = getArmPos() + 100;
    //         setArmPos((int) position);
    //         if (Math.abs(position - getArmPos()) < 50) {
    //             return true;
    //         } else {
    //             return false;
    //         }
    //     }
    // }
    
    // public boolean armDown() {
    //     double position;
    //     if (Math.abs(100 - getArmPos()) < 50) {
    //         return true;
    //     } else {
    //         position = getArmPos() - 100;
    //         setArmPos((int) position);
    //         if (Math.abs(position - getArmPos()) < 50) {
    //             return true;
    //         } else {
    //             return false;
    //         }
    //     }
    // }
    
    public boolean moveArm(double pos) {
        double position = getArmTargetPos();
        return setArmPos((int) Math.round(position + pos));
    }
    
    public boolean setArmPos(int position) {
        if (getArmTargetPos() != position){
            motors.arm.setTargetPosition(position - offset);
            motors.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motors.arm.setPower(0.4);
        }

        if (Math.abs(position - getArmPos()) < 50) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean setArmPower(double power) {
        motors.arm.setPower(power);
        return true;
    }
    
    public void switchGripper(){
        if (getGripperTargetPos() > (gripperModes[0]+gripperModes[1])/2){
            setGripperMode(0);
        } else {
            setGripperMode(1);
        }
    }
    
    public boolean setGripperMode(int mode){
        double position = gripperModes[mode];
        return setGripperPos(position);
    }
    
    public boolean setGripperPos(double pos) {
        motors.gripper.setPosition(pos);
        
        if (Math.abs(pos - getGripperPos()) < 50) {
            return true;
        } else {
            return false;
        }
    }
    
    public double getGripperPos() {
        return motors.gripper.getPosition();
        // return motors.gripper.getCurrentPosition();
    }
    
    public double getGripperTargetPos() {
        return motors.gripper.getPosition();
        // return motors.gripper.getTargetPosition();
    }
    
    public double getArmPos(){
        return motors.arm.getCurrentPosition() + offset;
    }
    
    public double getArmTargetPos(){
        return motors.arm.getTargetPosition() + offset;
    }
    
    public void resetEncoder() {
        motors.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    
    public String getDisplay(){
        int displayGripper = (int) Math.round(getGripperPos()*1000);
        String offset = "   ";
        String text = "\n"
        +offset+"||---"+ getGripperPos()
        +"\n"+offset+"||---\n"
        +offset+"||\n"
        +offset+Math.round(getArmPos())+"/"+Math.round(getArmTargetPos())+" with "+motors.arm.getPower();
        return text;
    }
}





