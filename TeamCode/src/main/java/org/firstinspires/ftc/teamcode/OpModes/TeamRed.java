package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Location.Start;

@TeleOp(name = "TeleOp Red", group = "main")
public class TeamRed extends TeleOpOpmode {
    @Override
    public void init() {
        super.setTeam(Start.TeamColor.RED);
        super.init();
    }
}