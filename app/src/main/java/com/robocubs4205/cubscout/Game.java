package com.robocubs4205.cubscout;

import java.util.List;

/**
 * Created by trevor on 12/30/16.
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class Game {
    public final int id;
    public final String name;
    public final String type;
    public final int year;
    public final Scorecard scorecard;
    public final List<Event> events;

    public Game(int id, String name, String type, int year, Scorecard scorecard,
                List<Event> events) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.year = year;
        this.scorecard = scorecard;
        this.events = events;
    }
}
