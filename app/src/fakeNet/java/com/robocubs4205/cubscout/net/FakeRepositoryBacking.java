package com.robocubs4205.cubscout.net;

/**
 * Created by trevor on 1/30/17.
 */


import android.annotation.SuppressLint;

import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.Match;
import com.robocubs4205.cubscout.Team;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * class for holding data accessed through fake repositories
 */
@SuppressLint("UseSparseArrays")
class FakeRepositoryBacking {
    private final Map<Integer, Event> events = new HashMap<>();
    private final Map<Integer, Game> games = new HashMap<>();
    private final Map<Integer, Match> matches = new HashMap<>();
    private final Map<Integer, Team> teams = new HashMap<>();

    public void insertOrUpdate(Game game) {
        games.put(game.id, game);
        for (Event event :
                game.events) {
            insertOrUpdateNoBackref(event);
        }
    }

    public void delete(Game game) {
        games.remove(game.id);
    }

    public Collection<Game> getGames() {
        return games.values();
    }

    public void insertOrUpdate(Event event) {
        insertOrUpdate(event.game);
        insertOrUpdateNoBackref(event);
    }

    private void insertOrUpdateNoBackref(Event event) {
        events.put(event.id, event);
        for (Match match :
                event.matches) {
            insertOrUpdateNoBackref(match);
        }
    }

    public void delete(Event event) {
        events.remove(event.id);
    }

    public Collection<Event> getEvents() {
        return events.values();
    }

    public void insertOrUpdate(Match match) {
        insertOrUpdate(match.event);
        insertOrUpdateNoBackref(match);
    }

    private void insertOrUpdateNoBackref(Match match) {
        matches.put(match.id, match);
    }

    public void delete(Match match) {
        matches.remove(match.id);
    }

    public Collection<Match> getMatches() {
        return matches.values();
    }

    public void insertOrUpdate(Team team) {
        teams.put(team.id, team);
    }

    public void delete(Team team) {
        teams.remove(team.id);
    }

    public Collection<Team> getTeams() {
        return teams.values();
    }
}
