package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Location.Start;

@Autonomous(name="Autonomous Red Warehouse", group="main")
public class TeamRedWarehouse extends AutonomousOpmode {
    @Override
    public void init() {
        super.setTeam(Start.TeamColor.RED);
        super.setStart(Start.StartLocation.WAREHOUSE);
        super.init();
    }
}
