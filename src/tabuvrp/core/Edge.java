package tabuvrp.core;


public final class Edge {

    private final double cost;

    public Edge(double cost) {
        this.cost = cost;
    }

    public Edge(String s) {
        this.cost = Integer.parseInt(s);
    }

    public final double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Cost: " + cost;
    }
}
