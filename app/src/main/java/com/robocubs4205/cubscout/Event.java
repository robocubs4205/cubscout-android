package com.robocubs4205.cubscout;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by trevor on 12/31/16.
 */
public final class Event {
    private long id;
    private String name;

    public Event(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Event)) {
            return false;
        }
        else if (o == this) {
            return true;
        }
        else {
            Event rhs = (Event) o;
            return new EqualsBuilder().append(id, rhs.id).build();
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(id).toHashCode();
    }
}
