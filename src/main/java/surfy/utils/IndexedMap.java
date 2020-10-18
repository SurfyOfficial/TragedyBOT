package surfy.utils;

import java.util.function.Function;

public class IndexedMap<T> {
    private int index;
    private T value;

    IndexedMap(int index, T value) {
        this.index = index;
        this.value = value;
    }

    public int index() {
        return index;
    }

    public T value() {
        return value;
    }

    @Override
    public String toString() {
        return value + "(" + index + ")";
    }

    public static <T> Function<T, IndexedMap<T>> indexed() {
        return new Function<T, IndexedMap<T>>() {
            int index = 0;
            @Override
            public IndexedMap<T> apply(T t) {
                return new IndexedMap<>(index++, t);
            }
        };
    }
}