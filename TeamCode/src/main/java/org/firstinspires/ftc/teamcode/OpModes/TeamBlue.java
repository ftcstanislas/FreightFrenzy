package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Location.Start;

@TeleOp(name = "TeleOp Blue", group = "main")
public class TeamBlue extends TeleOpOpmode {
    @Override
    public void init() {
        super.setTeam(Start.TeamColor.BLUE);
        super.init();
    }
}