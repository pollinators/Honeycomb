package io.github.pollinators.honeycomb.data;

import io.github.pollinators.honeycomb.data.models.MappingModel;
import io.github.pollinators.honeycomb.data.models.AbstractModel;

/**
 * Created by ted on 11/30/14.
 */
public interface CRUD<T> {

    public T create();
    public T get(long id);
    public void save(T object);
    public void delete(T object);

    public static interface ManyToMany<S extends AbstractModel, V extends AbstractModel, SV extends MappingModel<S, V>> {
        public SV create(S leftModel, V rightModel);
        public SV get(long leftId, long rightId);
        public void save(SV object);
        public void delete(SV object);
    }

}
