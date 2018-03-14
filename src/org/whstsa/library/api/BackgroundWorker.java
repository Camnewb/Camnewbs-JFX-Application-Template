package org.whstsa.library.api;

import javafx.application.Platform;
import org.whstsa.library.util.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BackgroundWorker is a class used for registering background operations
 * that run parallel with the main thread.
 * These operations run once every 50ms.
 * Use <code>BackgroundWorker.registerOperation(() -> {*Operation here*})</code>.
 */
@SuppressWarnings("InfiniteLoopStatement")
public class BackgroundWorker extends Thread {

    private static final BackgroundWorker singleton;

    static {
        singleton = new BackgroundWorker();
    }

    private List<Runnable> tickedOperationList = new ArrayList<>();
    private Logger logger = new Logger("BackgroundWorker");

    private BackgroundWorker() {
        this.logger.log("Background worker has loaded.");
    }

    public static BackgroundWorker getBackgroundWorker() {
        return singleton;
    }

    @Override
    public void run() {
        this.logger.log("Background worker has started.");
        while (true) {
            try {
                this.tick();
                Thread.sleep(50);
            } catch (InterruptedException e) {
                this.logger.warn("Background worker has been stopped abruptly.");
            }
        }
    }

    private void tick() {
        this.tickedOperationList.forEach(Platform::runLater);
    }

    public List<Runnable> getTickOperations() {
        return Collections.unmodifiableList(this.tickedOperationList);
    }

    /**
     * Used to register operations to be run in the background
     * @param operation Operation to be performed
     */
    public void registerOperation(Runnable operation) {
        this.tickedOperationList.add(operation);
    }
}
