package com.robocubs4205.cubscout;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
import java.util.List;

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

    public Event(int id, String name, Game game, Date startDate, Date endDate,
                 List<Match> matches) {
        this.id = id;
        this.name = name;
        this.game = game;
        this.startDate = startDate;
        this.endDate = endDate;
        this.matches = matches;
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
