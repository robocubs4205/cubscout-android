package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.Scorecard;
import com.robocubs4205.cubscout.scorelist.Result;

import java.util.Collection;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by trevor on 1/1/17.
 */
public interface CubscoutAPI {
    Observable<GetEventsResponse> getCurrentEvents();

    Observable<GetGamesResponse> getAllGames();

    Observable<GetEventsResponse> getAllEvents();

    Completable submitMatch(Integer teamNumber, Integer matchNumber, Scorecard scorecard,
                            Collection<FieldScore> values);

    Observable<List<Result>> getResults(Scorecard scorecard, String[] orderBy);

    class GetEventsResponse {
        public List<Error> errors;
        public List<Event> eventEntities;
    }

    class GetGamesResponse {
        public List<Error> errors;
        public List<Game> gameEntities;
    }
}
