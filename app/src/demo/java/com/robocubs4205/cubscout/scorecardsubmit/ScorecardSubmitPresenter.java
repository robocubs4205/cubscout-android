package com.robocubs4205.cubscout.scorecardsubmit;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.robocubs4205.cubscout.Application;
import com.robocubs4205.cubscout.DemoDataProvider;
import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.Scorecard;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection.NullWhen.CHECKED;

/**
 * Created by trevor on 1/8/17.
 */

final class ScorecardSubmitPresenter {
    private static final String FILENAME = "ScorecardSubmitPresenter";
    private final ScorecardSubmitView view;
    private final Application application;
    private final DemoDataProvider api;
    private final Gson gson;
    private final Map<Integer, FieldScore> fieldScores = new ArrayMap<>();
    private Integer teamNumber = null;
    private Integer matchNumber = null;
    private Scorecard currentScorecard;

    @Inject
    public ScorecardSubmitPresenter(final ScorecardSubmitView view, final Application application,
                                    final DemoDataProvider api, final Gson gson) {
        this.view = view;
        this.application = application;
        this.api = api;
        this.gson = gson;

        if (!deserialize()) {
            init();
        }
    }

    public static void clearCache(final Context context) {
        context.deleteFile(FILENAME);
    }

    public void initView() {
        view.loadSavedScores(fieldScores);
        view.setScorecard(currentScorecard);
        view.setTeamNumber(teamNumber);
        view.setMatchNumber(matchNumber);
    }

    private void init() {
        Scorecard scorecard = api.getDemoScorecard();
        if (fieldScores.isEmpty() || currentScorecard != scorecard) {
            for (int i = 0; i < scorecard.sections.size(); ++i) {
                Scorecard.ScorecardSection section = scorecard.sections.get(i);
                if (section instanceof Scorecard.ScorecardNullableFieldSection) {
                    Scorecard.ScorecardNullableFieldSection concreteSection =
                            (Scorecard.ScorecardNullableFieldSection) section;
                    fieldScores.put(i, new FieldScore(scorecard, i, 0,
                                                      concreteSection.nullWhen == CHECKED));
                }
                else if (section instanceof Scorecard.ScorecardFieldSection) {
                    fieldScores.put(i, new FieldScore(scorecard, i, 0, false));
                }
            }
        }
        currentScorecard = scorecard;
    }

    private boolean deserialize() {
        try {
            FileInputStream fileInputStream = application.openFileInput(FILENAME);
            PersistedClass persistedClass = gson.fromJson(IOUtils.toString(fileInputStream),
                                                          PersistedClass.class);
            fieldScores.clear();
            fieldScores.putAll(persistedClass.fieldScores);
            currentScorecard = persistedClass.scorecard;
            teamNumber = persistedClass.teamNumber;
            matchNumber = persistedClass.matchNumber;
            return true;
        }
        catch (IOException e) {
            Log.e("ScorecardSubmit", "failed to deserialize view data from disk", e);
            return false;
        }
        catch (JsonParseException e) {
            Log.e("ScorecardSubmit", "Stored data file for ScorecardSubmit is corrupted. erasing",
                  e);
            clearCache(application);
            return false;
        }
    }

    private void serialize() throws IOException {
        FileOutputStream fileOutputStream = application.openFileOutput(FILENAME,
                                                                       Context.MODE_PRIVATE);
        PersistedClass persistedClass = new PersistedClass(fieldScores, currentScorecard,
                                                           teamNumber, matchNumber);
        IOUtils.write(gson.toJson(persistedClass), fileOutputStream);
    }

    public void setFieldValue(FieldScore value) {
        fieldScores.put(value.scorecardIndex, value);
        try {
            serialize();
        }
        catch (IOException e) {
            Log.e("ScorecardSubmit", "fail to serialize view state to disk", e);
        }
    }

    public void submit() {
        if (teamNumber == null) {
            view.notifyTeamNumberMissing();
        }
        if (matchNumber == null) {
            view.notifyMatchNumberMissing();
        }
        if (matchNumber != null && teamNumber != null) {
            api.submitMatch(teamNumber, matchNumber, currentScorecard, fieldScores.values());
            clearCache(application);
            view.end();
        }
    }

    void setTeamNumber(final Integer teamNumber) {
        this.teamNumber = teamNumber;
        try {
            serialize();
        }
        catch (IOException e) {
            Log.e("ScorecardSubmit", "fail to serialize view state to disk", e);
        }
    }

    void setMatchNumber(final Integer matchNumber) {
        this.matchNumber = matchNumber;
        try {
            serialize();
        }
        catch (IOException e) {
            Log.e("ScorecardSubmit", "fail to serialize view state to disk", e);
        }
    }

    private static class PersistedClass {
        public final Integer matchNumber;
        public final Map<Integer, FieldScore> fieldScores;
        public final Scorecard scorecard;
        public final Integer teamNumber;

        public PersistedClass(Map<Integer, FieldScore> fieldScores, Scorecard scorecard,
                              Integer teamNumber, Integer MatchNumber) {

            this.fieldScores = fieldScores;
            this.scorecard = scorecard;
            this.teamNumber = teamNumber;
            matchNumber = MatchNumber;
        }
    }

}
