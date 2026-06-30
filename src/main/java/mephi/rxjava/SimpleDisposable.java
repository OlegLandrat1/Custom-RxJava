// SimpleDisposable.java
package mephi.rxjava;

public class SimpleDisposable implements Disposable {
    private volatile boolean disposed = false;

    @Override
    public void dispose() {
        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }
}
