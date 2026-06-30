package mephi.rxjava.test;

import mephi.rxjava.Observable;
import mephi.rxjava.Observer;
import mephi.rxjava.Scheduler;
import mephi.rxjava.SingleThreadScheduler;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SubscribeOnObserveOnTest {

    @Test
    void testSubscribeOnAndObserveOn() throws InterruptedException {
        Scheduler subscribeScheduler = new SingleThreadScheduler();
        Scheduler observeScheduler = new SingleThreadScheduler();

        AtomicReference<String> subscribeThread = new AtomicReference<>();
        AtomicReference<String> observeThread = new AtomicReference<>();

        Observable<String> observable = Observable.create(emitter -> {
            subscribeThread.set(Thread.currentThread().getName());
            emitter.onNext("data");
            emitter.onComplete();
        });

        observable
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String item) {
                        observeThread.set(Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable t) {
                        fail("Should not fail");
                    }

                    @Override
                    public void onComplete() {
                        assertTrue(subscribeThread.get() != null && observeThread.get() != null);
                        assertTrue(!subscribeThread.get().equals(observeThread.get()), "subscribeOn and observeOn should run in different threads");
                    }
                });

        Thread.sleep(300); // дожидаемся завершения
    }
}

