package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Location.Start;

@Autonomous(name="Autonomous Blue Warehouse", group="main")
public class TeamBlueWarehouse extends AutonomousOpmode {
    @Override
    public void init() {
        super.setTeam(Start.TeamColor.BLUE);
        super.setStart(Start.StartLocation.WAREHOUSE);
        super.init();
    }
}
