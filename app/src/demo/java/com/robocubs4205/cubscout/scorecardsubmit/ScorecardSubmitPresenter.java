package com.robocubs4205.cubscout.scorecardsubmit;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.robocubs4205.cubscout.DemoDataProvider;
import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.Scorecard;
import com.robocubs4205.cubscout.net.CubscoutAPI;

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
    private final Context context;
    private final CubscoutAPI api;
    private final Gson gson;
    private final DemoDataProvider provider = new DemoDataProvider();
    private final Map<Integer, FieldScore> fieldScores = new ArrayMap<>();
    private Integer teamNumber = null;
    private Integer matchNumber = null;
    private Scorecard currentScorecard;

    @Inject
    public ScorecardSubmitPresenter(final ScorecardSubmitView view, final Context context,
                                    CubscoutAPI api, Gson gson) {
        this.view = view;
        this.context = context.getApplicationContext();
        this.api = api;
        this.gson = gson;

        if (!deserialize()) {
            init();
        }
    }

    public static void clearCache(Context context) {
        context.deleteFile(FILENAME);
    }

    public void initView() {
        view.loadSavedScores(fieldScores);
        view.setScorecard(currentScorecard);
        view.setTeamNumber(teamNumber);
        view.setMatchNumber(matchNumber);
    }

    private void init() {
        Scorecard scorecard = provider.getDemoScorecard();
        if (fieldScores.isEmpty() || currentScorecard != scorecard) {
            int adapterIndex = 0;
            for (int i = 0; i < scorecard.sections.size(); ++i) {
                Scorecard.ScorecardSection section = scorecard.sections.get(i);
                if (section instanceof Scorecard.ScorecardNullableFieldSection) {
                    Scorecard.ScorecardNullableFieldSection concreteSection =
                            (Scorecard.ScorecardNullableFieldSection) section;
                    //noinspection ConstantConditions
                    fieldScores.put(adapterIndex, new FieldScore(i,
                                                                 (concreteSection.nullWhen ==
                                                                         CHECKED) ? 0 : null));
                }
                else if (section instanceof Scorecard.ScorecardFieldSection) {
                    fieldScores.put(adapterIndex, new FieldScore(i, 0));
                }
                adapterIndex++;
            }
        }
        currentScorecard = scorecard;
    }

    private boolean deserialize() {
        try {
            FileInputStream fileInputStream = context.openFileInput(FILENAME);
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
            clearCache(context);
            return false;
        }
    }

    private void serialize() throws IOException {
        FileOutputStream fileOutputStream = context.openFileOutput(FILENAME,
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
            clearCache(context);
            view.end();
        }
    }

    void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
        try {
            serialize();
        }
        catch (IOException e) {
            Log.e("ScorecardSubmit", "fail to serialize view state to disk", e);
        }
    }

    void setMatchNumber(Integer matchNumber) {
        this.matchNumber = matchNumber;
        try {
            serialize();
        }
        catch (IOException e) {
            Log.e("ScorecardSubmit", "fail to serialize view state to disk", e);
        }
    }

    private static class PersistedClass {
        private final Integer matchNumber;
        public Map<Integer, FieldScore> fieldScores;
        public Scorecard scorecard;
        public Integer teamNumber;

        public PersistedClass(Map<Integer, FieldScore> fieldScores, Scorecard scorecard,
                              Integer teamNumber, Integer MatchNumber) {

            this.fieldScores = fieldScores;
            this.scorecard = scorecard;
            this.teamNumber = teamNumber;
            matchNumber = MatchNumber;
        }
    }

}
