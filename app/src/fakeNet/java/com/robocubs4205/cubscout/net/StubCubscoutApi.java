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
        Game game = new Game();
        game.id = 1;
        game.name = "StrongHold";
        game.type = "FRC";
        game.year = 2016;
        stubResponse.games.add(game);
        return Observable.just(stubResponse);
    }
}
