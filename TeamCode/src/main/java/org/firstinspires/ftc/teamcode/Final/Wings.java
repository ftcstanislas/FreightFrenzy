package org.firstinspires.ftc.teamcode.Final;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Wings {
    
    public PushBot motors = null;
    
    public double[] modes = {0.9,0.5};
    
    public void init(PushBot motorsInit) {
        motors = motorsInit;
        setMode(0);
    }
    
    public boolean setPos(double pos) {
        motors.wing.setPosition(pos);
        return true;
    }
    
    public double getPos() {
        return motors.wing.getPosition();
    }
    
    public boolean setMode(int mode) {
        double position = modes[mode];
        return setPos(position);
    }
    
    public void switchWing() {
        if (getPos() < (modes[0]+modes[1])/2){
            setMode(0);
        } else {
            setMode(1);
        }
    }

}