package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Event;

import java.util.Date;

import io.reactivex.Completable;

/**
 * Created by trevor on 1/30/17.
 */

public interface EventRepository {
    Completable persist(Event event);

    QueryBuilder queryBuilder();

    Completable delete(RepositoryQuery<Event> events);

    interface QueryBuilder {
        RepositoryQuery<Event> build();

        QueryBuilder withID(int id);

        QueryBuilder ongoingDuring(Date date);
    }

}
