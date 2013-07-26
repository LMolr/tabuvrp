package tabuvrp.tabustage;

import tabuvrp.core.stage.StageListener;


public interface TabuStageListener extends StageListener {

    public void newUsefulStep(TabuStage stage, boolean feasible);

}
