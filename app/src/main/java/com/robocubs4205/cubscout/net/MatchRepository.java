package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Match;
import com.robocubs4205.cubscout.Team;

import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by trevor on 1/30/17.
 */
public interface MatchRepository {
    Completable persist(Match... matches);

    Completable persist(Collection<Match> matches);

    Completable persistProperties(Match... matches);

    Completable persistProperties(Collection<Match> matches);

    Completable persistEventId(Match... matches);

    Completable persistEventId(Collection<Match> matches);

    QueryBuilder queryBuilder();

    interface QueryBuilder {
        Query build();

        QueryBuilder withId(int id);

        QueryBuilder withIds(int... ids);

        QueryBuilder withIds(Collection<Integer> ids);

        QueryBuilder hasTeam(Team team);

        QueryBuilder hasTeamWithId(int id);

        QueryBuilder hasTeams(Team... teams);

        QueryBuilder hasTeamsWithIds(int... ids);

        QueryBuilder hasTeams(Collection<Team> teams);

        QueryBuilder hasTeamsWithIds(Collection<Integer> ids);
    }

    interface Query {
        /**
         * return the matching matches.
         * calling after calling delete returns an empty stream. calling delete after calling get
         * but before the stream finishes is undefined behavior
         *
         * @return {@link Observable}stream of the matching events
         */
        Observable<Match> get();

        /**
         * delete all matching matches
         *
         * @return {@link Completable} to notify caller when deletion is complete. caller must call
         * {@link Completable#subscribe()} in order for deletion to happen
         */
        Completable delete();
    }
}
