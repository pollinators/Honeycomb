package io.github.pollinators.honeycomb.data.api;

/**
 * Created by ted on 11/3/14.
 */
public interface DataView<T> {

    void setData(T data);
    T getData();
    void setNullable(boolean isNullable);
}
