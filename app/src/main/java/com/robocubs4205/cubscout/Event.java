package com.robocubs4205.cubscout;

import com.google.common.collect.ImmutableList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import static com.robocubs4205.cubscout.Game.GameBuilder;
import static com.robocubs4205.cubscout.Game.GameInfo;
import static com.robocubs4205.cubscout.Match.MatchBuilder;
import static com.robocubs4205.cubscout.Match.MatchInfo;

/**
 * Created by trevor on 12/31/16.
 */
public final class Event {
    public final int id;
    public final String name;
    public final Game game;
    public final Date startDate;
    public final Date endDate;
    public final List<Match> matches;

    private Event(int id, String name, Game game, Date startDate, Date endDate,
                  Collection<MatchInfo> matchInfos) {
        this.id = id;
        this.name = name;
        this.game = game;
        this.startDate = startDate;
        this.endDate = endDate;
        ImmutableList.Builder<Match> matchListBuilder = new ImmutableList.Builder<>();
        for (MatchInfo matchInfo : matchInfos) {
            matchListBuilder.add(new MatchBuilder(matchInfo.id, this).build());
        }
        matches = matchListBuilder.build();
    }

    private Event(int id, String name, GameInfo gameInfo, Date startDate, Date endDate,
                  Collection<MatchInfo> matchInfos) {
        this.id = id;
        this.game = new GameBuilder(gameInfo.id, gameInfo.name, gameInfo.type, gameInfo.year,
                                    gameInfo.scorecard).addEvent(EventInfo.from(this)).build();
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        ImmutableList.Builder<Match> matchListBuilder = new ImmutableList.Builder<>();
        for (MatchInfo matchInfo : matchInfos) {
            matchListBuilder.add(new MatchBuilder(matchInfo.id, this).build());
        }
        matches = matchListBuilder.build();
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    public static class EventBuilder {
        private final int id;
        private final String name;
        private final Game game;
        private final GameInfo gameInfo;
        private final Date startDate;
        private final Date endDate;
        private final Collection<MatchInfo> matchInfos = new LinkedHashSet<>();

        public EventBuilder(int id, String name, Game game, Date startDate, Date endDate) {
            this.id = id;
            this.name = name;
            this.game = game;
            this.startDate = startDate;
            this.endDate = endDate;
            this.gameInfo = null;
        }

        public EventBuilder(int id, String name, GameInfo gameInfo, Date startDate, Date endDate) {
            this.id = id;
            this.name = name;
            this.game = null;
            this.startDate = startDate;
            this.endDate = endDate;
            this.gameInfo = gameInfo;
        }

        EventBuilder addMatch(MatchInfo matchInfo) {
            matchInfos.add(matchInfo);
            return this;
        }

        public Event build() {
            if (game != null) {
                return new Event(id, name, game, startDate, endDate, matchInfos);
            }
            else if (gameInfo != null) {
                return new Event(id, name, gameInfo, startDate, endDate, matchInfos);
            }
            else throw new IllegalStateException("EventBuilder has neither Game nor GameInfo");
        }
    }

    public static class EventInfo {
        public final int id;
        public final String name;
        public final Date startDate;
        public final Date endDate;

        EventInfo(int id, String name, Date startDate,
                  Date endDate) {
            this.id = id;
            this.name = name;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public static EventInfo from(Event event) {
            return new EventInfo(event.id, event.name, event.startDate, event.endDate);
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
