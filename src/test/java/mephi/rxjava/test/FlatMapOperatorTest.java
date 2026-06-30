package mephi.rxjava.test;

import mephi.rxjava.Observable;
import mephi.rxjava.Observer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FlatMapOperatorTest {

    @Test
    void testFlatMapOperator() {
        Observable<Integer> observable = Observable.create((Observer<Integer> emitter) -> {
            emitter.onNext(1);
            emitter.onComplete();
        });

        AtomicReference<String> result = new AtomicReference<>("");

        observable.flatMap(i -> Observable.create((Observer<String> inner) -> {
                    inner.onNext("Item " + i);
                    inner.onComplete();
                }))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String item) {
                        result.set(item);
                    }

                    @Override
                    public void onError(Throwable t) {
                        fail("Unexpected error: " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        assertEquals("Item 1", result.get());
                    }
                });
    }
}
