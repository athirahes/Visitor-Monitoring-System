package my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1;

import java.io.Serializable;

public class Premise implements Serializable {

    private String premiseName, premiseType, premiseDescription, premiseLocation;
    private int currentCount, visitorAllowed, TrackVisitorCount;
    private double rating;

    public Premise() { }

    public String getPremiseName() {
        return premiseName;
    }

    public void setPremiseName(String premiseName) {
        this.premiseName = premiseName;
    }

    public String getPremiseType() {
        return premiseType;
    }

    public void setPremiseType(String premiseType) {
        this.premiseType = premiseType;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public int getVisitorAllowed() {
        return visitorAllowed;
    }

    public void setVisitorAllowed(int visitorAllowed) {
        this.visitorAllowed = visitorAllowed;
    }

    public String getPremiseDescription() {
        return premiseDescription;
    }

    public void setPremiseDescription(String premiseDescription) { this.premiseDescription = premiseDescription; }

    public String getPremiseLocation() { return premiseLocation; }

    public void setPremiseLocation(String premiseLocation) { this.premiseLocation = premiseLocation; }

    public int getTrackVisitorCount() { return TrackVisitorCount; }

    public void setTrackVisitorCount(int trackVisitorCount) { TrackVisitorCount = trackVisitorCount; }

    public double getRating() { return rating; }

    public void setRating(double rating) { this.rating = rating; }
}
