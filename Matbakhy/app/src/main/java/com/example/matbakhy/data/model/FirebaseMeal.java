package com.example.matbakhy.data.model;

import com.google.firebase.database.PropertyName;

import java.util.Map;

public class FirebaseMeal {
    @PropertyName("id")
    private String id;

    @PropertyName("name")
    private String name;

    @PropertyName("category")
    private String category;

    @PropertyName("area")
    private String area;

    @PropertyName("instructions")
    private String instructions;

    @PropertyName("thumbnail")
    private String thumbnail;

    @PropertyName("youtube_url")
    private String youtubeUrl;

    @PropertyName("is_favorite")
    private boolean isFavorite;

    @PropertyName("is_planned")
    private boolean isPlanned;

    @PropertyName("plan_date")
    private String planDate;

    @PropertyName("user_id")
    private String userId;

    @PropertyName("user_email")
    private String userEmail;

    @PropertyName("ingredients")
    private Map<String, String> ingredients;

    @PropertyName("backup_timestamp")
    private long backupTimestamp;

    public FirebaseMeal() {
    }

    public FirebaseMeal(Meal meal, String userId, String userEmail) {
        this.id = meal.getId();
        this.name = meal.getName();
        this.category = meal.getCategory();
        this.area = meal.getArea();
        this.instructions = meal.getInstructions();
        this.thumbnail = meal.getThumbnail();
        this.youtubeUrl = meal.getYoutubeUrl();
        this.isFavorite = meal.isFavorite();
        this.isPlanned = meal.isPlanned();
        this.planDate = meal.getPlanDate();
        this.userId = userId;
        this.userEmail = userEmail;
        this.backupTimestamp = System.currentTimeMillis();
        this.ingredients = meal.getIngredientsAsMap();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getYoutubeUrl() { return youtubeUrl; }
    public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }

    @PropertyName("is_favorite")
    public boolean isFavorite() { return isFavorite; }

    @PropertyName("is_favorite")
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    @PropertyName("is_planned")
    public boolean isPlanned() { return isPlanned; }

    @PropertyName("is_planned")
    public void setPlanned(boolean planned) { isPlanned = planned; }

    @PropertyName("plan_date")
    public String getPlanDate() { return planDate; }

    @PropertyName("plan_date")
    public void setPlanDate(String planDate) { this.planDate = planDate; }

    @PropertyName("user_id")
    public String getUserId() { return userId; }

    @PropertyName("user_id")
    public void setUserId(String userId) { this.userId = userId; }

    @PropertyName("user_email")
    public String getUserEmail() { return userEmail; }

    @PropertyName("user_email")
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Map<String, String> getIngredients() { return ingredients; }
    public void setIngredients(Map<String, String> ingredients) { this.ingredients = ingredients; }

    @PropertyName("backup_timestamp")
    public long getBackupTimestamp() { return backupTimestamp; }

    @PropertyName("backup_timestamp")
    public void setBackupTimestamp(long backupTimestamp) { this.backupTimestamp = backupTimestamp; }
}