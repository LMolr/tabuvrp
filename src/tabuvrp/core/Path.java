package tabuvrp.core;

import java.util.ArrayList;


public class Path {

    protected final Graph graph;
    protected final ArrayList<Integer> steps;
    protected double cost;
    protected int demandBalance;

    public Path(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("'graph' is null");
        }
        this.graph = graph;
        steps = new ArrayList<Integer>();
        cost = 0;
        demandBalance = graph.getNode(0).getDemand();
    }

    protected Path(Graph graph,
                   ArrayList<Integer> steps,
                   double cost,
                   int demandBalance) {
        this.graph = graph;
        this.steps = steps;
        this.cost = cost;
        this.demandBalance = demandBalance;
    }
    
    public Path deepCopy() {
        return new Path(graph, // graph is immutable
                        new ArrayList<Integer>(steps),
                        cost,
                        demandBalance);
    }

    public Integer[] getPathView() {
        Integer[] view = new Integer[steps.size() + 2];
        view[0] = 0;
        int i;
        for (i = 0; i < steps.size(); ++i) {
            view[i + 1] = Integer.valueOf(steps.get(i));
        }
        view[i + 1] = 0;
        return view;
    }

    public boolean isEmpty() {
        return steps.isEmpty();
    }

    public void insert(int position, Integer nodeIndex) {
        cost += deltaCostForInsert(position, nodeIndex);
        demandBalance += deltaDemandBalanceForInsert(nodeIndex);
        steps.add(position, nodeIndex);
    }

    public double deltaCostForInsert(int position, Integer nodeIndex) {
        if (steps.isEmpty()) {
            return    graph.getEdge(0, nodeIndex).getCost()
                    + graph.getEdge(nodeIndex, 0).getCost();
        } else {
            Integer start = (position == 0) ? 0 : steps.get(position - 1);
            Integer end = (position == steps.size()) ? 0 : steps.get(position);
            return    graph.getEdge(start, nodeIndex).getCost()
                    + graph.getEdge(nodeIndex, end).getCost()
                    - graph.getEdge(start, end).getCost();
        }
    }

    public int deltaDemandBalanceForInsert(Integer nodeIndex) {
        return graph.getNode(nodeIndex).getDemand();
    }

    public void remove(Integer nodeIndex) {
        cost += deltaCostForRemove(nodeIndex);
        demandBalance += deltaDemandBalanceForRemove(nodeIndex);
        int position = getPositionByNodeIndex(nodeIndex);
        steps.remove(position);
    }

    public double deltaCostForRemove(Integer nodeIndex) {
        int position = getPositionByNodeIndex(nodeIndex);
        if (steps.size() <= 1) {
            return -cost;
        }
        Integer start = (position == 0) ? 0 : steps.get(position - 1);
        Integer mid = nodeIndex;
        Integer end = (position == steps.size() - 1) ? 0 : steps.get(position + 1);
        return    graph.getEdge(start, end).getCost()
                - graph.getEdge(start, mid).getCost()
                - graph.getEdge(mid, end).getCost();
    }

    public int deltaDemandBalanceForRemove(Integer nodeIndex) {
        Node node = graph.getNode(nodeIndex);
        return - node.getDemand();
    }

    public void replace(Integer sourceNode, Integer targetNode) {
        cost += deltaCostForReplace(sourceNode, targetNode);
        demandBalance += deltaDemandBalanceForReplace(sourceNode, targetNode);
        steps.set(getPositionByNodeIndex(sourceNode), targetNode);
    }

    public double deltaCostForReplace(Integer sourceNode, Integer targetNode) {
        if (   steps.contains(targetNode)
            || !steps.contains(sourceNode)) {
            throw new IllegalArgumentException("path doesn't contain 'sourceNode' or path already contains 'targetNode'");
        }
        int position = getPositionByNodeIndex(sourceNode);
        Integer start = (position == 0) ? 0 : steps.get(position - 1);
        Integer end = (position == steps.size() - 1) ? 0 : steps.get(position + 1);
        return    graph.getEdge(start, targetNode).getCost()
                + graph.getEdge(targetNode, end).getCost()
                - graph.getEdge(start, sourceNode).getCost()
                - graph.getEdge(sourceNode, end).getCost();
    }

    public int deltaDemandBalanceForReplace(Integer sourceNode, Integer targetNode) {
        if (   steps.contains(targetNode)
            || !steps.contains(sourceNode)) {
            throw new IllegalArgumentException("path doesn't contain 'sourceNode' or path already contains 'targetNode'");
        }
        return graph.getNode(targetNode).getDemand()
                - graph.getNode(sourceNode).getDemand();
    }
    
    public double getCost() {
        return cost;
    }

    public int getDemandBalance() {
        return demandBalance;
    }

    public boolean isFeasible() {
        return demandBalance <= 0;
    }

    public int getStepCount() {
        return steps.size();
    }

    public int getPositionByNodeIndex(Integer nodeIndex) {
        int position = steps.indexOf(nodeIndex);
        if (position == -1) {
            throw new IllegalArgumentException("'path' doesn't contain 'nodeIndex' " + nodeIndex);
        }
        return position;
    }

    public Graph getProblem() {
        return graph;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Path 0 ");
        for (Integer i : steps) {
            b.append(i).append(" ");
        }
        b.append("0    cost: ~").append(Math.round(cost));
        b.append(" demand balance: ").append(demandBalance);
        return b.toString();
    }
}
