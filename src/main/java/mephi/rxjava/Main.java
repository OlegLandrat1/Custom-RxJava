package mephi.rxjava;

public class Main {
    public static void main(String[] args) {
        IOThreadScheduler io = new IOThreadScheduler();
        ComputationScheduler comp = new ComputationScheduler();

        System.out.println("=== Создаём Observable ===");
        Observable<Integer> observable = Observable.create(emitter -> {
            System.out.println("[emitter] onNext(10)");
            emitter.onNext(10);

            System.out.println("[emitter] onNext(0) — вызовет деление на 0");
            emitter.onNext(0); // вызовет ошибку

            System.out.println("[emitter] onNext(5)");
            emitter.onNext(5); // уже не сработает

            System.out.println("[emitter] onComplete()");
            emitter.onComplete();
        });

        System.out.println("=== Подключаем операторы ===");
        observable
                .map(x -> {
                    System.out.println("[map] input: " + x);
                    return 100 / x;
                })
                .subscribeOn(io)
                .observeOn(comp)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onNext(Integer item) {
                        System.out.println("[Observer] Received: " + item + " on " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.err.println("[Observer] Handled error: " + e.getMessage() +
                                " on " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("[Observer] Stream complete on " + Thread.currentThread().getName());
                    }
                });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("=== Завершаем Scheduler'ы ===");
        io.shutdown();
        comp.shutdown();
    }
}
