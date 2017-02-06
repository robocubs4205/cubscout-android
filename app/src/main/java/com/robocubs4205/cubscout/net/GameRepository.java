package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Game;

import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by trevor on 1/30/17.
 */
public interface GameRepository {
    /**
     * persist all properties of the games and do the same to related objects.
     * Warning: extremely likely to fail due to conflicts. explicitly persisting changed properties
     * and changed related objects is recommended.
     *
     * @param games games to persist changes to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the events to be persisted
     */
    Completable persist(Game... games);

    /**
     * persist all properties of the games and do the same to related objects.
     * Warning: extremely likely to fail due to conflicts. explicitly persisting changed properties
     * and changed related objects is recommended.
     *
     * @param games games to persist changes to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the events to be persisted
     */
    Completable persist(Collection<Game> games);

    /**
     * persist all properties of the games.
     * Warning: extremely likely to fail due to conflicts. explicitly persisting changed properties
     * and changed related objects is recommended.
     *
     * @param games games to persist changes to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the events to be persisted
     */
    Completable persistProperties(Game... games);

    /**
     * persist all properties of the games.
     * Warning: extremely likely to fail due to conflicts. explicitly persisting changed properties
     * and changed related objects is recommended.
     *
     * @param games games to persist changes to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the events to be persisted
     */
    Completable persistProperties(Collection<Game> games);

    Completable persistName(Game... games);

    Completable persistName(Collection<Game> games);

    Completable persistType(Game... games);

    Completable persistType(Collection<Game> games);

    Completable persistYear(Game... games);

    Completable persistYear(Collection<Game> games);

    Completable persistScorecardId(Game... games);

    Completable persistScorecardId(Collection<Game> games);

    Completable persistEventIds(Game... games);

    Completable persistEventIds(Collection<Game> games);

    QueryBuilder queryBuilder();

    interface QueryBuilder {
        Query build();

        QueryBuilder withId(int id);

        QueryBuilder withId(int... ids);

        QueryBuilder withId(Collection<Integer> ids);

        QueryBuilder withName(String name);

        QueryBuilder withNames(String... names);

        QueryBuilder withNames(Collection<String> names);

        QueryBuilder withType(String type);

        QueryBuilder withTypes(String... types);

        QueryBuilder withTypes(Collection<String> types);


    }

    interface Query {
        /**
         * return the matching games.
         * calling after calling delete returns an empty stream. calling delete after calling get
         * but before the stream finishes is undefined behavior
         *
         * @return {@link Observable}stream of the matching events
         */
        Observable<Game> get();

        /**
         * delete all matching games
         *
         * @return {@link Completable} to notify caller when deletion is complete. caller must call
         * {@link Completable#subscribe()} in order for deletion to happen
         */
        Completable delete();
    }
}
