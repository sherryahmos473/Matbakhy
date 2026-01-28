package com.example.matbakhy.data.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("idIngredient")
     private String id;
    @SerializedName("strIngredient")
    private String name;

    private String description;
    private String thumbUrl;
    private String type;

    public Ingredient() {
    }

    public Ingredient(String id, String name, String description, String thumbUrl, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbUrl = thumbUrl;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
