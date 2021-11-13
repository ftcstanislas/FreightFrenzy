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
    public String object = "EMPTY";

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
//        Not final values!
        if (inputMatch(100,100,100,100,100)) {
            object = "BLOCK";
        } else if (inputMatch(5,5,5,5,5)) {
            object = "BALL";
        } else if (inputMatch(69,420,69,420,69420)) {
            object = "CUSTOM";
        } else {
            object = "EMPTY";
        }
        telemetry.setValue("\n" +
                "----- Object: " + object + " -----" + "\n" +
                "red: " + sensorInput.get("red") + "\n" +
                "green: " + sensorInput.get("green") + "\n" +
                "blue: " + sensorInput.get("blue") + "\n" +
                "argb: " + sensorInput.get("argb") + "\n" +
                "alpha: " + sensorInput.get("alpha")
        );
    }

    public boolean inputMatch(double red, double green, double blue, double argb,double alpha) {
        return sensorInput.get("red") < red + errorMargin && sensorInput.get("red") > red - errorMargin &&
                sensorInput.get("green") < green + errorMargin && sensorInput.get("green") > green - errorMargin &&
                sensorInput.get("blue") < blue + errorMargin && sensorInput.get("blue") > blue - errorMargin &&
                sensorInput.get("argb") < argb + errorMargin && sensorInput.get("argb") > argb - errorMargin &&
                sensorInput.get("alpha") < alpha + errorMargin && sensorInput.get("alpha") > alpha - errorMargin;
    }
}
