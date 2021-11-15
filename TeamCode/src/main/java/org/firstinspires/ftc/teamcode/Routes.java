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
    false / true          ARM                   mode                low / mid / high
    false / true          DRIVETRAIN            toPosition          x, y, rotation, speed
    false / true          DRIVETRAIN            toCircle            x, y, radius

    -----------------------------------------------------------------------------------------------
    */


    public Object[][] route1 = {
        {true, "INTAKE", "mode", "intaking"},
        {true, "WAIT", "wait", 2},
        {true, "SPINNER", "mode", "spinLeft"},
        {true, "DRIVETRAIN", "toPosition", 23, 10, 40, 1},
        {true, "DRIVETRAIN", "toPosition", 23, 10, 20}
    };

    public Object[][] switchedRoute = switchRoute(route1);
    
    public static Object[][] getRoute(String team, String startPosition){
        Object[][] verwijder = {{"Boe"}};
        return verwijder;
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
