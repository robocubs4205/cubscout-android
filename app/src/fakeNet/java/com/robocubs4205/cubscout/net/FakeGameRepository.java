package com.robocubs4205.cubscout.net;

import com.google.common.primitives.Ints;
import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.Game;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import io.reactivex.Completable;
import io.reactivex.Observable;

import static com.robocubs4205.cubscout.Event.EventInfo;
import static com.robocubs4205.cubscout.Game.GameBuilder;

/**
 * Created by trevor on 1/29/17.
 */
public class FakeGameRepository implements GameRepository {
    private final FakeRepositoryBacking backing;

    public FakeGameRepository(FakeRepositoryBacking backing) {
        this.backing = backing;
    }

    @Override
    public Completable persist(Game... games) {
        return Completable.fromAction(() -> backing.insertOrUpdate(games));
    }

    @Override
    public Completable persist(Collection<Game> games) {
        return Completable.fromAction(() -> backing.insertOrUpdate(games));
    }

    @Override
    public Completable persistProperties(Game... games) {
        return Completable.fromAction(() -> backing.insertOrUpdateProperties(games));
    }

    @Override
    public Completable persistProperties(Collection<Game> games) {
        return Completable.fromAction(() -> backing.insertOrUpdateProperties(games));
    }

    @Override
    public Completable persistName(Game... games) {
        return persistName(Arrays.asList(games));
    }

    @Override
    public Completable persistName(Collection<Game> games) {
        return Observable.fromIterable(backing.getGames()).filter(game -> {
            for (Game newGame : games) if (game.id == newGame.id) return true;
            return false;
        }).map((game -> {
            Game replacingGame = null;
            for (Game newGame : games) {
                if (game.id == newGame.id) replacingGame = newGame;
            }
            assert replacingGame != null;
            return new GameBuilder(game.id, replacingGame.name, game.type,
                                   game.year,
                                   game.scorecard).build();
        })).doOnNext((backing::insertOrUpdate)).ignoreElements();
    }

    @Override
    public Completable persistType(Game... games) {
        return persistType(Arrays.asList(games));
    }

    @Override
    public Completable persistType(Collection<Game> games) {
        return Observable.fromIterable(backing.getGames()).filter(game -> {
            for (Game newGame : games) if (game.id == newGame.id) return true;
            return false;
        }).map((game -> {
            Game replacingGame = null;
            for (Game newGame : games) {
                if (game.id == newGame.id) replacingGame = newGame;
            }
            assert replacingGame != null;
            return new GameBuilder(game.id, game.name, replacingGame.type,
                                   game.year,
                                   game.scorecard).build();
        })).doOnNext((backing::insertOrUpdate)).ignoreElements();
    }

    @Override
    public Completable persistYear(Game... games) {
        return persistYear(Arrays.asList(games));
    }

    @Override
    public Completable persistYear(Collection<Game> games) {
        return Observable.fromIterable(backing.getGames()).filter(game -> {
            for (Game newGame : games) if (game.id == newGame.id) return true;
            return false;
        }).map((game -> {
            Game replacingGame = null;
            for (Game newGame : games) {
                if (game.id == newGame.id) replacingGame = newGame;
            }
            assert replacingGame != null;
            return new GameBuilder(game.id, game.name, game.type,
                                   replacingGame.year,
                                   game.scorecard).build();
        })).doOnNext((backing::insertOrUpdate)).ignoreElements();
    }

    @Override
    public Completable persistScorecardId(Game... games) {
        //TODO
        throw new NotImplementedException("");
    }

    @Override
    public Completable persistScorecardId(Collection<Game> games) {
        //TODO
        throw new NotImplementedException("");
    }

    @Override
    public Completable persistEventIds(Game... games) {
        return persistEventIds(Arrays.asList(games));
    }

    @Override
    public Completable persistEventIds(Collection<Game> games) {
        return Completable.fromAction(() -> {
            Collection<Pair<Game, Game>> oldAndNewGames = new LinkedList<Pair<Game, Game>>();
            for (Game oldGame : backing.getGames()) {
                for (Game newGame : games) {
                    if (oldGame.id == newGame.id) oldAndNewGames.add(Pair.of(oldGame, newGame));
                }
            }
            Collection<Game> persistedGames = new LinkedList<>();
            for (Pair<Game, Game> oldAndNewGame : oldAndNewGames) {
                Game oldGame = oldAndNewGame.getLeft();
                Game newGame = oldAndNewGame.getRight();
                GameBuilder builder = new GameBuilder(oldGame.id, oldGame.name, oldGame.type,
                                                      oldGame.year, oldGame.scorecard);
                for (Event event : newGame.events) {
                    if (!backing.getEventMap().containsKey(event.id)) {
                        throw new IllegalStateException(
                                "new game's events do not exist in repository");
                    }
                    builder.addEvent(EventInfo.from(backing.getEventMap().get(event.id)));
                }
                persistedGames.add(builder.build());
            }
            backing.insertOrUpdate(persistedGames);
        });
    }

    @Override
    public QueryBuilder queryBuilder() {
        return new QueryBuilderImpl();
    }

    private class QueryBuilderImpl implements QueryBuilder {

        QueryParameters parameters = new QueryParameters();

        @Override
        public Query build() {
            return new QueryImpl(parameters);
        }

        @Override
        public QueryBuilder withId(int id) {
            parameters.targetIds.add(id);
            return this;
        }

        @Override
        public QueryBuilder withId(int... ids) {
            parameters.targetIds.addAll(Ints.asList(ids));
            return this;
        }

        @Override
        public QueryBuilder withId(Collection<Integer> ids) {
            parameters.targetIds.addAll(ids);
            return this;
        }

        @Override
        public QueryBuilder withName(String name) {
            parameters.targetNames.add(name);
            return this;
        }

        @Override
        public QueryBuilder withNames(String... names) {
            parameters.targetNames.addAll(Arrays.asList(names));
            return this;
        }

        @Override
        public QueryBuilder withNames(Collection<String> names) {
            parameters.targetNames.addAll(names);
            return this;
        }

        @Override
        public QueryBuilder withType(String type) {
            parameters.targetTypes.add(type);
            return this;
        }

        @Override
        public QueryBuilder withTypes(String... types) {
            parameters.targetTypes.addAll(Arrays.asList(types));
            return this;
        }

        @Override
        public QueryBuilder withTypes(Collection<String> types) {
            parameters.targetTypes.addAll(types);
            return this;
        }
    }

    private class QueryImpl implements Query {
        private final QueryParameters parameters;

        private QueryImpl(QueryParameters parameters) {
            this.parameters = parameters;
        }

        @Override
        public Observable<Game> get() {
            return Observable.fromIterable(backing.getGames())
                             .filter((game) -> parameters.targetIds.isEmpty() ||
                                     parameters.targetIds.contains(game.id))
                             .filter((game) -> parameters.targetNames.isEmpty() ||
                                     parameters.targetNames.contains(game.name))
                             .filter((game) -> parameters.targetTypes.isEmpty() ||
                                     parameters.targetTypes.contains(game.type));
        }

        @Override
        public Completable delete() {
            return get().doOnNext(backing::delete).ignoreElements();
        }
    }

    private class QueryParameters {
        private Collection<Integer> targetIds = Collections.synchronizedSet(new HashSet<>());
        private Collection<String> targetNames = Collections.synchronizedSet(new HashSet<>());
        private Collection<String> targetTypes = Collections.synchronizedSet(new HashSet<>());
    }
}
