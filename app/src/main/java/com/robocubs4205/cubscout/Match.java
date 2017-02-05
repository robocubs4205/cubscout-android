package com.robocubs4205.cubscout;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static com.robocubs4205.cubscout.Event.EventBuilder;
import static com.robocubs4205.cubscout.Event.EventInfo;
import static com.robocubs4205.cubscout.Game.GameInfo;

/**
 * Created by trevor on 1/29/17.
 */
public class Match {
    public final int id;
    public final Event event;

    private Match(int id, Event event) {
        this.id = id;
        this.event = event;
    }

    private Match(int id, EventInfo eventInfo, GameInfo gameInfo) {
        this.id = id;
        this.event = new EventBuilder(eventInfo.id, eventInfo.name, gameInfo, eventInfo.startDate,
                                      eventInfo.endDate).addMatch(MatchInfo.from(this)).build();
    }

    public static class MatchBuilder {
        private final int id;
        private final EventInfo eventInfo;
        private final GameInfo gameInfo;
        private final Event event;

        public MatchBuilder(int id, Event event) {
            this.id = id;
            this.event = event;
            eventInfo = null;
            gameInfo = null;
        }

        public MatchBuilder(int id, EventInfo eventInfo, GameInfo gameInfo) {
            this.id = id;
            this.eventInfo = eventInfo;
            this.gameInfo = gameInfo;
            event = null;
        }

        public Match build() {
            if (event != null) return new Match(id, event);
            else if (eventInfo != null) return new Match(id, eventInfo, gameInfo);
            else throw new IllegalStateException("MatchBuilder has neither event nor eventInfo");
        }
    }

    static class MatchInfo {
        public final int id;

        MatchInfo(int id) {
            this.id = id;
        }

        private static MatchInfo from(Match match) {
            return new MatchInfo(match.id);
        }

        @Override
        public boolean equals(Object o) {
            return EqualsBuilder.reflectionEquals(this, o, false);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this, false);
        }
    }
}
