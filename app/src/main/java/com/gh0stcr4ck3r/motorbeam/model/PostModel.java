package com.gh0stcr4ck3r.motorbeam.model;

public class PostModel {
    String id;
    String title;
    String thumb;

    public PostModel(String id, String title, String thumb) {
        this.id = id;
        this.title = title;
        this.thumb = thumb;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumb() {
        return thumb;
    }
}
