package com.example.tool;

public class NoteModel {
    String id;
    String title;
    String content;
    String updateTime;

    public NoteModel(String id, String title, String content, String updateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content.length() > 20 ? content.substring(0, 20)  + "...": content;
    }

    public String getTitle() {

        return title;
    }

    public String getId() {
        return id;
    }

    public String getUpdateTime() {
        return updateTime;
    }
}
