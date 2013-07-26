package tabuvrp.optstage;

import java.util.Set;
import tabuvrp.core.Graph;
import tabuvrp.core.Solution;
import tabuvrp.core.stage.Stage;
import tabuvrp.core.move.Move11;


public class OptStage extends Stage {

    private final Graph graph;
    private final Solution solution;
    private final OptMove11Generator generator;

    public OptStage(Graph graph,
                    Solution solution) {
        super();
        this.graph = graph;
        this.solution = solution;
        generator = new OptMove11Generator(graph, solution);
        setBestSolution(solution);
    }

    @Override
    protected boolean doStep() {
        double minDeltaCost = Double.MAX_VALUE;
        Move11 bestMove = null;
        boolean moveFound = false;

        Set<Move11> moves = generator.getMoves();
        for (Move11 move : moves) {
            if (move.getDeltaCost() < minDeltaCost &&
                move.getDeltaInfIndex() == 0) {
                /* new move candidate */
                bestMove = move;
                minDeltaCost = move.getDeltaCost();
                moveFound = true;
            }
        }
        if (moveFound && minDeltaCost < - 0.1) {
            generator.apply(bestMove);
            setBestSolution(solution);
            return true;
        }
        return false;
    }

    
}
