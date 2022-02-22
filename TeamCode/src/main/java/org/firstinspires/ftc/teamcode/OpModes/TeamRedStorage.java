package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Autonomous Red Spinner", group="main")
public class TeamRedStorage extends AutonomousOpmode {
    @Override
    public void init() {
        super.setTeam("red");
        super.setStart("storage");
        super.init();
    }
}
