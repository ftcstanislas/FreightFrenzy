package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Location.Start;

@TeleOp(name = "Test Tele-op", group = "main")
public class TestOpmode extends AutonomousOpmode {
    @Override
    public void init() {
        super.setTeam(Start.TeamColor.RED);
        super.setStart(Start.StartLocation.WAREHOUSE);
        super.init();
    }

    @Override
    public void loop() {
        globalUpdate();
        super.loop();
    }
}
