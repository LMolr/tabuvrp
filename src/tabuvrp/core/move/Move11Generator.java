package tabuvrp.core.move;

import tabuvrp.core.Path;
import tabuvrp.core.Solution;


public class Move11Generator {

    protected final Solution solution;

    public Move11Generator(Solution solution) {
        if (solution == null) {
            throw new IllegalArgumentException("'solution' is null");
        }
        this.solution = solution;
    }

    public Move11 Move11(Integer sourceNode, Integer targetNode) {

        Path sourcePath = solution.getPathByNodeIndex(sourceNode);
        Path targetPath = solution.getPathByNodeIndex(targetNode);

        double deltaCost = solution.deltaCostForReplace(sourceNode, targetNode);
        int deltaInfIndex = solution.deltaInfIndexForReplace(sourceNode, targetNode);

        Move11 move = new Move11(sourcePath, sourceNode,
                                 targetPath, targetNode,
                                 deltaCost,
                                 deltaInfIndex);

        return move;
    }

    public void apply(Move11 move) {
        solution.replace(move.getSourceNode(), move.getTargetNode());
    }

}
