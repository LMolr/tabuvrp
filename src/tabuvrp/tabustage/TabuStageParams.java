package tabuvrp.tabustage;


public abstract class TabuStageParams {

    public final int MAX_STEPS;
    public final int MIN_THETA;
    public final int MAX_THETA;

    public TabuStageParams(int maxSteps,
                           int minTheta,
                           int maxTheta) {
        if (maxSteps <= 0) {
            throw new IllegalArgumentException("'max steps' <= 0");
        }
        MAX_STEPS = maxSteps;
        if (   minTheta < 0
            || maxTheta < 0) {
            throw new IllegalArgumentException("'theta' < 0");
        }
        if (minTheta > maxTheta) {
            throw new IllegalArgumentException("'min theta' > 'max theta'");
        }
        MIN_THETA = minTheta;
        MAX_THETA = maxTheta;
    }

    public abstract void step();

    public abstract double getAlpha();

    public abstract int getP();

    public abstract int getQ();
    
    public abstract int getTheta();
}
