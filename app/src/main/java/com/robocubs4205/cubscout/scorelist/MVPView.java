package com.robocubs4205.cubscout.scorelist;

import com.robocubs4205.cubscout.Scorecard;

import java.util.List;

/**
 * Created by trevor on 1/14/17.
 */

interface MVPView {
    void LoadResults(List<Result> results);

    void setScorecard(Scorecard demoScorecard);
}
