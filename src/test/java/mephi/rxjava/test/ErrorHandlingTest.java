package mephi.rxjava.test;

import mephi.rxjava.Observable;
import mephi.rxjava.Observer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandlingTest {

    @Test
    void testErrorPropagation() {
        Observable<String> observable = Observable.create(emitter -> {
            emitter.onNext("first");
            emitter.onError(new RuntimeException("Test failure"));
            emitter.onNext("second"); // не должен дойти
            emitter.onComplete();     // не должен дойти
        });

        AtomicBoolean onNextCalled = new AtomicBoolean(false);
        AtomicBoolean onErrorCalled = new AtomicBoolean(false);
        AtomicBoolean onCompleteCalled = new AtomicBoolean(false);

        observable.subscribe(new Observer<String>() {
            @Override
            public void onNext(String item) {
                onNextCalled.set(true);
                assertEquals("first", item);
            }

            @Override
            public void onError(Throwable t) {
                onErrorCalled.set(true);
                assertEquals("Test failure", t.getMessage());
            }

            @Override
            public void onComplete() {
                onCompleteCalled.set(true);
            }
        });

        assertTrue(onNextCalled.get());
        assertTrue(onErrorCalled.get());
        assertFalse(onCompleteCalled.get());
    }
}

