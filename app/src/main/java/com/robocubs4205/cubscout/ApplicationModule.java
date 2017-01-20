package com.robocubs4205.cubscout;

import dagger.Module;
import dagger.Provides;

/**
 * Created by trevor on 1/15/17.
 */
@Module
final class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {

        this.application = application;
    }

    @Provides
    public Application provideApplication() {
        return application;
    }
}
