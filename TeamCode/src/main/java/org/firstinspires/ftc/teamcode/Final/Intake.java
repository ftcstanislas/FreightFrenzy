package org.firstinspires.ftc.teamcode.Final;

//Script for setting shooter power
public class Intake {
    private PushBot motors = null;
    public double modes[] = {0,1,-1};
    
    public void init(PushBot motorsInit) {
        motors = motorsInit;
    }
    public boolean setMode(int mode) {
        return this.setPower(modes[mode]);
    }
    
    public boolean setPower(double power) {
        motors.intake.setPower(power);
        if (Math.abs(power - motors.intake.getPower()) <= 0.05) {
            return true;
        }
        return false;
    }
    
    public double getPower(){
        return motors.intake.getPower();
    }
}