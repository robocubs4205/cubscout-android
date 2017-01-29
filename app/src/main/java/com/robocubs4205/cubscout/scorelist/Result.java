package com.robocubs4205.cubscout.scorelist;

import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.Scorecard;

import java.util.List;

/**
 * Created by trevor on 1/14/17.
 */
public class Result {
    public final Scorecard scorecard;
    public final List<FieldScore> fieldScores;
    public final int teamNumber;

    public Result(Scorecard scorecard, List<FieldScore> fieldScores, int teamNumber) {
        this.scorecard = scorecard;
        this.fieldScores = fieldScores;
        this.teamNumber = teamNumber;
    }
}
