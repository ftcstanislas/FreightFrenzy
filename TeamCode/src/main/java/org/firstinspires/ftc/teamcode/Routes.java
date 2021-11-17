package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;

public class Routes {
    
    /*
    stiins telefoon was hier
    -----------------------------------------------------------------------------------------------
    WAIT UNTIL FINISHED   OBJECT                FUNCTIE             ARGUMENTEN (achter elkaar)
    -----------------------------------------------------------------------------------------------
    false / true          WAIT                  wait                seconds
    false / true          INTAKE                mode                stop / intaking
    false / true          SPINNER               mode                stop / spinLeft / spinRight
    false / true          ARM                   mode                base / low / mid / high
    false / true          DRIVETRAIN            toPosition          x, y, rotation, speed
    false / true          DRIVETRAIN            toCircle            x, y, radius

    -----------------------------------------------------------------------------------------------
    */


    public static Object[][] routeRight = {
            {true, "ARM", "mode", "high"},
            {true, "ARM", "mode", "base"},
            {true, "SPINNER", "mode", "spinLeft"},

    };

    public static Object[][] switchedRouteRight = switchRoute(routeRight);
    
    public static Object[][] getRoute(String team, String startPosition){
        return routeRight;
    }

    public static Object[][] switchRoute(Object[][] route){
        Object[][] newRoute = {};
        for (Object[] instruction : route) {
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
                    break;
                
                default: // if no match is found
                    throw new java.lang.Error("Part " + object + " does not exist");
            }
            
            newRoute[newRoute.length] = instruction;
        }
        return newRoute;
    }
}
