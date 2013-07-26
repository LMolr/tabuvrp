package tabuvrp.core;

import java.util.ArrayList;
import java.util.HashMap;


public class Solution {

    protected final Graph graph;
    protected final ArrayList<Path> paths;
    protected final HashMap<Integer, Path> nodesToPaths;

    public Solution(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("'graph' is null");
        }
        this.graph = graph;
        paths = new ArrayList<Path>(graph.getNodeCount());
        nodesToPaths = new HashMap<Integer, Path>();
    }

    protected Solution(Graph graph,
                       ArrayList<Path> paths,
                       HashMap<Integer, Path> nodesToPaths) {
        this.graph = graph;
        this.paths = paths;
        this.nodesToPaths = nodesToPaths;
    }

    public Solution deepCopy() {
        ArrayList<Path> newPaths = new ArrayList<Path>(paths.size());
        for (Path oldPath : paths) {
            newPaths.add(oldPath.deepCopy());
        }
        HashMap<Integer, Path> newNodesToPaths = new HashMap<Integer, Path>();
        for (Integer i : nodesToPaths.keySet()) {
            newNodesToPaths.put(i, newPaths.get(paths.indexOf(nodesToPaths.get(i))));
        }
        return new Solution(graph,
                            newPaths,
                            newNodesToPaths);
    }

    public Integer[][] getSolView() {
        Integer[][] v = new Integer[paths.size()][];
        for (int i = 0; i < paths.size(); ++i) {
            v[i] = paths.get(i).getPathView();
        }
        return v;
    }

    public int getPathSizeByNodeIndex(Integer nodeIndex) {
        return getPathByNodeIndex(nodeIndex).getStepCount();
    }

    public boolean inSamePath(Integer nodeIndex1, Integer nodeIndex2) {
        return getPathByNodeIndex(nodeIndex1)
                == getPathByNodeIndex(nodeIndex2);
    }

    public Path getPathByNodeIndex(Integer index) {
        return nodesToPaths.get(index);
    }

    public void makePath(Integer nodeIndex) {
        if (nodesToPaths.containsKey(nodeIndex)) {
            throw new IllegalArgumentException("a 'path' in 'solution' already contains 'nodeIndex' " + nodeIndex);
        }
        // check for nodeIndex
        graph.getNode(nodeIndex);
        Path p = new Path(graph);
        p.insert(0, nodeIndex);
        nodesToPaths.put(nodeIndex, p);
        paths.add(p);
    }

    public void remove(Integer nodeIndex) {
        Path path = getPathByNodeIndex(nodeIndex);
        path.remove(nodeIndex);
        if (path.isEmpty()) {
            paths.remove(path);
        }
        nodesToPaths.remove(nodeIndex);
    }

    public double deltaCostForRemove(Integer nodeIndex) {
        return getPathByNodeIndex(nodeIndex).deltaCostForRemove(nodeIndex);
    }

    public int deltaInfIndexForRemove(Integer nodeIndex) {
        Path p = getPathByNodeIndex(nodeIndex);
        return deltaDBtoDeltaInfIndex(
                p.getDemandBalance(),
                p.deltaDemandBalanceForRemove(nodeIndex) );
    }

    public void insert(Integer targetNode, int position, Integer nodeIndex) {
        Path path = getPathByNodeIndex(targetNode);
        path.insert(position, nodeIndex);
        nodesToPaths.put(nodeIndex, path);
    }

    public double deltaCostForInsert(Integer targetNode, int position, Integer nodeIndex) {
        return getPathByNodeIndex(targetNode).deltaCostForInsert(position, nodeIndex);
    }

    public int deltaInfIndexForInsert(Integer targetNode, Integer nodeIndex) {
        Path p = getPathByNodeIndex(targetNode);
        return deltaDBtoDeltaInfIndex(
                p.getDemandBalance(),
                p.deltaDemandBalanceForInsert(nodeIndex) );
    }

    public void replace(Integer sourceNode, Integer targetNode) {
        if (inSamePath(sourceNode, targetNode)) {
            throw new IllegalArgumentException("'source node' and 'target node' are in the same path");
        }
        Path sourcePath = getPathByNodeIndex(sourceNode);
        Path targetPath = getPathByNodeIndex(targetNode);
        sourcePath.replace(sourceNode, targetNode);
        targetPath.replace(targetNode, sourceNode);
        nodesToPaths.put(sourceNode, targetPath);
        nodesToPaths.put(targetNode, sourcePath);
    }

    public double deltaCostForReplace(Integer sourceNode, Integer targetNode) {
        return getPathByNodeIndex(sourceNode).deltaCostForReplace(sourceNode, targetNode)
                + getPathByNodeIndex(targetNode).deltaCostForReplace(targetNode, sourceNode);
    }

    public int deltaInfIndexForReplace(Integer sourceNode, Integer targetNode) {
        Path p1 = getPathByNodeIndex(sourceNode);
        Path p2 = getPathByNodeIndex(targetNode);
        return deltaDBtoDeltaInfIndex(
                    p1.getDemandBalance(),
                    p1.deltaDemandBalanceForReplace(sourceNode, targetNode) )
               + deltaDBtoDeltaInfIndex( 
                       p2.getDemandBalance(),
                       p2.deltaDemandBalanceForReplace(targetNode, sourceNode) );
    }

    public double getCost() {
        double cost = 0;
        for (Path path : paths) {
            cost += path.getCost();
        }
        return cost;
    }

    public int getInfIndex() {
        int infIndex = 0;
        for (Path path : paths) {
            int demBal = path.getDemandBalance();
            if (demBal > 0) {
                infIndex += demBal;
            }
        }
        return infIndex;
    }

    protected int deltaDBtoDeltaInfIndex(int curDB, int dDB) {
        int dII;
        if (curDB <= 0) {
            if (dDB <= 0) {
                // NEG NEG
                // no penalty
                dII = 0;
            }
            else {
                // NEG POS
                // penalty if dDB is big enough
                dII = (dDB + curDB > 0)? (dDB + curDB) : 0;
            }
        }
        else {
            if (dDB >= 0) {
                // POS POS
                // full penalty
                dII = dDB;
            }
            else {
                // POS NEG
                // full or partial gain
                dII = (dDB + curDB <= 0)? -curDB : dDB;
            }
        }        
        return dII;
    }
    
    public boolean isFeasible() {
        for (Path p : paths) {
            if (!p.isFeasible()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Solution\n");
        for (Path p : paths) {
            b.append('\t').append(p).append('\n');
        }
        b.append("cost: ~").append(Math.round(getCost()));
        b.append("\ninfeasibility index: ").append(getInfIndex());
        b.append('\n').append(isFeasible()? "feasible" : "NOT feasible");
        b.append('\n');
        return b.toString();
    }
}
