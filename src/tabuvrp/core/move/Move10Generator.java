package tabuvrp.core.move;

import tabuvrp.core.Path;
import tabuvrp.core.Solution;


public class Move10Generator {

    protected final Solution solution;

    public Move10Generator(Solution solution) {
        if (solution == null) {
            throw new IllegalArgumentException("'solution' is null");
        }
        this.solution = solution;
    }

    public Move10 Move10(Integer sourceNode, Integer targetNode, int position) {
        
        Path sourcePath = solution.getPathByNodeIndex(sourceNode);
        Path targetPath = solution.getPathByNodeIndex(targetNode);

        double deltaCost = solution.deltaCostForRemove(sourceNode)
                + solution.deltaCostForInsert(targetNode, position, sourceNode);

        int deltaInfIndex = solution.deltaInfIndexForRemove(sourceNode)
                + solution.deltaInfIndexForInsert(targetNode, sourceNode);

        Move10 move = new Move10(sourcePath, sourceNode,
                                 targetPath, targetNode,
                                 position,
                                 deltaCost,
                                 deltaInfIndex);

        return move;
    }

    public void apply(Move10 move) {
        solution.remove(move.getSourceNode());
        solution.insert(move.getTargetNode(), 
                        move.getPosition(),
                        move.getSourceNode());
    }

}
