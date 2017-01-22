package com.robocubs4205.cubscout.scorecardsubmit;

import android.support.annotation.AnyThread;
import android.support.annotation.MainThread;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.robocubs4205.cubscout.DemoDataProvider;
import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.Scorecard;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection.NullWhen.UNCHECKED;

/**
 * Created by trevor on 1/8/17.
 */

final class ScorecardSubmitPresenter {
    private final ScorecardSubmitView view;
    private final DemoDataProvider api;
    private final ScorecardSubmitStatePersistor persistor;
    private final Map<Integer, FieldScore> fieldScores = new ArrayMap<>();
    private Integer teamNumber = null;
    private Integer matchNumber = null;
    private Scorecard currentScorecard;

    private boolean initialized = false;

    @AnyThread
    @Inject
    public ScorecardSubmitPresenter(final ScorecardSubmitView view, final DemoDataProvider api,
                                    ScorecardSubmitStatePersistor persistor) {
        this.view = view;
        this.api = api;
        this.persistor = persistor;
        deserialize();
    }

    @AnyThread
    private void clearCache() {
        persistor.clearCache();
    }

    @AnyThread
    private void initActual() {
        Scorecard scorecard = DemoDataProvider.getDemoScorecard();
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
        initialized = true;
    }

    @AnyThread
    private void deserialize() {
        persistor.deserialize().subscribeOn(Schedulers.io()).subscribe(
                new Consumer<ScorecardSubmitStatePersistor.PersistedClass>() {
                    @Override
                    public void accept(ScorecardSubmitStatePersistor.PersistedClass persistedClass)
                            throws Exception {
                        fieldScores.clear();
                        fieldScores.putAll(persistedClass.fieldScores);
                        currentScorecard = persistedClass.scorecard;
                        teamNumber = persistedClass.teamNumber;
                        matchNumber = persistedClass.matchNumber;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof JsonParseException) {
                            clearCache();
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        initActual();
                    }
                });
    }

    @AnyThread
    private void serialize() {
        persistor.serialize(fieldScores, currentScorecard, teamNumber, matchNumber).subscribe(
                new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ScorecardSubmit", "fail to serialize view state to disk", throwable);
                    }
                });
    }

    @AnyThread
    public void setFieldValue(FieldScore value) {
        if (initialized) {
            fieldScores.put(value.scorecardIndex, value);
            serialize();
        }
    }

    @MainThread
    public void submit() {
        if (initialized) {
            if (teamNumber == null) {
                view.notifyTeamNumberMissing();
            }
            if (matchNumber == null) {
                view.notifyMatchNumberMissing();
            }
            if (matchNumber != null && teamNumber != null) {
                api.submitMatch(teamNumber, matchNumber, currentScorecard, fieldScores.values())
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(new Action() {
                       @Override
                       public void run() throws Exception {
                           clearCache();
                           view.end();
                       }
                   });
            }
        }
    }

    @MainThread
    void setTeamNumber(final Integer teamNumber) {
        if (initialized) {
            this.teamNumber = teamNumber;
            serialize();
        }
    }

    @MainThread
    void setMatchNumber(final Integer matchNumber) {
        if (initialized) {
            this.matchNumber = matchNumber;
            serialize();
        }
    }

}
