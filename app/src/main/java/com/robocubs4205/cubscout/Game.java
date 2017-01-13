package com.robocubs4205.cubscout;

/**
 * Created by trevor on 12/30/16.
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class Game {
    private long id;
    private String name;
    private String type;
    private int year;
    private Scorecard scorecard;

    public Game(long id, String name, String type, int year) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.year = year;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
