package tabuvrp.core.stage;

import java.util.HashSet;
import tabuvrp.core.Solution;


public abstract class Stage {

    enum State {
        IDLE,
        RUNNING,
        STOPPED;
    }

    private HashSet<StageListener> listeners;
    private State state;
    private boolean stopRequested;
    private long startTime;
    private long stopTime;
    private long steps;
    protected Solution bestSolution;

    public Stage() {
        listeners = new HashSet<StageListener>();
        stopRequested = false;
        state = State.IDLE;
        steps = 0;
    }

    public void addStageListener(StageListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("cannot add 'null' as listener");
        }
        listeners.add(listener);
    }

    public void remStageListener(StageListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("cannot remove 'null' listener");
        }
        listeners.remove(listener);
    }

    public void runStage() {
        if (state != State.IDLE) {
            return;
        }
        startTime = System.nanoTime();
        state = State.RUNNING;
        notifyAll_StageStarted();
        while (!stopRequested) {
            stopRequested = !doStep();
            steps += 1;
            notifyAll_StepDone();
        }
        stopTime = System.nanoTime();
        state = State.STOPPED;
        notifyAll_StageStopped();
    }

    public void stopStage() {
        if (state == State.RUNNING) {
            stopRequested = true;
        }
    }

    public long getStartTime() {
        return (state != State.IDLE)? startTime : -1;
    }

    public long getStopTime() {
        return (state == State.STOPPED)? stopTime : -1;
    }

    public long getElaborationTime() {
        if (state == State.RUNNING) {
            return System.nanoTime() - startTime;
        }
        if (state == State.STOPPED) {
                return stopTime - startTime;
        }
        return -1;
    }

    public long getSteps() {
        return steps;
    }

    private void notifyAll_StageStarted() {
        for (StageListener listener : listeners) {
            listener.stageStarted(this);
        }
    }

    private void notifyAll_StepDone() {
        for (StageListener listener : listeners) {
            listener.stepDone(this);
        }
    }

    private void notifyAll_StageStopped() {
        for (StageListener listener : listeners) {
            listener.stageStopped(this);
        }
    }

    private void notifyAll_NewBestSolution() {
        for (StageListener listener : listeners) {
            listener.newBestSolution(this);
        }
    }

    protected void setBestSolution(Solution newBestSolution) {
        if (newBestSolution == null) {
            throw new IllegalArgumentException("'solution' null");
        }
        bestSolution = newBestSolution.deepCopy();
        notifyAll_NewBestSolution();
    }

    public Solution getBestSolution() {
        return bestSolution.deepCopy();
    }
    
    protected abstract boolean doStep();

}
