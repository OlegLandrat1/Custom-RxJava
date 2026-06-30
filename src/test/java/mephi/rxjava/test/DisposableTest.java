package mephi.rxjava.test;

import mephi.rxjava.Disposable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DisposableTest {

    private static class TestDisposable implements Disposable {
        private boolean disposed = false;

        @Override
        public void dispose() {
            disposed = true;
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }
    }

    @Test
    void testDisposable() {
        TestDisposable disposable = new TestDisposable();
        assertFalse(disposable.isDisposed(), "Initially should not be disposed");

        disposable.dispose();
        assertTrue(disposable.isDisposed(), "Should be disposed after calling dispose()");
    }
}

