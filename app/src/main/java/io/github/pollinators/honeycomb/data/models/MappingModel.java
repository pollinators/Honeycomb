package io.github.pollinators.honeycomb.data.models;

/**
 * Created by ted on 11/30/14.
 */
public class MappingModel<S extends AbstractModel, V extends AbstractModel> {

    private S leftModel;
    private V rightModel;

    public MappingModel(S leftModel, V rightModel) {
        this.leftModel = leftModel;
        this.rightModel = rightModel;
    }

    public S getLeftModel() {
        return leftModel;
    }

    public V getRightModel() {
        return rightModel;
    }

    public void setLeftModel(S leftModel) {
        this.leftModel = leftModel;
    }

    public void setRightModel(V rightModel) {
        this.rightModel = rightModel;
    }
}
