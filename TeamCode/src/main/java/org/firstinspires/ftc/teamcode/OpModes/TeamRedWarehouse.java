package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Autonomous Red Warehouse", group="main")
public class TeamRedWarehouse extends AutonomousOpmode {
    @Override
    public void init() {
        super.setTeam("red");
        super.setStart("warehouse");
        super.init();
    }
}
