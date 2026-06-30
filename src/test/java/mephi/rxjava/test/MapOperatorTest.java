package mephi.rxjava.test;

import mephi.rxjava.Observable;
import mephi.rxjava.Observer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class MapOperatorTest {

    @Test
    void testMapOperator() {
        Observable<Integer> observable = Observable.create(emitter -> {
            emitter.onNext(2);
            emitter.onComplete();
        });

        AtomicReference<String> result = new AtomicReference<>("");

        observable
                .map(i -> "Number: " + (i * 5))
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
                        assertEquals("Number: 10", result.get());
                    }
                });
    }
}

