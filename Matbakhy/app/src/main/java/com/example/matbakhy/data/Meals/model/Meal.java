package com.example.matbakhy.data.Meals.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Meal {
    @SerializedName("idMeal")
    private String id;

    @SerializedName("strMeal")
    private String name;

    @SerializedName("strMealAlternate")
    private String alternateName;

    @SerializedName("strCategory")
    private String category;

    @SerializedName("strArea")
    private String area;

    @SerializedName("strInstructions")
    private String instructions;

    @SerializedName("strMealThumb")
    private String thumbnail;

    @SerializedName("strTags")
    private String tags;

    @SerializedName("strYoutube")
    private String youtubeUrl;

    @SerializedName("strSource")
    private String sourceUrl;

    @SerializedName("strImageSource")
    private String imageSource;

    @SerializedName("strCreativeCommonsConfirmed")
    private String creativeCommonsConfirmed;

    @SerializedName("dateModified")
    private String dateModified;

    @SerializedName("strIngredient1") private String ingredient1;
    @SerializedName("strIngredient2") private String ingredient2;
    @SerializedName("strIngredient3") private String ingredient3;
    @SerializedName("strIngredient4") private String ingredient4;
    @SerializedName("strIngredient5") private String ingredient5;
    @SerializedName("strIngredient6") private String ingredient6;
    @SerializedName("strIngredient7") private String ingredient7;
    @SerializedName("strIngredient8") private String ingredient8;
    @SerializedName("strIngredient9") private String ingredient9;
    @SerializedName("strIngredient10") private String ingredient10;
    @SerializedName("strIngredient11") private String ingredient11;
    @SerializedName("strIngredient12") private String ingredient12;
    @SerializedName("strIngredient13") private String ingredient13;
    @SerializedName("strIngredient14") private String ingredient14;
    @SerializedName("strIngredient15") private String ingredient15;
    @SerializedName("strIngredient16") private String ingredient16;
    @SerializedName("strIngredient17") private String ingredient17;
    @SerializedName("strIngredient18") private String ingredient18;
    @SerializedName("strIngredient19") private String ingredient19;
    @SerializedName("strIngredient20") private String ingredient20;

    @SerializedName("strMeasure1") private String measure1;
    @SerializedName("strMeasure2") private String measure2;
    @SerializedName("strMeasure3") private String measure3;
    @SerializedName("strMeasure4") private String measure4;
    @SerializedName("strMeasure5") private String measure5;
    @SerializedName("strMeasure6") private String measure6;
    @SerializedName("strMeasure7") private String measure7;
    @SerializedName("strMeasure8") private String measure8;
    @SerializedName("strMeasure9") private String measure9;
    @SerializedName("strMeasure10") private String measure10;
    @SerializedName("strMeasure11") private String measure11;
    @SerializedName("strMeasure12") private String measure12;
    @SerializedName("strMeasure13") private String measure13;
    @SerializedName("strMeasure14") private String measure14;
    @SerializedName("strMeasure15") private String measure15;
    @SerializedName("strMeasure16") private String measure16;
    @SerializedName("strMeasure17") private String measure17;
    @SerializedName("strMeasure18") private String measure18;
    @SerializedName("strMeasure19") private String measure19;
    @SerializedName("strMeasure20") private String measure20;

    // Constructors
    public Meal() {
    }

    public Meal(String id, String name, String thumbnail, String category, String area) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.category = category;
        this.area = area;
    }

    // Getters and Setters
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

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public List<String> getIngredients() {
        List<String> ingredients = new java.util.ArrayList<>();
        if (ingredient1 != null && !ingredient1.trim().isEmpty()) ingredients.add(ingredient1.trim());
        if (ingredient2 != null && !ingredient2.trim().isEmpty()) ingredients.add(ingredient2.trim());
        if (ingredient3 != null && !ingredient3.trim().isEmpty()) ingredients.add(ingredient3.trim());
        if (ingredient4 != null && !ingredient4.trim().isEmpty()) ingredients.add(ingredient4.trim());
        if (ingredient5 != null && !ingredient5.trim().isEmpty()) ingredients.add(ingredient5.trim());
        if (ingredient6 != null && !ingredient6.trim().isEmpty()) ingredients.add(ingredient6.trim());
        if (ingredient7 != null && !ingredient7.trim().isEmpty()) ingredients.add(ingredient7.trim());
        if (ingredient8 != null && !ingredient8.trim().isEmpty()) ingredients.add(ingredient8.trim());
        if (ingredient9 != null && !ingredient9.trim().isEmpty()) ingredients.add(ingredient9.trim());
        if (ingredient10 != null && !ingredient10.trim().isEmpty()) ingredients.add(ingredient10.trim());
        if (ingredient11 != null && !ingredient11.trim().isEmpty()) ingredients.add(ingredient11.trim());
        if (ingredient12 != null && !ingredient12.trim().isEmpty()) ingredients.add(ingredient12.trim());
        if (ingredient13 != null && !ingredient13.trim().isEmpty()) ingredients.add(ingredient13.trim());
        if (ingredient14 != null && !ingredient14.trim().isEmpty()) ingredients.add(ingredient14.trim());
        if (ingredient15 != null && !ingredient15.trim().isEmpty()) ingredients.add(ingredient15.trim());
        if (ingredient16 != null && !ingredient16.trim().isEmpty()) ingredients.add(ingredient16.trim());
        if (ingredient17 != null && !ingredient17.trim().isEmpty()) ingredients.add(ingredient17.trim());
        if (ingredient18 != null && !ingredient18.trim().isEmpty()) ingredients.add(ingredient18.trim());
        if (ingredient19 != null && !ingredient19.trim().isEmpty()) ingredients.add(ingredient19.trim());
        if (ingredient20 != null && !ingredient20.trim().isEmpty()) ingredients.add(ingredient20.trim());
        return ingredients;
    }

    public List<String> getMeasures() {
        List<String> measures = new java.util.ArrayList<>();
        if (measure1 != null && !measure1.trim().isEmpty()) measures.add(measure1.trim());
        if (measure2 != null && !measure2.trim().isEmpty()) measures.add(measure2.trim());
        if (measure3 != null && !measure3.trim().isEmpty()) measures.add(measure3.trim());
        if (measure4 != null && !measure4.trim().isEmpty()) measures.add(measure4.trim());
        if (measure5 != null && !measure5.trim().isEmpty()) measures.add(measure5.trim());
        if (measure6 != null && !measure6.trim().isEmpty()) measures.add(measure6.trim());
        if (measure7 != null && !measure7.trim().isEmpty()) measures.add(measure7.trim());
        if (measure8 != null && !measure8.trim().isEmpty()) measures.add(measure8.trim());
        if (measure9 != null && !measure9.trim().isEmpty()) measures.add(measure9.trim());
        if (measure10 != null && !measure10.trim().isEmpty()) measures.add(measure10.trim());
        if (measure11 != null && !measure11.trim().isEmpty()) measures.add(measure11.trim());
        if (measure12 != null && !measure12.trim().isEmpty()) measures.add(measure12.trim());
        if (measure13 != null && !measure13.trim().isEmpty()) measures.add(measure13.trim());
        if (measure14 != null && !measure14.trim().isEmpty()) measures.add(measure14.trim());
        if (measure15 != null && !measure15.trim().isEmpty()) measures.add(measure15.trim());
        if (measure16 != null && !measure16.trim().isEmpty()) measures.add(measure16.trim());
        if (measure17 != null && !measure17.trim().isEmpty()) measures.add(measure17.trim());
        if (measure18 != null && !measure18.trim().isEmpty()) measures.add(measure18.trim());
        if (measure19 != null && !measure19.trim().isEmpty()) measures.add(measure19.trim());
        if (measure20 != null && !measure20.trim().isEmpty()) measures.add(measure20.trim());
        return measures;
    }

    // Helper method to get ingredients with measures as a list of pairs
    public List<String> getIngredientsWithMeasures() {
        List<String> ingredients = getIngredients();
        List<String> measures = getMeasures();
        List<String> result = new java.util.ArrayList<>();

        int size = Math.min(ingredients.size(), measures.size());
        for (int i = 0; i < size; i++) {
            String ingredient = ingredients.get(i);
            String measure = measures.get(i);
            if (!ingredient.isEmpty() && !measure.isEmpty()) {
                result.add(measure + " " + ingredient);
            }
        }
        return result;
    }
}