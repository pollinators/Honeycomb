package io.github.pollinators.honeycomb.module;

import android.content.res.Resources;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.pollinators.honeycomb.MainActivity;
import io.github.pollinators.honeycomb.fragment.NavigationDrawerFragment;
import io.github.pollinators.honeycomb.fragment.QuestionFragment;
import io.github.pollinators.honeycomb.survey.MedoraSurvey;
import io.github.pollinators.honeycomb.survey.Survey;

/**
 * Created by ted on 11/2/14.
 */
@Module(
        complete = false,
        library = true,
        injects = {
                MainActivity.class,
                QuestionFragment.class,
                NavigationDrawerFragment.class
        }
)
public class QuestionModule {

    @Provides @Singleton Survey provideSurvey(Resources resources) {
        return new MedoraSurvey(resources);
    }
}
