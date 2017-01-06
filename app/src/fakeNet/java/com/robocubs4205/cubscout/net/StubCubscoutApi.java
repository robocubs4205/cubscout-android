package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.net.CubscoutAPI;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by trevor on 1/1/17.
 */

public class StubCubscoutApi implements CubscoutAPI{

    @Override
    public Observable<GetEventsResponse> getCurrentEvents() {
        GetEventsResponse stubResponse = new GetEventsResponse();
        stubResponse.errors = new ArrayList<>();
        stubResponse.events = new ArrayList<>();
        Event event = new Event();
        event.id = 1;
        event.name = "Mock event";
        stubResponse.events.add(event);
        return Observable.just(stubResponse);
    }

    @Override
    public Observable<GetGamesResponse> getAllGames() {
        GetGamesResponse stubResponse = new GetGamesResponse();
        stubResponse.errors = new ArrayList<>();
        stubResponse.games = new ArrayList<>();
        stubResponse.games.add(new Game(1, "StrongHold", "FRC", 2016));
        stubResponse.games.add(new Game(2, "Recycle Rush", "FRC", 2015));
        stubResponse.games.add(new Game(3, "Velocity Vortex", "FTC", 2016));
        return Observable.just(stubResponse);
        //RuntimeException result = new RuntimeException();
        //return Observable.error(result);
    }
}
