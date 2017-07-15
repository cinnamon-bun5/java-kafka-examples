package com.fibanez.kafka.delayedClient.quartz;

import com.fibanez.kafka.utils.StoppableRunnable;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by fibanez on 15/7/17.
 */
public class ShutDownSchedulerListener implements SchedulerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutDownSchedulerListener.class);

    private ExecutorService executor;
    private List< ? extends StoppableRunnable> runnables;
    private String name;

    public ShutDownSchedulerListener(ExecutorService executor, List< ? extends StoppableRunnable> runnables, String name) {
        this.executor = executor;
        this.runnables = (runnables != null) ? runnables : new ArrayList();
        this.name = name;
    }

    @Override
    public void jobScheduled(Trigger trigger) {
        LOGGER.info("jobScheduled");
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        LOGGER.info("jobUnscheduled");
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        LOGGER.info("triggerFinalized");
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        LOGGER.info("triggerPaused");
    }

    @Override
    public void triggersPaused(String triggerGroup) {
        LOGGER.info("triggersPaused");
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        LOGGER.info("triggerResumed");
    }

    @Override
    public void triggersResumed(String triggerGroup) {
        LOGGER.info("triggersResumed");
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
        LOGGER.info("jobAdded");
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        LOGGER.info("jobDeleted");
        shutdown();
    }

    @Override
    public void jobPaused(JobKey jobKey) {
        LOGGER.info("jobPaused");
    }

    @Override
    public void jobsPaused(String jobGroup) {
        LOGGER.info("jobsPaused");
    }

    @Override
    public void jobResumed(JobKey jobKey) {
        LOGGER.info("jobResumed");
    }

    @Override
    public void jobsResumed(String jobGroup) {
        LOGGER.info("jobsResumed");
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
        LOGGER.info("schedulerError {}", msg);
    }

    @Override
    public void schedulerInStandbyMode() {
        LOGGER.info("schedulerInStandbyMode");
    }

    @Override
    public void schedulerStarted() {
        LOGGER.info("schedulerStarted");
    }

    @Override
    public void schedulerShutdown() {
        LOGGER.info("schedulerShutdown");
        shutdown();
    }

    @Override
    public void schedulerShuttingdown() {
        LOGGER.info("schedulerShuttingdown");
    }

    @Override
    public void schedulingDataCleared() {
        LOGGER.info("schedulingDataCleared");
    }

    private void shutdown() {
        LOGGER.info("shutdown");

        runnables.forEach( r -> r.shutdown() );

        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            LOGGER.error("tasks interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                LOGGER.error("cancel non-finished tasks");
            }
            executor.shutdownNow();
            LOGGER.info("shutdown finished");
        }

        LOGGER.info("Finished scheduled quartz {} demo", name);

        System.exit(0);
    }
}
