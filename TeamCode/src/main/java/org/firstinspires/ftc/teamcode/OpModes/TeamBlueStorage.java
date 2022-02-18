package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Final Autonomous blue spinner 3.11", group="main")
public class TeamBlueStorage extends AutonomousOpmode {
    @Override
    public void init() {
        super.setTeam("blue");
        super.setStart("storage");
        super.init();
    }
}
