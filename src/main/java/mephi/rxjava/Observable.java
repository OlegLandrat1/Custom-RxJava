package mephi.rxjava;

import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Observable<T> {
    private final Consumer<Observer<T>> onSubscribe;
    private Executor subscribeExecutor = Runnable::run;
    private Executor observeExecutor = Runnable::run;

    private Observable(Consumer<Observer<T>> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public static <T> Observable<T> create(Consumer<Observer<T>> onSubscribe) {
        return new Observable<>(onSubscribe);
    }

    public <R> Observable<R> map(Function<T, R> mapper) {
        return create(observer -> subscribe(new Observer<T>() {
            @Override
            public void onNext(T item) {
                try {
                    R mapped = mapper.apply(item);
                    observer.onNext(mapped);
                } catch (Throwable t) {
                    observer.onError(t);
                }
            }

            @Override
            public void onError(Throwable t) {
                observer.onError(t);
            }

            @Override
            public void onComplete() {
                observer.onComplete();
            }
        }));
    }

    public Observable<T> filter(Predicate<T> predicate) {
        return create(observer -> subscribe(new Observer<T>() {
            @Override
            public void onNext(T item) {
                try {
                    if (predicate.test(item)) {
                        observer.onNext(item);
                    }
                } catch (Throwable t) {
                    observer.onError(t);
                }
            }

            @Override
            public void onError(Throwable t) {
                observer.onError(t);
            }

            @Override
            public void onComplete() {
                observer.onComplete();
            }
        }));
    }

    public <R> Observable<R> flatMap(Function<T, Observable<R>> mapper) {
        return create(observer -> subscribe(new Observer<T>() {
            @Override
            public void onNext(T item) {
                try {
                    mapper.apply(item).subscribe(observer);
                } catch (Throwable t) {
                    observer.onError(t);
                }
            }

            @Override
            public void onError(Throwable t) {
                observer.onError(t);
            }

            @Override
            public void onComplete() {
                observer.onComplete();
            }
        }));
    }

    public Observable<T> subscribeOn(Scheduler scheduler) {
        this.subscribeExecutor = scheduler::execute;
        return this;
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        this.observeExecutor = scheduler::execute;
        return this;
    }

    public void subscribe(Observer<T> observer) {
        subscribeExecutor.execute(() -> {
            final boolean[] terminated = {false};

            Observer<T> safeObserver = new Observer<>() {
                @Override
                public void onNext(T item) {
                    if (!terminated[0]) {
                        observeExecutor.execute(() -> observer.onNext(item));
                    }
                }

                @Override
                public void onError(Throwable t) {
                    if (!terminated[0]) {
                        terminated[0] = true;
                        observeExecutor.execute(() -> observer.onError(t));
                    }
                }

                @Override
                public void onComplete() {
                    if (!terminated[0]) {
                        terminated[0] = true;
                        observeExecutor.execute(observer::onComplete);
                    }
                }
            };

            onSubscribe.accept(safeObserver);
        });
    }
}
