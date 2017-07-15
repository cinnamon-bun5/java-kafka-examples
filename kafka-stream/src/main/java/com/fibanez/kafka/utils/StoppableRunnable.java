package com.fibanez.kafka.utils;

/**
 * Created by fibanez on 15/7/17.
 */
public interface StoppableRunnable extends Runnable {

    void shutdown();
}
