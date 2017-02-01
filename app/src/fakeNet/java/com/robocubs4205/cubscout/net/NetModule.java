package com.robocubs4205.cubscout.net;

import android.content.Context;

import com.robocubs4205.cubscout.Application;
import com.robocubs4205.cubscout.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by trevor on 1/1/17.
 */

@Module
public class NetModule {
    public NetModule(Context context){

    }

    @Provides
    @ApplicationScope
    public CubscoutAPI provideCubscoutApi(Application application) {
        return new FakeCubscoutApi(application);
    }

    @Provides
    @ApplicationScope
    public FakeRepositoryBacking fakeRepositoryBacking() {
        return new FakeRepositoryBacking();
    }

    @Provides
    @ApplicationScope
    public FakeEventRepository eventRepository(FakeRepositoryBacking backing) {
        return new FakeEventRepository(backing);
    }

    @Provides
    @ApplicationScope
    public GameRepository gameRepository(FakeRepositoryBacking backing) {
        return new FakeGameRepository(backing);
    }

    @Provides
    @ApplicationScope
    public MatchRepository matchRepository(FakeRepositoryBacking backing) {
        return new FakeMatchRepository(backing);
    }

    @Provides
    @ApplicationScope
    public TeamRepository teamRepository(FakeRepositoryBacking backing) {
        return new FakeTeamRepository();
    }
}
