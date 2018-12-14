package bsn_traveladvisor;


import java.util.ArrayList;
import java.util.List;
import java.text.*;

public class Attraction {
    
    private String requestID;
    private String requester;
    private String name;
    private String description;
    private String city;
    private String state;
    private String atags;
    private String status;
    private Double score;
    
    public Attraction(String r, String re, String n, String d, String c, String s, String t, String st, Double score)
    {
        requestID = r;
        requester = re;
        name = n;
        description = d;
        city = c;
        state = s;
        atags = t;
        status = st;
        this.score = score;
    }

    Attraction() {
    }

  
    
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAtags() {
        return atags;
    }

    public void setAtags(String atags) {
        this.atags = atags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
    

    
}
