package com.robocubs4205.cubscout.scorecardsubmit;

import android.support.annotation.AnyThread;
import android.support.annotation.Nullable;

import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.Scorecard;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by trevor on 1/8/17.
 */

interface MVPView {

    @AnyThread
    void setScorecard(@NotNull Scorecard scorecard);

    @AnyThread
    void loadSavedScores(@NotNull Map<Integer, FieldScore> scores);

    @AnyThread
    void setTeamNumber(@Nullable Integer teamNumber);

    @AnyThread
    void setMatchNumber(@Nullable Integer matchNumber);

    @AnyThread
    void end();

    @AnyThread
    void notifyMatchNumberMissing();

    @AnyThread
    void notifyTeamNumberMissing();
}
