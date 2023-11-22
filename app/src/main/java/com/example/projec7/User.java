package com.example.projec7;

import java.io.Serializable;
public class User implements Serializable{
    public String title;
    public String content;
    public String local_category;
    public String theme_category;
    public String image;

    public User(String title, String content, String local_category, String theme_category, String image) {
        this.title=title;
        this.content=content;
        this.local_category=local_category;
        this.theme_category=theme_category;
        this.image=image;


    }

}
