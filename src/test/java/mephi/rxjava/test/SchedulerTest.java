package mephi.rxjava.test;

import mephi.rxjava.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SchedulerTest {

    @Test
    void testIOThreadScheduler() throws InterruptedException {
        Scheduler scheduler = new IOThreadScheduler();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean executed = new AtomicBoolean(false);

        scheduler.execute(() -> {
            executed.set(true);
            latch.countDown();
        });

        latch.await();
        assertTrue(executed.get(), "IOThreadScheduler should execute task");
    }

    @Test
    void testComputationScheduler() throws InterruptedException {
        Scheduler scheduler = new ComputationScheduler();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean executed = new AtomicBoolean(false);

        scheduler.execute(() -> {
            executed.set(true);
            latch.countDown();
        });

        latch.await();
        assertTrue(executed.get(), "ComputationScheduler should execute task");
    }

    @Test
    void testSingleThreadScheduler() throws InterruptedException {
        Scheduler scheduler = new SingleThreadScheduler();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean executed = new AtomicBoolean(false);

        scheduler.execute(() -> {
            executed.set(true);
            latch.countDown();
        });

        latch.await();
        assertTrue(executed.get(), "SingleThreadScheduler should execute task");
    }
}
