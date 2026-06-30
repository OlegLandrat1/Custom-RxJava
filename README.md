# SF\_RxJava

## Описание проекта

Реализация кастомной библиотеки RxJava.
В библиотеке используются основные концепции реактивного программирования
Реализована система реактивных потоков с возможностью управления потоками выполнения (Schedulers) и 
обработки событий с использованием паттерна «Наблюдатель» (Observer pattern).

## Основные функции

* Поддержка операторов `map`, `filter`, `flatMap` для потоков данных;
* Управление потоками через `subscribeOn` и `observeOn`;
* Пользовательская реализация `Scheduler` и планировщиков: IO, computation, single;
* Интерфейс подписчика `Observer` и управления подпиской `Disposable`

## Установка и запуск

1. Клонировать репозиторий:

   ```bash
   git clone https://github.com/example-user/SF_RxJava.git
   cd SF_RxJava
   ```
2. Собрать проект:

   ```bash
   mvn clean install
   ```
3. Запустить проект:

   ```bash
   mvn exec:java -Dexec.mainClass="mephi.rxjava.Main"
   ```

## Структура проекта

```plaintext
SF_RxJava/
├── src/
│   ├── main/java/mephi/rxjava/
│   │   ├── Observable.java
│   │   ├── Observer.java
│   │   ├── Disposable.java
│   │   ├── SimpleDisposable.java
│   │   ├── Scheduler.java
│   │   ├── IOThreadScheduler.java
│   │   ├── ComputationScheduler.java
│   │   ├── SingleThreadScheduler.java
│   │   ├── Main.java
│   └── test/java/mephi/rxjava/test/
│       ├── ObservableTest.java
│       ├── MapOperatorTest.java
│       ├── FilterOperatorTest.java
│       ├── FlatMapOperatorTest.java
│       ├── SubscribeOnObserveOnTest.java
│       ├── SchedulerTest.java
│       ├── DisposableTest.java
│       ├── ObserverTest.java
│       └── ErrorHandlingTest.java
```

## Тесты (JUnit 5)

* Правильная трансформация данных (`map`, `filter`)
* Слияние потоков (`flatMap`)
* Асинхронность и распределение по потокам (`subscribeOn`, `observeOn`)
* Обработка ошибок (`onError`)
* Завершение (`onComplete`)
* Проверка работоспособности `Disposable`
* Симуляция исключений в операторах (`ArithmeticException` в `map`)

Запуск тестов `mvn test`.

### Архитектура

* `Observable<T>` — создаёт поток событий и управляет цепочкой операторов.
* `Observer<T>` — подписчик, получающий `onNext`, `onError`, `onComplete`.
* `Scheduler` — абстракция для планирования выполнения задач (`execute(Runnable)`).
* Три реализации планировщика:

  * `IOThreadScheduler` — CachedThreadPool
  * `ComputationScheduler` — FixedThreadPool
  * `SingleThreadScheduler` — SingleThreadExecutor

Каждый оператор (`map`, `filter`, `flatMap`) возвращает новый `Observable` и не нарушает чистоту потока. 
Методы `subscribeOn` и `observeOn` обеспечивают смену потока исполнения.

### Schedulers

```java
public interface Scheduler {
    void execute(Runnable task);
}
```

* **IOThreadScheduler**: используется для неблокирующих операций, многопоточная очередь.
* **ComputationScheduler**: используется для CPU-bound операций.
* **SingleThreadScheduler**: линейное исполнение задач в одном потоке.

Каждый Scheduler реализует стратегию исполнения с помощью стандартного пула потоков Java.

### Пример использования

```java
Observable<Integer> observable = Observable.create(emitter -> {
    emitter.onNext(10);
    emitter.onNext(0); 
    emitter.onNext(5);
    emitter.onComplete();
});

observable
    .map(x -> 100 / x)
    .subscribeOn(new IOThreadScheduler())
    .observeOn(new ComputationScheduler())
    .subscribe(new Observer<Integer>() {
        @Override
        public void onNext(Integer item) {
            System.out.println("Received: " + item);
        }

        @Override
        public void onError(Throwable e) {
            System.err.println("Handled error: " + e.getMessage());
        }

        @Override
        public void onComplete() {
            System.out.println("Stream complete");
        }
    });