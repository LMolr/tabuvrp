package tabuvrp.tabustage;

import java.util.Set;
import java.util.HashSet;
import tabuvrp.core.Graph;
import tabuvrp.core.Path;
import tabuvrp.core.Solution;
import tabuvrp.core.move.*;


public class TabuMoveGenerator {

    protected final Integer[][] genMat;
    protected final Solution solution;
    protected final TabuIndex<Integer, Path> tabuIndex;
    protected final TabuStageParams params;
    protected final Move10Generator moveGen;

    public TabuMoveGenerator(Graph graph,
                             Solution solution,
                             TabuIndex<Integer, Path> tabuIndex,
                             TabuStageParams params) {
        genMat = graph.getNeighbourhood();
        this.solution = solution;
        this.tabuIndex = tabuIndex;
        this.params = params;
        moveGen = new Move10Generator(solution);
    }

    public Set<Integer> extract(Integer row, int count) {
        final HashSet<Integer> extSet = new HashSet<Integer>();
        int i = 0;
        while (extSet.size() < count &&
               i < genMat[row].length) {
            if (accepted(row, genMat[row][i])) {
                extSet.add(genMat[row][i]);
            }
            i += 1;
        }
        return extSet;
    }

    public Set<Integer> getRandomNodeIndexes(int count) {
        final HashSet<Integer> indexes = new HashSet<Integer>();
        int limit = (count <= genMat.length - 1)? count : genMat.length - 1;
        while (indexes.size() < limit) {
            Integer index = (int) Math.round(Math.random() * (genMat.length - 2) + 1);
            indexes.add(index);
        }
        return indexes;
    }

    private final boolean accepted(Integer i1, Integer i2) {
        return i2 != 0 &&
               !tabuIndex.isTabu(i1, solution.getPathByNodeIndex(i2)) &&
               !solution.inSamePath(i1, i2);
    }

    public Set<Move10> getMoves() {
        HashSet<Move10> moves = new HashSet<Move10>();
        for (Integer sourceNode : getRandomNodeIndexes(params.getQ())) {
            for (Integer targetNode : extract(sourceNode, params.getP())) {
                for (int pos = 0; pos <= solution.getPathSizeByNodeIndex(targetNode); ++pos) {
                    moves.add(moveGen.Move10(sourceNode, targetNode, pos));
                }
            }
        }
        return moves;
    }

    public void apply(Move10 move) {
        moveGen.apply(move);
    }
}
