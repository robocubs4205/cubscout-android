package com.robocubs4205.cubscout.scorecardsubmit;

import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.Scorecard;

import java.util.Map;

/**
 * Created by trevor on 1/8/17.
 */

interface ScorecardSubmitView {
    void setScorecard(Scorecard scorecard);

    void loadSavedScores(Map<Integer, FieldScore> scores);

    void setTeamNumber(Integer teamNumber);

    void setMatchNumber(Integer matchNumber);

    void end();

    void notifyMatchNumberMissing();

    void notifyTeamNumberMissing();
}
