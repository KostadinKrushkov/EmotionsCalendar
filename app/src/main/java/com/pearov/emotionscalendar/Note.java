package com.pearov.emotionscalendar;

public class Note {

    private int id;
    private String title;
    private String noteText;

    public Note(int id, String title, String noteText) {
        this.id = id;
        this.title = title;
        this.noteText = noteText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", noteText='" + noteText + '\'' +
                '}';
    }
}
