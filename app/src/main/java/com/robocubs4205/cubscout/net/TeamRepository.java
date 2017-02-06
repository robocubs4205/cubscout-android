package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.Match;
import com.robocubs4205.cubscout.Team;

import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by trevor on 1/30/17.
 */
public interface TeamRepository {
    Completable persist(Team... teams);

    Completable persist(Collection<Team> teams);

    Completable persistProperties(Team... teams);

    Completable persistProperties(Collection<Team> teams);

    Completable persistGameId(Team... teams);

    Completable persistGameId(Collection<Team> teams);

    QueryBuilder queryBuilder();

    interface QueryBuilder {
        Query build();

        QueryBuilder withId(int id);

        QueryBuilder inMatch(Match match);

        QueryBuilder inMatchWithId(int id);

        QueryBuilder inMatches(Match... matches);

        QueryBuilder inMatchesWithIds(int... ids);

        QueryBuilder inMatches(Collection<Match> matches);

        QueryBuilder inMatchesWithIds(Collection<Integer> ids);

        QueryBuilder inGame(Game game);

        QueryBuilder inGameWithId(int id);

        QueryBuilder inGames(Game... games);

        QueryBuilder inGamesWithIds(int... games);

        QueryBuilder inGames(Collection<Game> games);

        QueryBuilder inGamesWithIds(Collection<Integer> ids);
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
