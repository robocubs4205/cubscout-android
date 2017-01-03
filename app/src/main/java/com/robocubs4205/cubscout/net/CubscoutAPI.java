package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.Game;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by trevor on 1/1/17.
 */
public interface CubscoutAPI {
    public Observable<GetEventsResponse> getCurrentEvents();
    public Observable<GetGamesResponse> getAllGames();

    class GetEventsResponse {
        public List<Error> errors;
        public List<Event> events;
    }

    class GetGamesResponse {
        public List<Error> errors;
        public List<Game> games;
    }
}
