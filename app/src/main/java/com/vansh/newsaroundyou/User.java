package com.vansh.newsaroundyou;

import java.util.ArrayList;

public class User {

    private String email;
    private ArrayList<NewsModel> savedArticles;
    private String language;
    private String region;
    private ArrayList<String> categories;
    public final static String ENGLISH = "en";
    public final static String SINGAPORE = "sg";

    public User(String email) {
        this.email = email;
        this.language = ENGLISH;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<NewsModel> getSavedArticles() {
        return savedArticles;
    }

    public void setSavedArticles(ArrayList<NewsModel> savedArticles) {
        this.savedArticles = savedArticles;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}
