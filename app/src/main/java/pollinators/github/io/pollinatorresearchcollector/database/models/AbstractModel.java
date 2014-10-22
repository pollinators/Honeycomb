package pollinators.github.io.pollinatorresearchcollector.database.models;

/**
 * Created by ted on 10/18/14.
 */
public abstract class AbstractModel {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
