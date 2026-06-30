package mephi.rxjava.test;

import mephi.rxjava.Observable;
import mephi.rxjava.Observer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class FilterOperatorTest {

    @Test
    void testFilterOperator() {
        AtomicBoolean itemReceived = new AtomicBoolean(false);

        Observable<Integer> observable = Observable.create(emitter -> {
            emitter.onNext(4);
            emitter.onNext(9);
            emitter.onNext(16);
            emitter.onComplete();
        });

        observable
                .filter(i -> i > 10)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onNext(Integer item) {
                        itemReceived.set(true);
                        assertEquals(16, item);
                    }

                    @Override
                    public void onError(Throwable t) {
                        fail("Unexpected error: " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        assertTrue(itemReceived.get(), "Expected at least one item to pass the filter");
                    }
                });
    }
}

