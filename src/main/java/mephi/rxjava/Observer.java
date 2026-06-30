// Observer.java
package mephi.rxjava;

/**
 * Интерфейс наблюдателя для обработки элементов потока.
 */
public interface Observer<T> {
    void onNext(T item);             // Получение следующего элемента потока
    void onError(Throwable t);       // Обработка ошибки
    void onComplete();               // Завершение потока
}

