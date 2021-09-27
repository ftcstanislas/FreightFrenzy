package org.firstinspires.ftc.teamcode.Final;

//Script for setting ringplacer power
public class RingPlacer {
    
    //declare objects
    private PushBot motors = null;
    public double modes[] = {0,0.5,1};
    
    public void init(PushBot motorsInit) {
        motors = motorsInit;
    }
    
    public boolean setMode(int mode) {
        return this.setPower(modes[mode]);
    }
    
    public boolean setPower(double power) {
        motors.ringPlacer.setPower(power);
        if (Math.abs(power - motors.ringPlacer.getPower()) <= 0.05) {
            return true;
        }
        return false;
    }
}