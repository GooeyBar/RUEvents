package gtsioni.dtsioni.crejaud.ruevents;

import java.util.Date;

public class Event {

    private String name, description, location, host;
    private Date date;

    public Event(String name, String description, String location, String host, Long date) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.host = host;
        this.date = new Date(date);
    }


    // ACCESSORS
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public String getLocation() {
        return this.location;
    }
    public String getHost() {
        return this.host;
    }
    public Date getDate() {
        return this.date;
    }
}
