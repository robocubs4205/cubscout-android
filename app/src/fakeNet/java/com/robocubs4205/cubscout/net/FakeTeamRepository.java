package com.robocubs4205.cubscout.net;

import com.robocubs4205.cubscout.Team;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by trevor on 1/29/17.
 */
public class FakeTeamRepository implements TeamRepository {
    @Override
    public void persist(Team team) {
        //TODO
        throw new NotImplementedException("");
    }

    @Override
    public QueryBuilder queryBuilder() {
        //TODO
        throw new NotImplementedException("");
    }
}
