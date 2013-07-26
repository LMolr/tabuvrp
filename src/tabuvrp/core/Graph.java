package tabuvrp.core;


public interface Graph {

    public Node getNode(int n);

    public Edge getEdge(int n1, int n2);

    public Integer[][] getNeighbourhood();

    public int getNodeCount();

    public double getMTilde();

    public double getMinX();

    public double getMaxX();

    public double getMinY();
    
    public double getMaxY();
}
