package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Team;

import io.reactivex.Completable;

/**
 * Created by trevor on 1/30/17.
 */
public interface TeamRepository {
    Completable persist(Team team);

    QueryBuilder queryBuilder();

    interface QueryBuilder {
        RepositoryQuery<Team> build();

        QueryBuilder withID(int id);
    }
}
