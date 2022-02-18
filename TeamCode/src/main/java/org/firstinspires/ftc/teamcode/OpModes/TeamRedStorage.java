package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Final Autonomous red spinner 3.11", group="main")
public class TeamRedStorage extends AutonomousOpmode {
    @Override
    public void init() {
        super.setTeam("red");
        super.setStart("storage");
        super.init();
    }
}
