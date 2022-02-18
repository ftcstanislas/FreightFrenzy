package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Final Autonomous red warehouse 3.11", group="main")
public class TeamRedWarehouse extends AutonomousOpmode {
    @Override
    public void init() {
        super.setTeam("red");
        super.setStart("warehouse");
        super.init();
    }
}
