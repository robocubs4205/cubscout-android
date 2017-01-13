package com.robocubs4205.cubscout;

import com.robocubs4205.cubscout.net.StubCubscoutApi;

import java.util.ArrayList;

/**
 * Created by trevor on 1/10/17.
 */

public class DemoDataProvider extends StubCubscoutApi {
    public Scorecard getDemoScorecard() {
        Scorecard scorecard = new Scorecard();
        scorecard.id = 1;
        scorecard.sections = new ArrayList<>();
        scorecard.sections.add(
                new Scorecard.ScorecardFieldSection("Gears taken to airship",
                                                    Scorecard.ScorecardFieldSection.Type.COUNT, 1));
        scorecard.sections.add(
                new Scorecard.ScorecardFieldSection("Low boiler effectiveness",
                                                    Scorecard.ScorecardFieldSection.Type.RATING,
                                                    2));
        scorecard.sections.add(
                new Scorecard.ScorecardFieldSection("High boiler effectiveness",
                                                    Scorecard.ScorecardFieldSection.Type.RATING,
                                                    3));
        return scorecard;
    }
}
