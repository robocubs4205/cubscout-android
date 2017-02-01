package com.robocubs4205.cubscout.net;

import java.util.Collection;

import io.reactivex.Single;

/**
 * Created by trevor on 1/30/17.
 */
public interface RepositoryQuery<T> {
    Single<Collection<T>> getResults();
}
