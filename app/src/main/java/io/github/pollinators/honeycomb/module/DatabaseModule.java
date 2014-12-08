package io.github.pollinators.honeycomb.module;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.pollinators.honeycomb.MainActivity;
import io.github.pollinators.honeycomb.data.ImageDataSource;
import io.github.pollinators.honeycomb.data.ResponseDataSource;
import io.github.pollinators.honeycomb.data.SurveySQLiteHelper;
import io.github.pollinators.honeycomb.fragment.QuestionFragment;
import io.github.pollinators.honeycomb.survey.Survey;
import io.github.pollinators.honeycomb.util.MediaFileStore;
import io.github.pollinators.honeycomb.util.MediaUtils;

/**
 * Created by ted on 11/2/14.
 */
@Module(
        complete = false,
        injects = {
                MainActivity.class,
                QuestionFragment.class,
        },
        addsTo = ActivityModule.class
)
public class DatabaseModule {

    @Provides
    SQLiteOpenHelper provideSQLiteOpenHelper(@ForActivity Context context) {
        return new SurveySQLiteHelper(context);
    }

    @Provides @Singleton
    ResponseDataSource provideResponseDataSource(SQLiteOpenHelper dbHelper, Survey survey) {
        return new ResponseDataSource(dbHelper, survey.getQuestionCount());
    }

    @Provides @Singleton
    ImageDataSource provideImageDataSource(SQLiteOpenHelper dbHelper, Survey survey) {
        return new ImageDataSource(dbHelper);
    }
}
