package io.github.pollinators.honeycomb.module;

import android.net.Uri;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.pollinators.honeycomb.MainActivity;
import io.github.pollinators.honeycomb.util.MediaFileStore;
import io.github.pollinators.honeycomb.fragment.QuestionFragment;
import io.github.pollinators.honeycomb.util.MediaUtils;

/**
 * Created by ted on 11/2/14.
 */
@Module(
        complete = false,
        library = true,
        injects = {
                MainActivity.class,
                QuestionFragment.class,
        }
)
public class MediaModule {

    @Provides @Singleton
    MediaFileStore provideMediaFileStore() {
        return new MediaFileStore() {
            @Override
            public Uri getImageFileUri() {
                return MediaUtils.getOutputMediaFileUri(MediaUtils.MEDIA_TYPE_IMAGE);
            }

            @Override
            public Uri getVideoFileUri() {
                return MediaUtils.getOutputMediaFileUri(MediaUtils.MEDIA_TYPE_VIDEO);
            }
        };
    }
}
