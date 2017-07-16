package com.fibanez.kafka.delayedClient.quartz;

import com.fibanez.kafka.utils.StoppableRunnable;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by fibanez on 15/7/17.
 */
public class ShutDownListener implements JobListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutDownListener.class);

    private ExecutorService executor;
    private List< ? extends StoppableRunnable> runnables;
    private String name;

    public ShutDownListener(ExecutorService executor, List< ? extends StoppableRunnable> runnables, String name) {
        this.executor = executor;
        this.runnables = runnables;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        try {
            if (context.getNextFireTime() == null && context.getScheduler().getCurrentlyExecutingJobs().size() == 1) {
                context.getScheduler().shutdown();

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
        } catch (SchedulerException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
