package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Game;

import io.reactivex.Completable;

/**
 * Created by trevor on 1/30/17.
 */
public interface GameRepository {
    Completable persist(Game game);

    QueryBuilder queryBuilder();

    interface QueryBuilder {
        RepositoryQuery<Game> build();

        QueryBuilder withID(int id);
    }
}
