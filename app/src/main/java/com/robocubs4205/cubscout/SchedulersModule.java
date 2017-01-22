package com.robocubs4205.cubscout;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by trevor on 1/21/17.
 */

@Module
class SchedulersModule {

    static final String IO_SCHEDULER = "IoScheduler";
    static final String FOREGROUND_SCHEDULER = "ForegroundScheduler";
    static final String COMPUTATION_SCHEDULER = "ComputationScheduler";

    @Provides
    @Named(IO_SCHEDULER)
    Scheduler provideIOScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Named(FOREGROUND_SCHEDULER)
    Scheduler provideForegroundScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Named(COMPUTATION_SCHEDULER)
    Scheduler provideComputationScheduler() {
        return Schedulers.computation();
    }
}
