package com.pearov.emotionscalendar;

public class Client {

    private String username;
    private String joinedOnDate;
    private String country;

    public Client(String username, String joinedOnDate, String country) {
        this.username = username;
        this.joinedOnDate = joinedOnDate;
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public String getJoinedOnDate() {
        return joinedOnDate;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Client{" +
                "username='" + username + '\'' +
                ", joinedOnDate='" + joinedOnDate + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
