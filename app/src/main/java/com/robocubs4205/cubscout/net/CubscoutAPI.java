package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Event;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by trevor on 1/1/17.
 */
public interface CubscoutAPI {
    public Observable<GetEventsResponse> getCurrentEvents();


    class GetEventsResponse {
        public List<Error> errors;
        public List<Event> events;
    }
}
