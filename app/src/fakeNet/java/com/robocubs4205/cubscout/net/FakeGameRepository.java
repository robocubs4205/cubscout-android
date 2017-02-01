package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Game;

import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by trevor on 1/29/17.
 */
public class FakeGameRepository implements GameRepository {
    private final FakeRepositoryBacking backing;

    public FakeGameRepository(FakeRepositoryBacking backing) {
        this.backing = backing;
    }

    @Override
    public Completable persist(Game game) {
        return Completable.fromAction(() -> backing.insertOrUpdate(game));
    }

    @Override
    public QueryBuilder queryBuilder() {
        return new QueryBuilderImpl();
    }

    private class QueryBuilderImpl implements QueryBuilder {

        QueryParameters parameters = new QueryParameters();

        @Override
        public RepositoryQuery<Game> build() {
            return new QueryImpl(parameters);
        }

        @Override
        public QueryBuilder withID(int id) {
            parameters.targetId = id;
            parameters.byId = true;
            return this;
        }
    }

    private class QueryImpl implements RepositoryQuery<Game> {
        private final QueryParameters parameters;

        private QueryImpl(QueryParameters parameters) {
            this.parameters = parameters;
        }

        @Override
        public Single<Collection<Game>> getResults() {
            return Observable.fromIterable(backing.getGames())
                             .filter((game) -> parameters.byId || game.id == parameters.targetId)
                             .toList().map((e) -> e);
        }
    }

    private class QueryParameters {
        private int targetId;
        private boolean byId = false;
    }
}
