package org.firstinspires.ftc.teamcode;

import com.sun.tools.javac.util.ArrayUtils;

import org.firstinspires.ftc.teamcode.Location.Start;

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

    public Object[][] routeSpinner = {
            {true, "DRIVETRAIN", "toPosition", -1041.0, -1581.0, 90.0, 0.3},

            // Deliver preloaded freight
            {true, "DRIVETRAIN", "toPosition", -820.0, -824.0, 90.0, 0.3},
            {true, "ARM", "toAngle", 45.0},
            {true, "ARM", "toHeight", "{customElementHeight}"},
            {true, "ARM", "setIntake", "outtaking"},
            {true, "WAIT", "wait", 2.0},
            {true, "ARM", "setIntake", "stop"},
            {false, "ARM", "toAngle", 0.0},

            // Duck
            {true, "DRIVETRAIN", "toPosition", -1391.0, -1486.0, 90.0, 0.3},

            // Park
            {true, "DRIVETRAIN", "toPosition", 0.0, -1200.0, 180.0, 0.3},
            {true, "DRIVETRAIN", "driveImu", 0.0, -1.0, 180.0, 1.0, 1.1},
    };

    public Object[][] routeWarehouse = {
            {true, "DRIVETRAIN", "toPosition", -86.4, -1609.0, 90.0, 0.3},

            // Deliver preloaded freight
            {true, "DRIVETRAIN", "toPosition", 8.0, -1180.0, 0.0, 0.3},
            {true, "ARM", "toAngle", 45.0},
            {true, "ARM", "toHeight", "{customElementHeight}"},
            {true, "ARM", "toAngle", 110.0},
            {true, "ARM", "setIntake", "outtaking"},
            {true, "WAIT", "wait", 2.0},
            {true, "ARM", "setIntake", "stop"},
            {false, "ARM", "toAngle", 0.0},

            // Park
            {true, "DRIVETRAIN", "toPosition", 0.0, -1250.0, 0.0, 0.3},
            {true, "DRIVETRAIN", "driveImu", 0.0, 1.0, 0.0, 1.0, 1.1},
    };

    public Object[][] test = {
            {true, "DRIVETRAIN", "toPosition", -86.4, -1609.0, 90.0, 0.3},
            {true, "DRIVETRAIN", "driveImu", 0.0, 1.0, 90.0, 0.1, 30.0},
    };

    public Object[][] getRoute(Start.TeamColor team, Start.StartLocation startPosition, Start.CustomElement customElement){
        Object[][] route;
        if (startPosition == Start.StartLocation.SPINNER) {
            route = routeSpinner;
        } else if (startPosition == Start.StartLocation.WAREHOUSE){
            route = routeWarehouse;
        } else {
            throw new java.lang.Error(startPosition + " is not a starting position.");
        }
        route = replaceTemplates(route, customElement);
        return route;
    }

    private Object[][] replaceTemplates(Object[][] route, Start.CustomElement customElement){
        for (int i =0; i < route.length; i++){
            for (int j =0; j < route[i].length; j++){
                if (route[i][j] == "{customElementHeight}"){
                    if (customElement == Start.CustomElement.RIGHT){
                        route[i][j] = 940;
                    } else if (customElement == Start.CustomElement.MID){
                        route[i][j] = 600;
                    } if (customElement == Start.CustomElement.LEFT){
                        route[i][j] = 280;
                    }
                }
            }
        }
        return route;
    }

    public double[] getStartPosition(Start.TeamColor team, Start.StartLocation startPosition){
        Object[][] route = getRoute(team, startPosition, Start.CustomElement.MID);
        double x = (double) route[0][3];
        double y = (double) route[0][4];
        double rotation = (double) route[0][5];
        return new double[]{x,y,rotation};
    }


    private Object[][] switchRoute(Object[][] route){
        Object[][] newRoute = route.clone();
        for (Object[] instruction : newRoute) {
            String object = (String) instruction[1];
            String function = (String) instruction[2];
            
            switch (object) {
                case "WAIT":
                    break;
                    
                case "INTAKE":
                    break;

                case "ARM":
                    break;

                case "SPINNER": 
                    if (function == "mode"){
                        if (instruction[3] == "spinLeft"){
                            instruction[3] = "spinRight";
                        } else if (instruction[3] == "spinRight"){
                            instruction[3] = "spinLeft";
                        }
                    }
                    break;

                case "DRIVETRAIN": 
                    if (function == "toPosition" || function == "toCircle") {
                        instruction[3] = (double) instruction[3] * -1; //x
                    }
                    if (function == "toPosition") {
                        instruction[5] = (double) instruction[5] * -1; //rotation
                    }
                    if (function == "timeBased") {
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
