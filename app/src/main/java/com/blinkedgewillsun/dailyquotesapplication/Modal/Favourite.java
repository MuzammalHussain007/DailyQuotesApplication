package com.blinkedgewillsun.dailyquotesapplication.Modal;

public class Favourite {

    private String noteNname , noteAuthor;

    public String getNoteNname() {
        return noteNname;
    }

    public void setNoteNname(String noteNname) {
        this.noteNname = noteNname;
    }

    public String getNoteAuthor() {
        return noteAuthor;
    }

    public void setNoteAuthor(String noteAuthor) {
        this.noteAuthor = noteAuthor;
    }

    public Favourite(String noteNname, String noteAuthor) {
        this.noteNname = noteNname;
        this.noteAuthor = noteAuthor;
    }
}
