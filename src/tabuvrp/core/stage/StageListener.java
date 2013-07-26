package tabuvrp.core.stage;


public interface StageListener {

    public void stageStarted(Stage stage);

    public void stepDone(Stage stage);

    public void newBestSolution(Stage stage);
    
    public void stageStopped(Stage stage);

}
