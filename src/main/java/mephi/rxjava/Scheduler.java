package mephi.rxjava;

public interface Scheduler {
    void execute(Runnable task);
}

