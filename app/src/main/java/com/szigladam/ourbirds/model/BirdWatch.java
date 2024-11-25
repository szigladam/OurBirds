package com.szigladam.ourbirds.model;

public class BirdWatch {

    private int id;
    private String birdSpecies;
    private String watchDate;
    private String location;
    private double latitude;
    private double longitude;
    private String habitat;
    private String comment;
    private String user;

    public BirdWatch() {
    }

    public BirdWatch(int id, String birdSpecies, String watchDate, String location, double latitude, double longitude, String habitat, String comment, String user) {
        this.id = id;
        this.birdSpecies = birdSpecies;
        this.watchDate = watchDate;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.habitat = habitat;
        this.comment = comment;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBirdSpecies() {
        return birdSpecies;
    }

    public void setBirdSpecies(String birdSpecies) {
        this.birdSpecies = birdSpecies;
    }

    public String getWatchDate() {
        return watchDate;
    }

    public void setWatchDate(String watchDate) {
        this.watchDate = watchDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}