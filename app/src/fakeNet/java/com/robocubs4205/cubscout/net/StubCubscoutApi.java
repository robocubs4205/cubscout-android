package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.Scorecard;

import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by trevor on 1/1/17.
 */

public class StubCubscoutApi implements CubscoutAPI{

    @Override
    public Observable<GetEventsResponse> getCurrentEvents() {
        GetEventsResponse stubResponse = new GetEventsResponse();
        stubResponse.errors = new ArrayList<>();
        stubResponse.eventEntities = new ArrayList<>();
        Event event = new Event(1, "Mock event");
        stubResponse.eventEntities.add(event);
        return Observable.just(stubResponse);
    }

    @Override
    public Observable<GetGamesResponse> getAllGames() {
        GetGamesResponse stubResponse = new GetGamesResponse();
        stubResponse.errors = new ArrayList<>();
        stubResponse.gameEntities = new ArrayList<>();
        stubResponse.gameEntities.add(new Game(1, "StrongHold", "FRC", 2016));
        stubResponse.gameEntities.add(new Game(2, "Recycle Rush", "FRC", 2015));
        stubResponse.gameEntities.add(new Game(3, "Velocity Vortex", "FTC", 2016));
        return Observable.just(stubResponse);
        //RuntimeException result = new RuntimeException();
        //return Observable.error(result);
    }

    @Override
    public Observable<GetEventsResponse> getAllEvents() {
        throw new NotImplementedException("");
    }

    @Override
    public Completable submitMatch(Integer teamNumber, Integer matchNumber,
                                   Scorecard currentScorecard, Collection<FieldScore> values) {
        return Completable.complete();
    }
}
