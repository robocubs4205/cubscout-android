package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Match;

import io.reactivex.Completable;

/**
 * Created by trevor on 1/30/17.
 */
public interface MatchRepository {
    Completable persist(Match match);

    QueryBuilder queryBuilder();

    interface QueryBuilder {
        RepositoryQuery<Match> build();

        QueryBuilder withID(int id);
    }
}
