package io.github.pollinators.honeycomb.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;

import io.github.pollinators.honeycomb.data.models.MappingModel;
import io.github.pollinators.honeycomb.data.models.AbstractModel;

/**
 * Created by ted on 11/30/14.
 */
public class DataMappingSource<L extends AbstractModel, R extends AbstractModel> extends DatabaseSource
        implements CRUD.ManyToMany<L, R, MappingModel<L, R>>
{

    AbstractDataSource<L> leftDataSource;
    AbstractDataSource<R> rightDataSource;

    String leftIdColumn;
    String rightIdColumn;
    String tableName;

    public DataMappingSource(SQLiteOpenHelper dbHelper,
                             AbstractDataSource<L> leftDataSource,
                             AbstractDataSource<R> rightDataSource)
    {
        super(dbHelper);
        this.leftDataSource = leftDataSource;
        this.rightDataSource = rightDataSource;
        this.leftIdColumn = leftDataSource.getTableName() + SurveySQLiteHelper.COLUMN_ID;
        this.rightIdColumn = rightDataSource.getTableName() + SurveySQLiteHelper.COLUMN_ID;
        this.tableName = String.format(SurveySQLiteHelper.TABLE_FORMAT, leftDataSource.getTableName(), rightDataSource.getTableName());
    }

    @Override
    public MappingModel<L, R> create(L leftModel, R rightModel) {
        ContentValues values = new ContentValues(2);
        values.put(leftIdColumn, leftModel.getId());
        values.put(rightIdColumn, rightModel.getId());
        database.insert(SurveySQLiteHelper.TABLE_RESPONSE_DATA_IMAGES, null, values);
        return new MappingModel<L, R>(leftModel, rightModel);
    }

    @Override
    public MappingModel<L, R> get(long leftModelId, long rightModelId) {
        return new MappingModel(leftDataSource.get(leftModelId), rightDataSource.get(rightModelId));
    }

    @Override
    public void save(MappingModel<L, R> mappingModel) {
        leftDataSource.save(mappingModel.getLeftModel());
        rightDataSource.save(mappingModel.getRightModel());
    }

    @Override
    /**
     * This still leaves the issue that if either is deleted from the individual data sources, this
     * row will remain. A callback on delete for the two model data sources could help
     */
    public void delete(MappingModel<L, R> mappingModel) {
        delete(mappingModel.getLeftModel().getId(), mappingModel.getRightModel().getId());
    }

    public void delete(long leftId, long rightId) {
        if (leftId >= 0) {
            L model = leftDataSource.get(leftId);
            if (model != null) {
                leftDataSource.delete(model);
            }

            database.delete(tableName, leftIdColumn + " = " + leftId, null);
        }
        if (rightId >= 0) {
            R model = rightDataSource.get(rightId);
            if (model != null) {
                rightDataSource.delete(rightDataSource.get(rightId));
            }

            database.delete(tableName, rightIdColumn + " = " + rightId, null);
        }
    }
}
