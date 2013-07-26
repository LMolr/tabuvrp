package tabuvrp.optstage;

import java.util.Set;
import java.util.HashSet;
import tabuvrp.core.Graph;
import tabuvrp.core.Path;
import tabuvrp.core.Solution;
import tabuvrp.core.move.*;


public class OptMove11Generator {

    protected final Integer[][] genMat;
    protected final Solution solution;
    protected final Move11Generator moveGen;

    public OptMove11Generator(Graph graph,
                              Solution solution) {
        genMat = graph.getNeighbourhood();
        this.solution = solution;
        moveGen = new Move11Generator(solution);
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
               !solution.inSamePath(i1, i2);
    }

    public Set<Move11> getMoves() {
        HashSet<Move11> moves = new HashSet<Move11>();
        for (Integer sourceNode : getRandomNodeIndexes(genMat.length)) {
            for (Integer targetNode : extract(sourceNode, genMat.length)) {
                moves.add(moveGen.Move11(sourceNode, targetNode));
            }
        }
        return moves;
    }

    public void apply(Move11 move) {
        moveGen.apply(move);
    }
}