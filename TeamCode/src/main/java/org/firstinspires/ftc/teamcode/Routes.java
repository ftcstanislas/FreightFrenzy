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
            {true, "DRIVETRAIN", "toPosition", -1100.0, -1609.0, 90.0, 0.3},

            // Deliver preloaded freight
            {false, "ARM", "toAngle", 90.0},
            {false, "ARM", "toHeight", 900},
            {true, "DRIVETRAIN", "toPosition", -780.0, -1100.0, 180.0, 0.3},
            {true, "ARM", "toHeight", "{customElementHeight}"},
            {true, "ARM", "toAngle", 50.0},
            {true, "ARM", "setIntake", "outtaking"},
            {true, "WAIT", "wait", 2.0},
            {true, "ARM", "setIntake", "stop"},

            // Duck
            {true, "ARM", "setIntake", "intaking"},
            {true, "ARM", "toAngle", -45.0},
            {true, "ARM", "setIntake", "stop"},
            {true, "ARM", "toHeight", 1900},
            {true, "DRIVETRAIN", "toPosition", -1041.0, -1430.0, 90.0, 0.3},
            {false, "ARM", "toAngle", "{spinnerAngle}"},
            {true, "DRIVETRAIN", "toPosition", -1450.0, -1300.0, 45.0, 0.3},
            {true, "DRIVETRAIN", "driveImu", 0.0, -1.0, 45.0, 0.4, 0.5},
            {true, "SPINNER", "mode", "spinLeft"},
            {true, "WAIT", "wait", 3.5},
            {true, "SPINNER", "mode", "stop"},

            // Park
            {false, "ARM", "toAngle", 180.0},
            {true, "DRIVETRAIN", "toPosition", -600.0, -1200.0, 180.0, 0.3},
            {true, "DRIVETRAIN", "driveImu", 0.0, -1.0, 180.0, 1.0, 2.4},
            {true, "ARM", "toAngle", -10.0},
            {true, "ARM", "toAngle", 70.0},
            {true, "ARM", "toAngle", 180.0},
    };

    public Object[][] routeWarehouse = {
            {true, "DRIVETRAIN", "toPosition", -86.4, -1609.0, 90.0, 0.3},

            // Deliver preloaded freight
            {false, "ARM", "toAngle", 90.0},
            {false, "ARM", "toHeight", 900},
            {true, "DRIVETRAIN", "toPosition", 8.0, -1180.0, 0.0, 0.3},
            {true, "ARM", "toHeight", "{customElementHeight}"},
            {true, "ARM", "toAngle", 130.0},
            {true, "ARM", "setIntake", "outtaking"},
            {true, "WAIT", "wait", 2.0},
            {true, "ARM", "setIntake", "stop"},

            // Park
            {false, "ARM", "toHeight", 900},
            {false, "ARM", "toAngle", 0.0},
            {true, "DRIVETRAIN", "toPosition", -100.0, -1200.0, 0.0, 0.3},
            {true, "DRIVETRAIN", "driveImu", 0.0, 1.0, 0.0, 1.0, 1.7},
    };

    public Object[][] test = {
            {true, "DRIVETRAIN", "toPosition", -86.4, -1609.0, 90.0, 0.3},
            {true, "DRIVETRAIN", "driveImu", 0.0, 1.0, 90.0, 0.1, 30.0},
    };

    public Object[][] getRoute(Start.TeamColor teamColor, Start.StartLocation startPosition, Start.CustomElement customElement){
        // Get route based on startPosition
        Object[][] route;
        if (startPosition == Start.StartLocation.SPINNER) {
            route = Routes.deepCopy(routeSpinner);
        } else if (startPosition == Start.StartLocation.WAREHOUSE){
            route = Routes.deepCopy(routeWarehouse);
        } else {
            throw new java.lang.Error(startPosition + " is not a starting position.");
        }

        // Update route for team color
        if (teamColor == Start.TeamColor.RED){
            // route is made for red team
        } else if (teamColor == Start.TeamColor.BLUE){
            route = switchRoute(route);
        } else {
            throw new java.lang.Error(teamColor + " is not a team color.");
        }

        // Update route for custom element
        route = replaceTemplates(route, customElement, teamColor);
        return route;
    }

    public double[] getStartPosition(Start.TeamColor team, Start.StartLocation startPosition){
        Object[][] routeTest = getRoute(team, startPosition, Start.CustomElement.LEFT);
        double x = (double) routeTest[0][3];
        double y = (double) routeTest[0][4];
        double rotation = (double) routeTest[0][5];
        return new double[]{x,y,rotation};
    }

    private Object[][] replaceTemplates(Object[][] route, Start.CustomElement customElement, Start.TeamColor teamColor){
        Object[][] newRoute = Routes.deepCopy(route);
        for (int i =0; i < newRoute.length; i++){
            for (int j =0; j < newRoute[i].length; j++){
                if (newRoute[i][j] == "{customElementHeight}"){
                    if (customElement == Start.CustomElement.RIGHT){
                        newRoute[i][j] = 980;
                    } else if (customElement == Start.CustomElement.MID){
                        newRoute[i][j] = 640;
                    } if (customElement == Start.CustomElement.LEFT){
                        newRoute[i][j] = 464;
                    }
                } else if (newRoute[i][j] == "{spinnerAngle}") {
                    if (teamColor == Start.TeamColor.RED) {
                        newRoute[i][j] = 180.0;
                    } else if (teamColor == Start.TeamColor.BLUE) {
                        newRoute[i][j] = 0.0;
                    }
                }
            }
        }
        return newRoute;
    }

    private Object[][] switchRoute(Object[][] route){
        Object[][] newRoute = Routes.deepCopy(route);
        for (Object[] instruction : newRoute) {
            String object = (String) instruction[1];
            String function = (String) instruction[2];
            
            switch (object) {
                case "WAIT":
                    break;
                    
                case "INTAKE":
                    break;

                case "ARM":
                    if (function == "toAngle") {
                        instruction[3] = (double) instruction[3] * -1;
                    }

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
                    if (function == "toPosition") {
                        instruction[4] = (double) instruction[4] * -1; //y
                        instruction[5] = (double) instruction[5] * -1; //rotation
                    }
                    if (function == "driveImu") {
                        instruction[5] = (double) instruction[5] * -1; //rotation
                    }

                    break;
                
                default: // if no match is found
                    throw new java.lang.Error("Part " + object + " does not exist");
            }
        }
        return newRoute;
    }

    public static Object[][] deepCopy(Object[][] original) {
        if (original == null) {
            return null;
        }

        final Object[][] result = new Object[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }
}
