package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Match;

import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by trevor on 1/29/17.
 */
public class FakeMatchRepository implements MatchRepository {
    final FakeRepositoryBacking backing;

    public FakeMatchRepository(FakeRepositoryBacking backing) {
        this.backing = backing;
    }

    @Override
    public Completable persist(Match match) {
        return Completable.fromAction(() -> backing.insertOrUpdate(match));
    }

    @Override
    public QueryBuilder queryBuilder() {
        return new QueryBuilderImpl();
    }

    private class QueryBuilderImpl implements QueryBuilder {
        QueryParameters parameters;

        @Override
        public RepositoryQuery<Match> build() {
            return new QueryImpl(parameters);
        }

        @Override
        public QueryBuilder withID(int id) {
            parameters.targetId = id;
            parameters.byId = true;
            return this;
        }
    }

    private class QueryParameters {
        int targetId;
        boolean byId = false;
    }

    private class QueryImpl implements RepositoryQuery<Match> {
        final QueryParameters parameters;

        private QueryImpl(QueryParameters parameters) {
            this.parameters = parameters;
        }

        @Override
        public Single<Collection<Match>> getResults() {
            return Observable.fromIterable(backing.getMatches())
                             .filter((match) -> !parameters.byId || parameters.targetId == match.id)
                             .toList().map((e) -> e);
        }
    }
}
