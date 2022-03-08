package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Location.Start;

@Autonomous(name="Autonomous Blue Spinner", group="main")
public class TeamBlueSpinner extends AutonomousOpmode {
    @Override
    public void init() {
        super.setTeam(Start.TeamColor.BLUE);
        super.setStart(Start.StartLocation.SPINNER);
        super.init();
    }
}
