package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Final Autonomous blue warehouse 3.11", group="main")
public class TeamBlue1 extends AutonomousV3{
    @Override
    public void init() {
        super.setTeam("blue");
        super.setStart("warehouse");
        super.init();
    }
}
