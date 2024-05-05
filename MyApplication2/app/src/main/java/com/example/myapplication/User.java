package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String email;
    private boolean isCoach;
    private String gender;
    private List<String> coachedDances;
    private List<Session> upcomingSessions;

    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.isCoach = false;
        this.gender = null;
        this.coachedDances = new ArrayList<>();
        this.upcomingSessions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCoach() {
        return isCoach;
    }

    public void setCoach(boolean coach) {
        isCoach = coach;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getCoachedDances() {
        return coachedDances;
    }

    public void setCoachedDances(List<String> coachedDances) {
        this.coachedDances = coachedDances;
    }

    public List<Session> getUpcomingSessions() {
        return upcomingSessions;
    }

    public void setUpcomingSessions(List<Session> upcomingSessions) {
        this.upcomingSessions = upcomingSessions;
    }

    public void addCoachedDance(String danceType) {
        this.coachedDances.add(danceType);
    }

    public void removeCoachedDance(String danceType) {
        this.coachedDances.remove(danceType);
    }

    public void addSession(Session session) {
        this.upcomingSessions.add(session);
    }

    public void removeSession(Session session) {
        this.upcomingSessions.remove(session);
    }
}
