package com.example.matbakhy.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AreaResponse {

    @SerializedName("meals")
    private List<Area> areas;

    public AreaResponse() {
    }

    public AreaResponse(List<Area> areas) {
        this.areas = areas;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public boolean hasAreas() {
        return areas != null && !areas.isEmpty();
    }

    public List<String> getAreaNames() {
        List<String> names = new ArrayList<>();
        if (areas != null) {
            for (Area area : areas) {
                names.add(area.getStrArea());
            }
        }
        return names;
    }
}