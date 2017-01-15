package com.robocubs4205.cubscout;

/**
 * Created by trevor on 1/10/17.
 */

public class FieldScore {
    public final Scorecard scorecard;
    public int scorecardIndex;
    public int value;

    public FieldScore(Scorecard scorecard, int scorecardIndex, int value) {
        this.scorecard = scorecard;
        this.scorecardIndex = scorecardIndex;
        this.value = value;
    }
}
