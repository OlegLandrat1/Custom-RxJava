package mephi.rxjava.test;

import mephi.rxjava.Observer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverTest {

    private static class TestObserver<T> implements Observer<T> {
        private final List<T> receivedItems = new ArrayList<>();
        private Throwable error;
        private boolean completed = false;

        @Override
        public void onNext(T item) {
            receivedItems.add(item);
        }

        @Override
        public void onError(Throwable t) {
            this.error = t;
        }

        @Override
        public void onComplete() {
            completed = true;
        }

        public List<T> getReceivedItems() {
            return receivedItems;
        }

        public Throwable getError() {
            return error;
        }

        public boolean isCompleted() {
            return completed;
        }
    }

    @Test
    void testObserverBehavior() {
        TestObserver<String> observer = new TestObserver<>();

        observer.onNext("one");
        observer.onNext("two");
        observer.onComplete();

        assertEquals(2, observer.getReceivedItems().size());
        assertTrue(observer.getReceivedItems().contains("one"));
        assertTrue(observer.getReceivedItems().contains("two"));
        assertTrue(observer.isCompleted());
        assertNull(observer.getError());
    }

    @Test
    void testObserverError() {
        TestObserver<String> observer = new TestObserver<>();
        RuntimeException exception = new RuntimeException("Test error");

        observer.onError(exception);

        assertEquals(exception, observer.getError());
        assertFalse(observer.isCompleted());
    }
}
