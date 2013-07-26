package tabuvrp.core.move;


public abstract class Move {

    private final double deltaCost;
    private final int deltaInfIndex;

    public Move(double deltaCost,
                int deltaInfIndex) {
        this.deltaCost = deltaCost;
        this.deltaInfIndex = deltaInfIndex;
    }
    
    public final double getDeltaCost() {
        return deltaCost;
    }

    public final int getDeltaInfIndex() {
        return deltaInfIndex;
    }
}
