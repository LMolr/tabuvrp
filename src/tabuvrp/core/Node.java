package tabuvrp.core;


public final class Node {

    private double x;
    private double y;
    private final int demand;

    public Node(double x,
                double y,
                int demand) {
        this.x = x;
        this.y = y;
        this.demand = demand;
    }

    public Node(String s) {
        this.demand = Integer.parseInt(s);
    }

    public static final double distance(Node n1, Node n2) {
        return Math.sqrt(  Math.pow(n2.x - n1.x, 2)
                         + Math.pow(n2.y - n1.y, 2));
    }

    public final int getDemand() {
        return demand;
    }

    public final double getX() {
        return x;
    }

    public final double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Demand: " + demand;
    }
}

