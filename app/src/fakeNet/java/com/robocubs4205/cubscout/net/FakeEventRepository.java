package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Event;

import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by trevor on 1/29/17.
 */

public class FakeEventRepository implements EventRepository {

    private final FakeRepositoryBacking backing;

    public FakeEventRepository(FakeRepositoryBacking backing) {

        this.backing = backing;
    }

    @Override
    public Completable persist(Event event) {
        return Completable.fromAction(() -> backing.insertOrUpdate(event));
    }

    @Override
    public QueryBuilder queryBuilder() {
        //TODO
        throw new NotImplementedException("");
    }

    @Override
    public Completable delete(RepositoryQuery<Event> events) {
        //TODO
        throw new NotImplementedException("");
    }

    private class QueryBuilderImpl implements QueryBuilder {

        private boolean hasClauses = false;
        private QueryParameters parameters = new QueryParameters();

        @Override
        public QueryImpl build() {
            QueryImpl query = new QueryImpl();
            query.parameters = parameters;
            return query;
        }

        @Override
        public QueryBuilder withID(int id) {
            parameters.targetId = id;
            parameters.byId = true;
            hasClauses = true;
            return this;
        }

        @Override
        public QueryBuilder ongoingDuring(Date date) {
            parameters.duringDate = date;
            parameters.ongoingDuringDate = true;
            hasClauses = true;
            return this;
        }
    }

    private class QueryImpl implements RepositoryQuery {
        private QueryParameters parameters;

        @Override
        public Single<Collection<Event>> getResults() {
            return Observable.fromIterable(backing.getEvents())
                             .filter((event) -> !parameters.byId || parameters.targetId == event.id)
                             .filter((event) -> !parameters.ongoingDuringDate ||
                                     (event.startDate.before(parameters.duringDate) &&
                                             event.endDate.after(parameters.duringDate)))
                             .toList().map((e) -> e);
        }
    }

    private class QueryParameters {
        private int targetId;
        private boolean byId = false;
        private Date duringDate;
        private boolean ongoingDuringDate;
    }
}
