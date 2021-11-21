package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Final Autonomous red warehouse 3.11", group="main")
public class TeamRed1 extends AutonomousV3{
    @Override
    public void init() {
        super.setTeam("red");
        super.setStart("warehouse");
        super.init();
    }
}
