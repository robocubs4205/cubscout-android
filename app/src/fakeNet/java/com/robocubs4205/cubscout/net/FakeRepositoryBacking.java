package com.robocubs4205.cubscout.net;

/**
 * Created by trevor on 1/30/17.
 */


import android.annotation.SuppressLint;

import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.Match;
import com.robocubs4205.cubscout.Team;

import org.apache.commons.lang3.NotImplementedException;

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
        //TODO
        throw new NotImplementedException("");
    }

    public void delete(Game game) {
        //TODO
        throw new NotImplementedException("");
    }

    public Collection<Game> getGames() {
        return games.values();
    }

    public void insertOrUpdate(Event event) {
        //TODO
        throw new NotImplementedException("");
    }

    public void delete(Event event) {
        //TODO
        throw new NotImplementedException("");
    }

    public Collection<Event> getEvents() {
        return events.values();
    }

    public void insertOrUpdate(Match match) {
        matches.put(match.id, match);
    }

    public void delete(Match match) {
        //TODO
        throw new NotImplementedException("");
    }

    public Collection<Match> getMatches() {
        return matches.values();
    }

    public void insertOrUpdate(Team team) {
        //TODO
        throw new NotImplementedException("");
    }

    public void delete(Team team) {
        //TODO
        throw new NotImplementedException("");
    }

    public Collection<Team> getTeams() {
        return teams.values();
    }
}
