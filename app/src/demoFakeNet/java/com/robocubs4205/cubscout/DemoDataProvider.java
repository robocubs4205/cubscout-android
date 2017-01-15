package com.robocubs4205.cubscout;

import com.robocubs4205.cubscout.net.StubCubscoutApi;

import java.util.ArrayList;

import static com.robocubs4205.cubscout.Scorecard.ScorecardFieldSection;
import static com.robocubs4205.cubscout.Scorecard.ScorecardFieldSection.Type.COUNT;
import static com.robocubs4205.cubscout.Scorecard.ScorecardFieldSection.Type.RATING;

/**
 * Created by trevor on 1/10/17.
 */

public class DemoDataProvider extends StubCubscoutApi {
    public Scorecard getDemoScorecard() {
        Scorecard scorecard = new Scorecard();
        scorecard.id = 1;
        scorecard.sections = new ArrayList<>();
        scorecard.sections.add(new ScorecardFieldSection("Gears taken to airship", COUNT));
        scorecard.sections.add(new ScorecardFieldSection("Low boiler effectiveness", RATING));
        scorecard.sections.add(new ScorecardFieldSection("High boiler effectiveness", RATING));
        scorecard.sections.add(new ScorecardFieldSection("", RATING));
        scorecard.sections.add(new ScorecardFieldSection("Defense", RATING));
        return scorecard;
    }
}
