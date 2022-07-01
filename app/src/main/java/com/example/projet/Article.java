package com.example.projet;

public class Article {
    String Id , title , description;

    public Article() {}

    public Article(String id, String title, String description) {
        this.Id = id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
