package com.robocubs4205.cubscout.scorecardsubmit;

import android.support.annotation.AnyThread;
import android.support.annotation.MainThread;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.Scorecard;
import com.robocubs4205.cubscout.net.FakeCubscoutApi;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection.NullWhen.UNCHECKED;
/**
 * Created by trevor on 1/8/17.
 */

final class Presenter {
    private final MVPView view;
    private final FakeCubscoutApi api;
    private final StatePersistor persistor;
    private final Map<Integer, FieldScore> fieldScores = new ArrayMap<>();
    private final List<Event> ongoingEvents = new ArrayList<>();
    private Integer teamNumber = null;
    private Integer matchNumber = null;
    private Scorecard currentScorecard;
    private Event currentEvent;

    private boolean isInitialized = false;

    @AnyThread
    @Inject
    public Presenter(final MVPView view, final FakeCubscoutApi api,
                     StatePersistor persistor) {
        this.view = view;
        this.api = api;
        this.persistor = persistor;
        Observable.mergeDelayError(deserialize().toObservable(),
                                   api.getOngoingEvents()
                                      .doOnNext((response) -> {
                                          if (response == null)
                                              throw new AssertionError();
                                          List<Event> concreteEvents = response.events;
                                          ongoingEvents.clear();
                                          ongoingEvents.addAll(concreteEvents);
                                      }))

                  .subscribeOn(Schedulers.io())
                  .doOnTerminate(this::init)
                  .subscribe();


    }

    @AnyThread
    private void clearCache() {
        persistor.clearCache().subscribe();
    }

    @AnyThread
    private void init() {
        Scorecard scorecard = FakeCubscoutApi.getDemoScorecard();
        if (fieldScores.isEmpty() || !currentScorecard.equals(scorecard)) {
            for (int i = 0; i < scorecard.sections.size(); ++i) {
                Scorecard.ScorecardSection section = scorecard.sections.get(i);
                if (section instanceof Scorecard.ScorecardNullableFieldSection) {
                    Scorecard.ScorecardNullableFieldSection concreteSection =
                            (Scorecard.ScorecardNullableFieldSection) section;
                    fieldScores.put(i, new FieldScore(scorecard, i, 0,
                                                      concreteSection.nullWhen == UNCHECKED));
                }
                else if (section instanceof Scorecard.ScorecardFieldSection) {
                    fieldScores.put(i, new FieldScore(scorecard, i, 0, false));
                }
            }
        }
        currentScorecard = scorecard;
        view.setScorecard(scorecard);
        view.setMatchNumber(matchNumber);
        view.setTeamNumber(teamNumber);
        view.loadSavedScores(fieldScores);
        isInitialized = true;
    }

    @AnyThread
    private Completable deserialize() {
        return persistor.deserialize()
                        .doOnNext(persistedClass -> {
                            fieldScores.clear();
                            fieldScores.putAll(persistedClass.fieldScores);
                            currentScorecard = persistedClass.scorecard;
                            teamNumber = persistedClass.teamNumber;
                            matchNumber = persistedClass.matchNumber;
                            ongoingEvents.clear();
                            ongoingEvents.addAll(persistedClass.eventList);
                            currentEvent = persistedClass.eventList.get(
                                    persistedClass.currentEventIndex);
                        })
                        .doOnError(throwable -> {
                            if (throwable instanceof JsonParseException) {
                                clearCache();
                            }
                        }).ignoreElements();
    }

    @AnyThread
    private void serialize() {
        persistor.serialize(fieldScores, currentScorecard, teamNumber, matchNumber, ongoingEvents,
                            currentEvent).subscribe(
                () -> {
                },
                throwable ->
                        Log.e("ScorecardSubmit", "fail to serialize view state to disk",
                              throwable));
    }

    @AnyThread
    public boolean isInitialized() {
        return isInitialized;
    }

    @MainThread
    public void setFieldValue(@NotNull FieldScore value) {
        if (isInitialized) {
            fieldScores.put(value.scorecardIndex, value);
            serialize();
        }
    }

    @MainThread
    public void submit() {
        if (teamNumber == null) {
            view.notifyTeamNumberMissing();
        }
        if (matchNumber == null) {
            view.notifyMatchNumberMissing();
        }
        if (matchNumber != null && teamNumber != null) {
            api.submitMatch(teamNumber, matchNumber, currentScorecard, fieldScores.values())
               .subscribeOn(Schedulers.io())
               .subscribe(() -> {
                   clearCache();
                   view.end();
               });
        }
    }

    @MainThread
    void setTeamNumber(final Integer teamNumber) {
        if (isInitialized) {
            this.teamNumber = teamNumber;
            serialize();
        }
    }

    @MainThread
    void setMatchNumber(final Integer matchNumber) {
        if (isInitialized) {
            this.matchNumber = matchNumber;
            serialize();
        }
    }

    @MainThread
    void setEvent(final Event event) {
        currentEvent = event;
    }

}
