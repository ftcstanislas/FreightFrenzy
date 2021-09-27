package org.firstinspires.ftc.teamcode.Final;

import com.qualcomm.robotcore.hardware.DcMotor;

//Script for setting intakeLock power
public class IntakeLock {
    private PushBot motors = null;
    
    private double[] modes = {0,1};
    
    public void init(PushBot motorsInit) {
        motors = motorsInit;
        setMode(0);
    }
    
    public void switchIt(){
        if (getPos() > (modes[0]+modes[1])/2){
            setMode(0);
        } else {
            setMode(1);
        }
    }
    
    public boolean openLock() {
        motors.intakeLock.setPosition(0);
        return true;
    }
    
    public boolean setMode(int mode){
        double position = modes[mode];
        return setPos(position);
    }
    
    public boolean setPos(double position) {
        motors.intakeLock.setPosition(position);
        return true;
    }
    
    public double getPos() {
        return motors.intakeLock.getPosition();
    }
    
    public String getDisplay(){
        return "\n Γ "+getPos();
    }
}






