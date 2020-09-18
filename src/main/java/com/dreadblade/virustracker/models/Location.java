package com.dreadblade.virustracker.models;

public class Location {
    private String state;
    private String country;
    private int latestTotalConfirmedCases;
    private int latestTotalDeathCases;
    private int latestTotalRecoveredCases;
    private int latestTotalActiveCases;
    private int confirmedDelta;
    private int deathDelta;
    private int recoveredDelta;
    private int activeDelta;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLatestTotalConfirmedCases() {
        return latestTotalConfirmedCases;
    }

    public void setLatestTotalConfirmedCases(int latestTotalConfirmedCases) {
        this.latestTotalConfirmedCases = latestTotalConfirmedCases;
    }

    public int getLatestTotalDeathCases() {
        return latestTotalDeathCases;
    }

    public void setLatestTotalDeathCases(int latestTotalDeathCases) {
        this.latestTotalDeathCases = latestTotalDeathCases;
    }

    public int getLatestTotalRecoveredCases() {
        return latestTotalRecoveredCases;
    }

    public void setLatestTotalRecoveredCases(int latestTotalRecoveredCases) {
        this.latestTotalRecoveredCases = latestTotalRecoveredCases;
    }

    public int getLatestTotalActiveCases() {
        return latestTotalActiveCases;
    }

    public void setLatestTotalActiveCases(int latestTotalActiveCases) {
        this.latestTotalActiveCases = latestTotalActiveCases;
    }

    public int getConfirmedDelta() {
        return confirmedDelta;
    }

    public void setConfirmedDelta(int confirmedDelta) {
        this.confirmedDelta = confirmedDelta;
    }

    public int getDeathDelta() {
        return deathDelta;
    }

    public void setDeathDelta(int deathDelta) {
        this.deathDelta = deathDelta;
    }

    public int getRecoveredDelta() {
        return recoveredDelta;
    }

    public void setRecoveredDelta(int recoveredDelta) {
        this.recoveredDelta = recoveredDelta;
    }

    public int getActiveDelta() {
        return activeDelta;
    }

    public void setActiveDelta(int activeDelta) {
        this.activeDelta = activeDelta;
    }
}
