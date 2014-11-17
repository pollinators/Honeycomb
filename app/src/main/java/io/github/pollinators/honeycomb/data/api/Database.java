package io.github.pollinators.honeycomb.data.api;

/**
 * Created by ted on 11/2/14.
 */
public interface Database {

    public Object get(Object o, Object key);
    public void put(Object key, Object value);
}
