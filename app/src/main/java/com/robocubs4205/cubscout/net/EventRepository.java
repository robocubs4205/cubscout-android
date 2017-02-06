package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.Game;

import java.util.Collection;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by trevor on 1/30/17.
 */

public interface EventRepository {
    /**
     * persist all properties of the events and do the same to related objects.
     * Warning: extremely likely to fail due to conflicts. explicitly persisting changed properties
     * and changed related objects is recommended.
     *
     * @param events events to persist changes to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the events to be persisted
     */
    Completable persist(Event... events);

    /**
     * persist all properties of the events and do the same to related objects.
     * Warning: extremely likely to fail due to conflicts. explicitly persisting changed properties
     * and changed related objects is recommended.
     *
     * @param events events to persist changes to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the events to be persisted
     */
    Completable persist(Collection<Event> events);

    /**
     * persist all properties of the events.
     * Warning: likely to fail due to conflicts. explicitely persisting changed properties is
     * recommended. Only recommended for persisting newly created events.
     *
     * @param events events to persist changes to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the event to be persisted
     */
    Completable persistProperties(Event... events);

    /**
     * persist all properties of the events.
     * Warning: likely to fail due to conflicts. explicitely persisting changed properties is
     * recommended. Only recommended for persisting newly created events.
     *
     * @param events events to persist changes to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the event to be persisted
     */
    Completable persistProperties(Collection<Event> events);

    /**
     * persist changes to the events' name
     *
     * @param events events to persist changes to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the event to be persisted
     */
    Completable persistName(Event... events);

    /**
     * persist changes to the events' name
     *
     * @param events events to persist changes to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the event to be persisted
     */
    Completable persistName(Collection<Event> events);

    /**
     * persist changes to which games the events are associated with. Does not persist the game
     * itself. If the game is new or edited, it must be persisted explicitly. Will fail if the id
     * does not coorespond to an existing game.
     *
     * @param events events to persist changes to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the event to be persisted
     */
    Completable persistGameId(Event... events);

    /**
     * persist changes to which games the events are associated with. Does not persist the game
     * itself. If the game is new or edited, it must be persisted explicitly. Will fail if the id
     * does not coorespond to an existing game.
     *
     * @param events events to persist changes to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the event to be persisted
     */
    Completable persistGameId(Collection<Event> events);

    /**
     * persist changes to the start date of the events
     *
     * @param events events to persist change to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the event to be persisted
     */
    Completable persistStartDate(Event... events);

    /**
     * persist changes to the start date of the events
     *
     * @param events events to persist change to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the event to be persisted
     */
    Completable persistStartDate(Collection<Event> events);

    /**
     * persist changes to the end dates of the events
     *
     * @param events events to persist change to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the event to be persisted
     */
    Completable persistEndDate(Event... events);

    /**
     * persist changes to the end dates of the events
     *
     * @param events events to persist change to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the event to be persisted
     */
    Completable persistEndDate(Collection<Event> events);

    /**
     * persist changes to which matches are associated with the events. Does not persist changes to
     * the matches themselves. If the matches are new or edited, they must be persisted explicitely.
     * Will fail if an id does not coorespond to an existing match.
     *
     * @param events events to persist change to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the event to be persisted
     */
    Completable persistMatchIds(Event... events);

    /**
     * persist changes to which matches are associated with the events. Does not persist changes to
     * the matches themselves. If the matches are new or edited, they must be persisted explicitely. Will fail
     * if an id does not coorespond to an existing match.
     *
     * @param events events to persist change to
     * @return {@link Completable} to notify caller when persisting is done. caller must call
     * subscribe in order for the event to be persisted
     */
    Completable persistMatchIds(Collection<Event> events);

    /**
     * delete events
     *
     * @param events events to delete
     * @return {@link Completable} to notify caller when deletion is done. caller must call
     * subscribe in order for the event to be deleted
     */
    Completable delete(Event... events);

    /**
     * delete events
     *
     * @param events events to delete
     * @return {@link Completable} to notify caller when deletion is done. caller must call
     * subscribe in order for the event to be deleted
     */
    Completable delete(Collection<Event> events);

    /**
     * create a query builder to get or delete events based on a set of criteria
     *
     * @return {@link QueryBuilder} to create a query
     */
    QueryBuilder queryBuilder();

    interface QueryBuilder {
        /**
         * Create a query that returns all events that match all conditions added to the builder.
         *
         * @return {@link RepositoryQuery} that returns all events that match all conditions added
         * to the builder.
         */
        Query build();

        /**
         * filter events by id. if specified multiple times or in combination with
         * {@link #withIds(int...)} or {@link #withIds(Collection)}, events matching any id given
         * to any of the three will be included
         *
         * @param id id to filter by
         * @return this {@link QueryBuilder}
         */
        QueryBuilder withId(int id);

        /**
         * filter events by id. if specified multiple times or in combination  with
         * {@link #withId(int)} or {@link #withIds(Collection)}, events matching any id given to
         * any of the three will be included
         *
         * @param ids ids to filter by
         * @return this {@link QueryBuilder}
         */
        QueryBuilder withIds(int... ids);

        /**
         * filter events by id. if specified multiple times or in combination with
         * {@link #withId(int)} or {@link #withIds(int...)}, events matching any id given to any of
         * the three will be included
         *
         * @param ids ids to filter by
         * @return this {@link QueryBuilder}
         */
        QueryBuilder withIds(Collection<Integer> ids);

        /**
         * filter events by name. if specified multiple times or in combination with
         * {@link #withNames(Collection)} or {@link #withNames(String...)}, events matching any
         * name given to any of the three will be included
         *
         * @param name name to filter by
         * @return this {@link QueryBuilder}
         */
        QueryBuilder withName(String name);

        /**
         * filter events by name. if specified multiple times or in combination with
         * {@link #withNames(Collection)} or {@link #withName(String)}, events matching any
         * name given to any of the three will be included
         *
         * @param names name to filter by
         * @return this {@link QueryBuilder}
         */
        QueryBuilder withNames(String... names);

        /**
         * filter events by name. if specified multiple times or in combination with
         * {@link #withName(String)} or {@link #withNames(String...)}, events matching any
         * name given to any of the three will be included
         *
         * @param names name to filter by
         * @return this {@link QueryBuilder}
         */
        QueryBuilder withNames(Collection<String> names);

        /**
         * filter events by which game they are associated with. If specified multiple times or in
         * combination with {@link #withGames(Collection)}, {@link #withGames(Game...)},
         * {@link #withGameID(int)}, or {@link #withGameIDs(Collection)}, events associated with
         * games provided to any of the preceding functions will be matched
         *
         * @param game game to filter by
         * @return this {@link QueryBuilder}
         */
        QueryBuilder withGame(Game game);

        /**
         * filter events by which game they are associated with. If specified multiple times or in
         * combination with {@link #withGame(Game)}, {@link #withGames(Collection)},
         * {@link #withGameID(int)},  or {@link #withGameIDs(Collection)}, events associated with
         * games provided to any of the preceding functions will be matched
         *
         * @param games games to filter by
         * @return this {@link QueryBuilder}
         */
        QueryBuilder withGames(Game... games);

        /**
         * filter events by which game they are associated with. If specified multiple times or in
         * combination with {@link #withGame(Game)}, {@link #withGames(Game...)},
         * {@link #withGameID(int)}, or {@link #withGameIDs(Collection)}, events associated with
         * games provided to any of the preceding functions will be matched
         *
         * @param games games to filter by
         * @return this {@link QueryBuilder}
         */
        QueryBuilder withGames(Collection<Game> games);

        /**
         * filter events by which game they are associated with. If specified multiple times or in
         * combination with {@link #withGame(Game)}, {@link #withGames(Collection)},
         * {@link #withGames(Game...)}, or {@link #withGameIDs(Collection)}, events associated with
         * games provided to any of the preceding functions will be matched
         *
         * @param id id of game to filter by
         * @return this {@link QueryBuilder}
         */
        QueryBuilder withGameID(int id);

        /**
         * filter events by which game they are associated with. If specified multiple times or in
         * combination with {@link #withGame(Game)}, {@link #withGames(Collection)},
         * {@link #withGames(Game...)}, or {@link #withGameIDs(Collection)}, events associated with
         * games provided to any of the preceding functions will be matched
         *
         * @param ids ids of games to filter by
         * @return this {@link QueryBuilder}
         */
        QueryBuilder withGameIDs(int... ids);

        /**
         * filter events by which game they are associated with. If specified multiple times or in
         * combination with {@link #withGame(Game)}, {@link #withGames(Collection)},
         * {@link #withGames(Game...)}, or {@link #withGameID(int)}, events associated with games
         * provided to any of the preceding functions will be matched
         *
         * @param ids ids of games to filter by
         * @return this {@link QueryBuilder}
         */
        QueryBuilder withGameIDs(Collection<Integer> ids);

        QueryBuilder ongoingDuring(Date date);

        QueryBuilder startsBefore(Date date);

        QueryBuilder startsOn(Date date);

        QueryBuilder startsAfter(Date date);

        QueryBuilder endsBefore(Date date);

        QueryBuilder endsOn(Date date);

        QueryBuilder endsAfter(Date date);

        /**
         * creates a {@link MatchRepository.QueryBuilder} for matches that is limited to matches
         * for events from this {@link QueryBuilder}.
         *
         * @return {@link MatchRepository.QueryBuilder} for matches
         */
        MatchRepository.QueryBuilder matches();
    }

    interface Query {
        /**
         * return the matching events.
         * calling after calling delete returns an empty stream. calling delete before the stream
         * finishes is undefined behavior
         *
         * @return {@link Observable}stream of the matching events
         */
        Observable<Event> get();

        /**
         * delete all matching events
         *
         * @return {@link Completable} to notify caller when deletion is complete. caller must call
         * {@link Completable#subscribe()} in order for deletion to happen
         */
        Completable delete();
    }
}
