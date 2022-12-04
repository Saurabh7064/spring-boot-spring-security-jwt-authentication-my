package com.bezkoder.springjwt.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "campaign")
public class Campaign {

    //id	Campaign_type	campaign_message	age	location 	gender 	time


    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @Column
    private String campaign_type;

    @Column
    private String campaign_message;

    @Column
    private String age;

    @Column
    private String location;

    @Column
    private String gender;

    @Column
    private String time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCampaign_type() {
        return campaign_type;
    }

    public void setCampaign_type(String campaign_type) {
        this.campaign_type = campaign_type;
    }

    public String getCampaign_message() {
        return campaign_message;
    }

    public void setCampaign_message(String campaign_message) {
        this.campaign_message = campaign_message;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
