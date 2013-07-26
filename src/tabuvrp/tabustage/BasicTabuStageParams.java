package tabuvrp.tabustage;

import tabuvrp.core.Solution;


public final class BasicTabuStageParams extends TabuStageParams {

    protected final Solution solution;
    protected double alpha;
    protected final int BETA;
    protected final int P;
    protected final int Q;
    protected long noFeasTransStreak;
    protected boolean feasible;

    public BasicTabuStageParams(Solution solution,
            double initialAlpha,
            int beta,
            int p,
            int q,
            int minTheta,
            int maxTheta,
            int maxSteps) {
        super(maxSteps, minTheta, maxTheta);
        if (solution == null) {
            throw new IllegalArgumentException("'solution' is null");
        }
        this.solution = solution;
        if (initialAlpha <= 0) {
            throw new IllegalArgumentException("'alpha' <= 0");
        }
        alpha = initialAlpha;
        if (beta <= 0) {
            throw new IllegalArgumentException("'beta' <= 0");
        }
        BETA = beta;
        if (p <= 0) {
            throw new IllegalArgumentException("'p' <= 0");
        }
        P = p;
        if (q <= 0) {
            throw new IllegalArgumentException("'q' <= 0");
        }
        Q = q;
        if (maxSteps <= 0) {
            throw new IllegalArgumentException("'max steps' <= 0");
        }
        noFeasTransStreak = 0;
        feasible = solution.isFeasible();
    }

    public final void step() {
        if (solution.isFeasible() == feasible) {
            noFeasTransStreak += 1;
            if (noFeasTransStreak == BETA) {
                alpha = feasible? (alpha / 2) : (alpha  * 2);
                noFeasTransStreak = 0;
            }
        } else {
            noFeasTransStreak = 0;
            feasible = !feasible;
        }
    }

    public final double getAlpha() {
        return alpha;
    }

    public final int getP() {
        return P;
    }

    public final int getQ() {
        return Q;
    }

    public final int getTheta() {
        return (int) Math.round(Math.random() * (MAX_THETA - MIN_THETA) + MIN_THETA);
    }
}
