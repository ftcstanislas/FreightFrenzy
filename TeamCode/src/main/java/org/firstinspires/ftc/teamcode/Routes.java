package org.firstinspires.ftc.teamcode;

import com.sun.tools.javac.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

// First item from route is start position


public class Routes {
    
    /*
    Routes:
    -----------------------------------------------------------------------------------------------
    WAIT UNTIL FINISHED   OBJECT                FUNCTIE             ARGUMENTEN (achter elkaar)
    -----------------------------------------------------------------------------------------------
    false / true          WAIT                  wait                seconds
    false / true          INTAKE                mode                stop / intaking
    false / true          SPINNER               mode                stop / spinLeft / spinRight
    false / true          ARM                   mode                base / low / mid / high
    false / true          ARM                   mode                drop / intake      (voor bakje)
    false / true          DRIVETRAIN            toPosition          x, y, rotation, speed
    false / true          DRIVETRAIN            toCircle            x, y, radius
    false / true          DRIVETRAIN            setPower            x, y, turn, power


    -----------------------------------------------------------------------------------------------
    */

    public Object[][] routeStorage = {
            {true, "DRIVETRAIN", "toPosition", -1041.0, -1581.0, 90.0, 0.3},
            {true, "DRIVETRAIN", "toPosition", -820.0, -824.0, 90.0, 0.3},
//            {true, "ARM", "mode", "high"},
//            {true, "ARM", "switchServo"},
//            {true, "WAIT", "wait", 1.0},
//            {true, "ARM", "switchServo"},
//            {false, "ARM", "mode", "base"},
            {true, "DRIVETRAIN", "toPosition", -1391.0, -1486.0, 90.0, 0.3},
//            {true, "WAIT", "wait", 0.5},
//            {true, "SPINNER", "mode", "spinLeft"},
//            {true, "WAIT", "wait", 2.0},
//            {true, "SPINNER", "mode", "stop"},
            {true, "DRIVETRAIN", "toPosition", 0.0, -1200.0, 180.0, 0.3},
            {true, "DRIVETRAIN", "setPower", 0.0, -1.0, 0.0, 1.0},
            {true, "WAIT", "wait", 3000.0},
            {true, "DRIVETRAIN", "setPower", 0.0, 0.0, 0.0, 0.0}
    };

    public Object[][] routeWarehouse = {
            // {true, "DRIVETRAIN", "toCircle", -300, -600, 228.6},
            {true, "ARM", "mode", "high"},
            {true, "ARM", "switchServo"},
            {true, "WAIT", "wait", 1.0},
            {true, "ARM", "switchServo"},
            {false, "ARM", "mode", "base"}
            // {false, "DRIVETRAIN", "toPosition", -1600, -1600, 0, 0.5},
    };

    public Object[][] test = {
            {true, "DRIVETRAIN", "toPosition", 0.0, -1200.0, 0.0, 0.3},
            {true, "DRIVETRAIN", "toPosition", 0.0, -600.0, -90.0, 0.3},
            {true, "DRIVETRAIN", "toPosition", -600.0, -600.0, -90.0, 0.3},
            {true, "DRIVETRAIN", "toPosition", -600.0, -1200.0, -90.0, 0.3},
            {true, "DRIVETRAIN", "toPosition", 0.0, -1200.0, 0.0, 0.3},
    };

    public Object[][] getRoute(String team, String startPosition, String ducks){
        Object[][] route = routeStorage;
        return route;
//        if (startPosition=="storage") {
//            if (team == "red") {
//                return spinningRed;
//            } else {
//                return spinningBlue;
//            }
//        } else {
//            return warehouse;
//        }
    }

    public double[] getStartPosition(String team, String startPosition){
        Object[][] route = getRoute(team, startPosition, "right");
        double x = (double) route[0][3];
        double y = (double) route[0][4];
        double rotation = (double) route[0][5];
        double[] startPositionLocation = {x,y,rotation};
        return startPositionLocation;
    }


    private Object[][] switchRoute(Object[][] route){
        Object[][] newRoute = route.clone();
        for (Object[] instruction : newRoute) {
            String object = (String) instruction[1];
            String functie = (String) instruction[2];
            
            switch (object) {
                case "WAIT":
                    break;
                    
                case "INTAKE":
                    break;
                    
                case "ARM":
                    break;

                case "SPINNER": 
                    if (functie == "mode"){
                        if (instruction[3] == "spinLeft"){
                            instruction[3] = "spinRight";
                        } else if (instruction[3] == "spinRight"){
                            instruction[3] = "spinLeft";
                        }
                    }
                    break;

                case "DRIVETRAIN": 
                    if (functie == "toPosition" || functie == "toCircle") {
                        instruction[3] = (double) instruction[3] * -1; //x
                    }
                    if (functie == "toPosition") {
                        instruction[5] = (double) instruction[5] * -1; //rotation
                    }
                    if (functie == "timeBased1") {
                        instruction[4] = (double) instruction[4] * -1;
                    }
                    break;
                
                default: // if no match is found
                    throw new java.lang.Error("Part " + object + " does not exist");
            }
        }
        return newRoute;
    }
}
