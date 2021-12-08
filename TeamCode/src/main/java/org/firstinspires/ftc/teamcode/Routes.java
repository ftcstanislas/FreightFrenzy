package org.firstinspires.ftc.teamcode;

import com.sun.tools.javac.util.ArrayUtils;

import java.util.ArrayList;

public class Routes {
    
    /*
    Stijns telefoon was hier
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

    -----------------------------------------------------------------------------------------------
    */


    public Object[][] routeRight = {
            {true, "SPINNER", "mode", "spinRight"},
            {true, "WAIT", "wait", 1.0},
            {true, "SPINNER", "mode", "stop"},
            {true, "INTAKE", "mode", "intaking"},
            {true, "WAIT", "wait", 1.0},
            {true, "INTAKE", "mode", "stop"},
            {true, "ARM", "mode", "high"},
            {true, "ARM", "mode", "base"},
    };

    public Object[][] routeStorageOfficial = {
            // {true, "DRIVETRAIN", "toCircle", -300, -600, 228.6},
            {true, "ARM", "mode", "high"},
            {true, "ARM", "switchServo"},
            {true, "WAIT", "wait", 1.0},
            {true, "ARM", "switchServo"},
            {false, "ARM", "mode", "base"},
            // {false, "DRIVETRAIN", "toPosition", -1600, -1600, 0, 0.5},
            {true, "WAIT", "wait", 0.5},
            {true, "SPINNER", "mode", "spinLeft"},
            {true, "WAIT", "wait", 2.0},
            {true, "SPINNER", "mode", "stop"},
            // ,{true, "DRIVETRAIN", "toPosition", -1600, -1000}
    };

    public Object[][] spinningRed = {
            {true, "DRIVETRAIN", "timeBased", -1.0, 0.3, 0.2},
            {true, "WAIT", "wait", 2.0},
            {true, "DRIVETRAIN", "timeBased", 0.0, 0.0, 0.0},
            {true, "SPINNER", "mode", "spinLeft"},
            {true, "WAIT", "wait", 5.0},
            {true, "SPINNER", "mode", "stop"},
            {true, "DRIVETRAIN", "timeBased1", 0.0, 1.0, -0.3},
            {true, "WAIT", "wait", 1.5},
            {true, "DRIVETRAIN", "timeBased", 0.0, 0.0, 0.0},
    };

    public Object[][] spinningBlue = {
            {true, "DRIVETRAIN", "timeBased", -1.0, -0.1, 0.0},
            {true, "WAIT", "wait", 1.5},
            {true, "DRIVETRAIN", "timeBased", 0.0, 0.0, 0.0},
            {true, "SPINNER", "mode", "spinRight"},
            {true, "WAIT", "wait", 5.0},
            {true, "SPINNER", "mode", "stop"},
            //{true, "DRIVETRAIN", "timeBased1", -0.3, -1.0, -0.0},
            //{true, "WAIT", "wait", 1.2},
            // {true, "DRIVETRAIN", "timeBased", 0.0, 0.0, 0.0},
    };

    public Object[][] warehouse = {
            {true, "DRIVETRAIN", "timeBased", 1.0, 0.0, 0.0},
            {true, "WAIT", "wait", 3.0},
            {true, "DRIVETRAIN", "timeBased", 0.0, 0.0, 0.0}
    };


    public Object[][] routeWarehouseOfficial = {
            // {true, "DRIVETRAIN", "toCircle", -300, -600, 228.6},
            {true, "ARM", "mode", "high"},
            {true, "ARM", "switchServo"},
            {true, "WAIT", "wait", 1.0},
            {true, "ARM", "switchServo"},
            {false, "ARM", "mode", "base"}
            // {false, "DRIVETRAIN", "toPosition", -1600, -1600, 0, 0.5},
    };

    public Object[][] test = {
            {true, "DRIVETRAIN", "toPosition", -1200.0, -1200.0, 180.0, 0.3},
            {true, "DRIVETRAIN", "toPosition", -1200.0, -600.0, 180.0, 0.3},
            {true, "DRIVETRAIN", "toPosition", -1200.0, -1200.0, 180.0, 0.3},
            {true, "DRIVETRAIN", "toPosition", -1200.0, -600.0, 180.0, 0.3},
            {true, "DRIVETRAIN", "toPosition", -1200.0, -1200.0, 180.0, 0.3}
//            {true, "DRIVETRAIN", "toPosition", -600.0, -600.0, 180.0, 0.3},
//            {true, "DRIVETRAIN", "toPosition", -600.0, -1200.0, 180.0, 0.3},
//            {true, "DRIVETRAIN", "toPosition", -1200.0, -1200.0, 180.0, 0.3},
//            {true, "DRIVETRAIN", "toPosition", -1200.0, -600.0, 180.0, 0.3},
//            {true, "DRIVETRAIN", "toPosition", 0.0, -600.0, 180.0, 0.3},
//            {true, "DRIVETRAIN", "toPosition", 0.0, -1200.0, 180.0, 0.3},
//            {true, "DRIVETRAIN", "toPosition", -1200.0, -1200.0, 180.0, 0.3}
    };

    // public Object[][] switchedRouteRight = switchRoute(routeRight);
    
    public Object[][] getRoute(String team, String startPosition, String ducks){
        return test;
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

    public Object[][] switchRoute(Object[][] route){
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
