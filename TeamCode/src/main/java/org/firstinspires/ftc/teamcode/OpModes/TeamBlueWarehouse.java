package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Autonomous Blue Warehouse", group="main")
public class TeamBlueWarehouse extends AutonomousOpmode {
    @Override
    public void init() {
        super.setTeam("blue");
        super.setStart("warehouse");
        super.init();
    }
}
