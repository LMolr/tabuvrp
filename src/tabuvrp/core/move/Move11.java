package tabuvrp.core.move;

import tabuvrp.core.Path;


public final class Move11 extends Move {

    private final Path sourcePath;
    private final Integer sourceNode;
    private final Path targetPath;
    private final Integer targetNode;

    public Move11(Path sourcePath, Integer sourceNode,
                  Path targetPath, Integer targetNode,
                  double deltaCost,
                  int deltaInfIndex) {
        super(deltaCost, deltaInfIndex);
        this.sourcePath = sourcePath;
        this.sourceNode = sourceNode;
        this.targetPath = targetPath;
        this.targetNode = targetNode;
    }

    public final Path getSourcePath() {
        return sourcePath;
    }

    public final Integer getSourceNode() {
        return sourceNode;
    }

    public final Path getTargetPath() {
        return targetPath;
    }

    public final Integer getTargetNode() {
        return targetNode;
    }
}