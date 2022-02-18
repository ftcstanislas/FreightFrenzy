package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Final Autonomous blue warehouse 3.11", group="main")
public class TeamBlueWarehouse extends AutonomousOpmode {
    @Override
    public void init() {
        super.setTeam("blue");
        super.setStart("warehouse");
        super.init();
    }
}
