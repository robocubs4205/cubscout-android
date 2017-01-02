package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.net.CubscoutAPI;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by trevor on 1/1/17.
 */

public class StubCubscoutApi implements CubscoutAPI{

    @Override
    public Observable<GetEventsResponse> getCurrentEvents() {
        GetEventsResponse mockResponse = new GetEventsResponse();
        mockResponse.errors = new ArrayList<>();
        mockResponse.events = new ArrayList<>();
        Event event = new Event();
        event.id = 1;
        event.name = "Mock event";
        mockResponse.events.add(event);
        return Observable.just(mockResponse);
    }
}
