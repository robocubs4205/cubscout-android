package com.robocubs4205.cubscout.scorecardsubmit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.robocubs4205.cubscout.Application;
import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.Scorecard;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by trevor on 1/21/17.
 */

class StatePersistor {
    static final String FILENAME = "Presenter";
    private final Application application;
    private final Gson gson;

    @Inject
    public StatePersistor(Application application, Gson gson) {

        this.application = application;
        this.gson = gson;
    }

    public Completable serialize(final Map<Integer, FieldScore> fieldScores,
                                 final Scorecard scorecard,
                                 final Integer teamNumber, final Integer matchNumber,
                                 final List<Event> eventList, final Event currentEvent) {
        return Completable.fromAction(() -> {
            FileOutputStream fileOutputStream = application.openFileOutput(
                    StatePersistor.FILENAME,
                    Context.MODE_PRIVATE);
            PersistedClass persistedClass = new PersistedClass(
                    fieldScores, scorecard,
                    teamNumber, matchNumber, eventList, eventList.indexOf(currentEvent));
            IOUtils.write(gson.toJson(persistedClass), fileOutputStream);
        });
    }

    public Observable<PersistedClass> deserialize() {
        return Observable.create(new ObservableOnSubscribe<PersistedClass>() {
            @Override
            public void subscribe(ObservableEmitter<PersistedClass> emitter)
                    throws Exception {
                try {
                    FileInputStream fileInputStream = application.openFileInput(
                            StatePersistor.FILENAME);
                    if (fileInputStream != null) {
                        StatePersistor.PersistedClass persistedClass = gson.fromJson(
                                IOUtils.toString(fileInputStream),
                                StatePersistor.PersistedClass.class);
                        emitter.onNext(persistedClass);
                        emitter.onComplete();
                    }
                    else emitter.onError(new FileNotFoundException());
                }
                catch (IOException e) {
                    Log.e("ScorecardSubmit", "failed to deserialize view data from disk", e);
                    emitter.onError(e);
                }
                catch (JsonParseException e) {
                    Log.e("ScorecardSubmit",
                          "Stored data file for ScorecardSubmit is corrupted. erasing",
                          e);
                    emitter.onError(e);
                }
            }
        });
    }

    public Completable clearCache() {
        return Completable.fromAction(() -> application.deleteFile(StatePersistor.FILENAME));
    }

    static class PersistedClass {
        public final Integer matchNumber;
        public final Map<Integer, FieldScore> fieldScores;
        public final Scorecard scorecard;
        public final Integer teamNumber;
        public final List<Event> eventList;
        public final int currentEventIndex;

        public PersistedClass(Map<Integer, FieldScore> fieldScores, Scorecard scorecard,
                              Integer teamNumber, Integer matchNumber, List<Event> eventList,
                              int currentEventIndex) {

            this.fieldScores = fieldScores;
            this.scorecard = scorecard;
            this.teamNumber = teamNumber;
            this.matchNumber = matchNumber;
            this.eventList = eventList;
            this.currentEventIndex = currentEventIndex;
        }
    }
}
