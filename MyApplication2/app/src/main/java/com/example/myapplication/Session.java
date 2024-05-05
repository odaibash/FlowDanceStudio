package com.example.myapplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session {
    private String sessionName;
    private List<String> participants;
    private String site;
    private Date date;
    private String startTime;
    private String endTime;
    private List<String> admins;
    private String coach;

    public Session() {
        // Default constructor required for Firestore
    }

    public Session(String sessionName, String site, Date date, String startTime, String endTime, List<String> admins, String coach) {
        this.sessionName = sessionName;
        this.site = site;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.admins = admins;
        this.participants = new ArrayList<>();
        this.coach = coach;
    }

    public String getName() {
        return sessionName;
    }

    public void setName(String sessionName) {
        this.sessionName = sessionName;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void addParticipant(String participant) {
        this.participants.add(participant);
    }

    public void removeParticipant(String participant) {
        this.participants.remove(participant);
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("sessionName", sessionName);
        map.put("participants", participants);
        map.put("site", site);
        map.put("date", date);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("admins", admins);
        map.put("coach", coach);
        return map;
    }

    public static Session fromMap(Map<String, Object> map) {
        Session session = new Session();
        session.setName((String) map.get("sessionName"));
        session.setSite((String) map.get("site"));
        session.setDate((Date) map.get("date"));
        session.setStartTime((String) map.get("startTime"));
        session.setEndTime((String) map.get("endTime"));
        session.setCoach((String) map.get("coach"));

        // Participants need to be added individually
        List<String> participants = (List<String>) map.get("participants");
        if (participants != null) {
            for (String participant : participants) {
                session.addParticipant(participant);
            }
        }

        return session;
    }
}