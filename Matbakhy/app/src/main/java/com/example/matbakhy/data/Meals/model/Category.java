package com.example.matbakhy.data.Meals.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("idCategory")
    private int id;
    @SerializedName("strCategory")
    private String name;
    @SerializedName("strCategoryDescription")
    private String desc;
    @SerializedName("strCategoryThumb")
    private String thumbnail;

    public Category() {
    }

    public Category(int id, String name, String desc, String thumbnail) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
