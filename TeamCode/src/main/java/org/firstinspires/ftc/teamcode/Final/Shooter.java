package org.firstinspires.ftc.teamcode.Final;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Odometry_Sample.OdometryGlobalCoordinatePosition;



//Script for setting shooter power
public class Shooter {
    private PushBot motors = null;
    private RingPlacer ringPlacer = null;
    private Intake intake = null;
    private Telemetry.Item telemetry;
    private OdometryGlobalCoordinatePosition globalPositionUpdate = null;
    private double modesHighGoal[] = {0, 1950, 2300};
    private double modesPowerTarget[] = {0, 1500, 1700};
    private String state = "stop";
    private double previousSpeed = 0;
    
    public double[] shootPosition = {-1800,31500,0};
    
    
    public void init(PushBot motorsInit, RingPlacer ringPlacerInit, Intake intakeInit, Telemetry.Item telemetryInit, OdometryGlobalCoordinatePosition globalPositionUpdateInit) {
        motors = motorsInit;
        ringPlacer = ringPlacerInit;
        intake = intakeInit;
        telemetry = telemetryInit;
        globalPositionUpdate = globalPositionUpdateInit;
    }
   
    
    public boolean setPower(double power) {
        motors.shooter.setPower(power);
        return true;
    }
    
    public boolean setVelocity(double velocity){
        motors.shooter.setVelocity(velocity);
        telemetry.setValue(motors.shooter.getVelocity());
        if (Math.abs(motors.shooter.getVelocity()-velocity) < 15){
            return true;
        } else {
            return false;
        }
    }
    
    public boolean setModeHighGoal(int mode){
        double velocity = this.modesHighGoal[mode];
        return this.setVelocity(velocity);
    }
    
    public boolean setModePowerTarget(int mode){
        double velocity = this.modesPowerTarget[mode];
        return this.setVelocity(velocity);
    }
    
    // public void changeShootingPower(double change) {
    //     modesHighGoal[1] += change;
    // }
    
    // public boolean shootNew2ProMaxMiniSEFoundersEditionUltra() {
    //     intake.setMode(1);
    //     ringPlacer.setMode(1);
    //     setMode(1);
    //     return true;
    // }
    
    public boolean shoot(boolean continuous, boolean highGoal){
        // update shooting position
        shootPosition[0] = globalPositionUpdate.returnXCoordinate();
        shootPosition[1] = globalPositionUpdate.returnYCoordinate();
        shootPosition[2] = globalPositionUpdate.returnOrientation();
        
        if (highGoal){
            setModeHighGoal(2);
        } else {
            setModePowerTarget(2);
        }
        
        double speed = getVelocity();
        
        // if turning is to slow
        if (speed - previousSpeed < -80) {
            if (state == "shooting") {
                state = "stop";
                if (!continuous){
                    intake.setMode(0);
                    ringPlacer.setMode(0);
                    setModeHighGoal(0);
                }
                return true;
            } else {
                state = "starting";
            }
        }
        previousSpeed = speed;
        
        // good speed
        if ((modesHighGoal[1] < speed && highGoal) || (modesPowerTarget[1] < speed && !highGoal)){
            // shoot
            state = "shooting";
            ringPlacer.setMode(2);
            intake.setMode(1);
        } else {
            ringPlacer.setMode(0);
            intake.setMode(0);
        }
        
        return false;
    }
    
    public double getPower(){
        return motors.shooter.getPower();
    }
    
    public double getVelocity() {
        return motors.shooter.getVelocity();
    }
    
    public String getDisplay(){
        int displayRingPlacerPower = (int) Math.round(motors.ringPlacer.getPower()*10);
        int displayIntakePower = (int) Math.round(motors.intake.getPower() *10);
        String offset = "   ";
        String text = "\n"
        +offset+"        ／／"+ getVelocity() + " State: " + state + " Power: "+motors.shooter.getPower()
        +"\n"+offset+"    ／／" + displayRingPlacerPower/10
        +"\n"+offset+"／／" + displayIntakePower/10;
        return text;
    }
}






