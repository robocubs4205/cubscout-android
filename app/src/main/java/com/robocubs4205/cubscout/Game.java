package com.robocubs4205.cubscout;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.robocubs4205.cubscout.Event.EventBuilder;
import static com.robocubs4205.cubscout.Event.EventInfo;
import static com.robocubs4205.cubscout.Match.MatchInfo;

/**
 * Created by trevor on 12/30/16.
 */
public class Game {
    public final int id;
    public final String name;
    public final String type;
    public final int year;
    public final Scorecard scorecard;
    public final Collection<Event> events;

    private Game(int id, String name, String type, int year, Scorecard scorecard,
                 List<EventInfo> eventInfos, Multimap<Integer, MatchInfo> matchInfos) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.year = year;
        this.scorecard = scorecard;
        ImmutableList.Builder<Event> eventListBuilder = new ImmutableList.Builder<>();
        for (EventInfo eventInfo : eventInfos) {
            EventBuilder builder = new EventBuilder(eventInfo.id, eventInfo.name, this,
                                                    eventInfo.startDate, eventInfo.endDate);
            for (MatchInfo matchInfo : matchInfos.get(eventInfo.id)) {
                builder.addMatch(matchInfo);
            }
            eventListBuilder.add(builder.build());
        }
        events = eventListBuilder.build();
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public static class GameBuilder {
        private final int id;
        private final String name;
        private final String type;
        private final int year;
        private final Scorecard scorecard;
        private final List<EventInfo> eventInfos = new LinkedList<>();
        private final Multimap<Integer, MatchInfo> matchInfos = LinkedListMultimap.create();

        public GameBuilder(int id, String name, String type, int year, Scorecard scorecard) {

            this.id = id;
            this.name = name;
            this.type = type;
            this.year = year;
            this.scorecard = scorecard;
        }

        public GameBuilder addEvent(EventInfo eventInfo) {
            eventInfos.add(eventInfo);
            return this;
        }

        public GameBuilder addMatch(int eventId, MatchInfo matchInfo) {
            matchInfos.put(eventId, matchInfo);
            return this;
        }

        public Game build() {
            return new Game(id, name, type, year, scorecard, eventInfos, matchInfos);
        }
    }

    public static class GameInfo {
        public final int id;
        public final String name;
        public final String type;
        public final int year;
        public final Scorecard scorecard;

        public GameInfo(int id, String name, String type, int year, Scorecard scorecard) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.year = year;
            this.scorecard = scorecard;
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
