package tabuvrp;

import tabuvrp.vrp.*;
import tabuvrp.gui.TabuDemo;


public class Main {

    public static void main(String[] args) {

        VRP graph = null;

        /* GRAPH INIT */
        try {
            graph = VRPFactory.newVRPFromFile(args[0]);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }

        new TabuDemo(graph).setVisible(true);
    }
}
