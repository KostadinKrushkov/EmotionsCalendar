package com.pearov.emotionscalendar;

public class ClientDate {
    private int dateId;
    private int clientId;

    public ClientDate(int dateId, int clientId) {
        this.dateId = dateId;
        this.clientId = clientId;
    }

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
