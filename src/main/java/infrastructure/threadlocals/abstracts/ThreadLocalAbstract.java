package infrastructure.threadlocals.abstracts;

import lombok.Getter;

@Getter
public abstract class ThreadLocalAbstract<T> {

    private final String description;
    private final ThreadLocal<T> threadLocal;
    private final T initValue;

    private ThreadLocalAbstract(String description, ThreadLocal<T> threadLocal, T initValue) {
        this.description = description;
        this.threadLocal = threadLocal;
        this.initValue = initValue;
    }

    public ThreadLocalAbstract(String description) {
        this(description, new ThreadLocal<>(), null);
    }

    public void put(T value) {
        this.threadLocal.set(value);
    }

    public T get() {
        T value = threadLocal.get();

        if (value == null && initValue != null) {
            put(initValue);
            value = initValue;
        }

        return value;
    }

    public void clear() {
        threadLocal.remove();
    }

    @Override
    public String toString() {
        T value = this.threadLocal.get();

        String valueStr =
                value == null
                        ? "undefined"
                        : value.toString();

        return String.format("%s as \"%s\". Thread: %d", this.description.toUpperCase(), valueStr, Thread.currentThread().getId());
    }

}
