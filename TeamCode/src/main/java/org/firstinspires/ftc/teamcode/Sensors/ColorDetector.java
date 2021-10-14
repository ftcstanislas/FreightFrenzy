package org.firstinspires.ftc.teamcode.Sensors;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Map;

// It's called ColorDetector to avoid the code from confusing
// this with the ColorSensor class from the FTC SDK
public class ColorDetector {

    private ColorSensor colorSensor = null;
    private double errorMargin = 100;
    public Map<String, Integer> sensorInput = new HashMap<String, Integer>();
    protected Telemetry.Item telemetry = null;

    public void init(HardwareMap map, Telemetry.Item telemetryInit) {
        telemetry = telemetryInit;
        colorSensor = map.get(ColorSensor.class, "color_sensor");
    }

    public void update() {
        sensorInput.put("red", colorSensor.red());
        sensorInput.put("green", colorSensor.blue());
        sensorInput.put("blue", colorSensor.blue());
        sensorInput.put("argb", colorSensor.argb());
        sensorInput.put("alpha", colorSensor.alpha());
        telemetry.setValue("\n" +
                "red: " + sensorInput.get("red") + "\n" +
                "green: " + sensorInput.get("green") + "\n" +
                "blue: " + sensorInput.get("blue") + "\n" +
                "argb: " + sensorInput.get("argb") + "\n"
        );
    }
}

//        double gamepadAngle = Math.atan(gamepad1.right_stick_y / gamepad1.right_stick_x);
//        if (gamepad1.right_stick_x < 0) gamepadAngle *= -1;
//        double turnAngle = location.getRotation() - gamepadAngle;
//        double turning = turnAngle / 10;
