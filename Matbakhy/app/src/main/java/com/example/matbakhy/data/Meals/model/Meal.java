package com.example.matbakhy.data.Meals.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = "meals")
public class Meal implements Parcelable {

    @PrimaryKey
    @NonNull
    @SerializedName("idMeal")
    @ColumnInfo(name = "id")
    private String id;

    @SerializedName("strMeal")
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName("strMealAlternate")
    @ColumnInfo(name = "alternate_name")
    private String alternateName;

    @SerializedName("strCategory")
    @ColumnInfo(name = "category")
    private String category;

    @SerializedName("strArea")
    @ColumnInfo(name = "area")
    private String area;

    @SerializedName("strInstructions")
    @ColumnInfo(name = "instructions")
    private String instructions;

    @SerializedName("strMealThumb")
    @ColumnInfo(name = "thumbnail")
    private String thumbnail;

    @SerializedName("strTags")
    @ColumnInfo(name = "tags")
    private String tags;

    @SerializedName("strYoutube")
    @ColumnInfo(name = "youtube_url")
    private String youtubeUrl;

    @SerializedName("strSource")
    @ColumnInfo(name = "source_url")
    private String sourceUrl;

    @SerializedName("strImageSource")
    @ColumnInfo(name = "image_source")
    private String imageSource;

    @SerializedName("strCreativeCommonsConfirmed")
    @ColumnInfo(name = "creative_commons_confirmed")
    private String creativeCommonsConfirmed;

    @SerializedName("dateModified")
    @ColumnInfo(name = "date_modified")
    private String dateModified;

    @ColumnInfo(name = "is_favorite", defaultValue = "0")
    private boolean isFavorite;

    @ColumnInfo(name = "is_planned", defaultValue = "0")
    private boolean isPlanned;

    @ColumnInfo(name = "plan_date")
    private String planDate;

    @SerializedName("strIngredient1")
    @ColumnInfo(name = "ingredient_1")
    private String ingredient1;

    @SerializedName("strIngredient2")
    @ColumnInfo(name = "ingredient_2")
    private String ingredient2;

    @SerializedName("strIngredient3")
    @ColumnInfo(name = "ingredient_3")
    private String ingredient3;

    @SerializedName("strIngredient4")
    @ColumnInfo(name = "ingredient_4")
    private String ingredient4;

    @SerializedName("strIngredient5")
    @ColumnInfo(name = "ingredient_5")
    private String ingredient5;

    @SerializedName("strIngredient6")
    @ColumnInfo(name = "ingredient_6")
    private String ingredient6;

    @SerializedName("strIngredient7")
    @ColumnInfo(name = "ingredient_7")
    private String ingredient7;

    @SerializedName("strIngredient8")
    @ColumnInfo(name = "ingredient_8")
    private String ingredient8;

    @SerializedName("strIngredient9")
    @ColumnInfo(name = "ingredient_9")
    private String ingredient9;

    @SerializedName("strIngredient10")
    @ColumnInfo(name = "ingredient_10")
    private String ingredient10;

    @SerializedName("strIngredient11")
    @ColumnInfo(name = "ingredient_11")
    private String ingredient11;

    @SerializedName("strIngredient12")
    @ColumnInfo(name = "ingredient_12")
    private String ingredient12;

    @SerializedName("strIngredient13")
    @ColumnInfo(name = "ingredient_13")
    private String ingredient13;

    @SerializedName("strIngredient14")
    @ColumnInfo(name = "ingredient_14")
    private String ingredient14;

    @SerializedName("strIngredient15")
    @ColumnInfo(name = "ingredient_15")
    private String ingredient15;

    @SerializedName("strIngredient16")
    @ColumnInfo(name = "ingredient_16")
    private String ingredient16;

    @SerializedName("strIngredient17")
    @ColumnInfo(name = "ingredient_17")
    private String ingredient17;

    @SerializedName("strIngredient18")
    @ColumnInfo(name = "ingredient_18")
    private String ingredient18;

    @SerializedName("strIngredient19")
    @ColumnInfo(name = "ingredient_19")
    private String ingredient19;

    @SerializedName("strIngredient20")
    @ColumnInfo(name = "ingredient_20")
    private String ingredient20;

    // Measures
    @SerializedName("strMeasure1")
    @ColumnInfo(name = "measure_1")
    private String measure1;

    @SerializedName("strMeasure2")
    @ColumnInfo(name = "measure_2")
    private String measure2;

    @SerializedName("strMeasure3")
    @ColumnInfo(name = "measure_3")
    private String measure3;

    @SerializedName("strMeasure4")
    @ColumnInfo(name = "measure_4")
    private String measure4;

    @SerializedName("strMeasure5")
    @ColumnInfo(name = "measure_5")
    private String measure5;

    @SerializedName("strMeasure6")
    @ColumnInfo(name = "measure_6")
    private String measure6;

    @SerializedName("strMeasure7")
    @ColumnInfo(name = "measure_7")
    private String measure7;

    @SerializedName("strMeasure8")
    @ColumnInfo(name = "measure_8")
    private String measure8;

    @SerializedName("strMeasure9")
    @ColumnInfo(name = "measure_9")
    private String measure9;

    @SerializedName("strMeasure10")
    @ColumnInfo(name = "measure_10")
    private String measure10;

    @SerializedName("strMeasure11")
    @ColumnInfo(name = "measure_11")
    private String measure11;

    @SerializedName("strMeasure12")
    @ColumnInfo(name = "measure_12")
    private String measure12;

    @SerializedName("strMeasure13")
    @ColumnInfo(name = "measure_13")
    private String measure13;

    @SerializedName("strMeasure14")
    @ColumnInfo(name = "measure_14")
    private String measure14;

    @SerializedName("strMeasure15")
    @ColumnInfo(name = "measure_15")
    private String measure15;

    @SerializedName("strMeasure16")
    @ColumnInfo(name = "measure_16")
    private String measure16;

    @SerializedName("strMeasure17")
    @ColumnInfo(name = "measure_17")
    private String measure17;

    @SerializedName("strMeasure18")
    @ColumnInfo(name = "measure_18")
    private String measure18;

    @SerializedName("strMeasure19")
    @ColumnInfo(name = "measure_19")
    private String measure19;

    @SerializedName("strMeasure20")
    @ColumnInfo(name = "measure_20")
    private String measure20;

    public Meal() {
    }

    @Ignore
    public Meal(@NonNull String id, String name, String thumbnail, String category, String area) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.category = category;
        this.area = area;
    }

    @Ignore
    protected Meal(Parcel in) {
        id = in.readString();
        name = in.readString();
        alternateName = in.readString();
        category = in.readString();
        area = in.readString();
        instructions = in.readString();
        thumbnail = in.readString();
        tags = in.readString();
        youtubeUrl = in.readString();
        sourceUrl = in.readString();
        imageSource = in.readString();
        creativeCommonsConfirmed = in.readString();
        dateModified = in.readString();
        isFavorite = in.readByte() != 0;
        isPlanned = in.readByte() != 0;
        planDate = in.readString();

        ingredient1 = in.readString();
        ingredient2 = in.readString();
        ingredient3 = in.readString();
        ingredient4 = in.readString();
        ingredient5 = in.readString();
        ingredient6 = in.readString();
        ingredient7 = in.readString();
        ingredient8 = in.readString();
        ingredient9 = in.readString();
        ingredient10 = in.readString();
        ingredient11 = in.readString();
        ingredient12 = in.readString();
        ingredient13 = in.readString();
        ingredient14 = in.readString();
        ingredient15 = in.readString();
        ingredient16 = in.readString();
        ingredient17 = in.readString();
        ingredient18 = in.readString();
        ingredient19 = in.readString();
        ingredient20 = in.readString();

        // Measures
        measure1 = in.readString();
        measure2 = in.readString();
        measure3 = in.readString();
        measure4 = in.readString();
        measure5 = in.readString();
        measure6 = in.readString();
        measure7 = in.readString();
        measure8 = in.readString();
        measure9 = in.readString();
        measure10 = in.readString();
        measure11 = in.readString();
        measure12 = in.readString();
        measure13 = in.readString();
        measure14 = in.readString();
        measure15 = in.readString();
        measure16 = in.readString();
        measure17 = in.readString();
        measure18 = in.readString();
        measure19 = in.readString();
        measure20 = in.readString();
    }

    // Parcelable Creator
    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(alternateName);
        dest.writeString(category);
        dest.writeString(area);
        dest.writeString(instructions);
        dest.writeString(thumbnail);
        dest.writeString(tags);
        dest.writeString(youtubeUrl);
        dest.writeString(sourceUrl);
        dest.writeString(imageSource);
        dest.writeString(creativeCommonsConfirmed);
        dest.writeString(dateModified);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeByte((byte) (isPlanned ? 1 : 0));
        dest.writeString(planDate);
        dest.writeString(ingredient1);
        dest.writeString(ingredient2);
        dest.writeString(ingredient3);
        dest.writeString(ingredient4);
        dest.writeString(ingredient5);
        dest.writeString(ingredient6);
        dest.writeString(ingredient7);
        dest.writeString(ingredient8);
        dest.writeString(ingredient9);
        dest.writeString(ingredient10);
        dest.writeString(ingredient11);
        dest.writeString(ingredient12);
        dest.writeString(ingredient13);
        dest.writeString(ingredient14);
        dest.writeString(ingredient15);
        dest.writeString(ingredient16);
        dest.writeString(ingredient17);
        dest.writeString(ingredient18);
        dest.writeString(ingredient19);
        dest.writeString(ingredient20);

        dest.writeString(measure1);
        dest.writeString(measure2);
        dest.writeString(measure3);
        dest.writeString(measure4);
        dest.writeString(measure5);
        dest.writeString(measure6);
        dest.writeString(measure7);
        dest.writeString(measure8);
        dest.writeString(measure9);
        dest.writeString(measure10);
        dest.writeString(measure11);
        dest.writeString(measure12);
        dest.writeString(measure13);
        dest.writeString(measure14);
        dest.writeString(measure15);
        dest.writeString(measure16);
        dest.writeString(measure17);
        dest.writeString(measure18);
        dest.writeString(measure19);
        dest.writeString(measure20);
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getCreativeCommonsConfirmed() {
        return creativeCommonsConfirmed;
    }

    public void setCreativeCommonsConfirmed(String creativeCommonsConfirmed) {
        this.creativeCommonsConfirmed = creativeCommonsConfirmed;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isPlanned() {
        return isPlanned;
    }

    public void setPlanned(boolean planned) {
        isPlanned = planned;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }


    public String getIngredient1() {
        return ingredient1;
    }

    public void setIngredient1(String ingredient1) {
        this.ingredient1 = ingredient1;
    }

    public String getIngredient2() {
        return ingredient2;
    }

    public void setIngredient2(String ingredient2) {
        this.ingredient2 = ingredient2;
    }

    public String getIngredient3() {
        return ingredient3;
    }

    public void setIngredient3(String ingredient3) {
        this.ingredient3 = ingredient3;
    }

    public String getIngredient4() {
        return ingredient4;
    }

    public void setIngredient4(String ingredient4) {
        this.ingredient4 = ingredient4;
    }

    public String getIngredient5() {
        return ingredient5;
    }

    public void setIngredient5(String ingredient5) {
        this.ingredient5 = ingredient5;
    }

    public String getIngredient6() {
        return ingredient6;
    }

    public void setIngredient6(String ingredient6) {
        this.ingredient6 = ingredient6;
    }

    public String getIngredient7() {
        return ingredient7;
    }

    public void setIngredient7(String ingredient7) {
        this.ingredient7 = ingredient7;
    }

    public String getIngredient8() {
        return ingredient8;
    }

    public void setIngredient8(String ingredient8) {
        this.ingredient8 = ingredient8;
    }

    public String getIngredient9() {
        return ingredient9;
    }

    public void setIngredient9(String ingredient9) {
        this.ingredient9 = ingredient9;
    }

    public String getIngredient10() {
        return ingredient10;
    }

    public void setIngredient10(String ingredient10) {
        this.ingredient10 = ingredient10;
    }

    public String getIngredient11() {
        return ingredient11;
    }

    public void setIngredient11(String ingredient11) {
        this.ingredient11 = ingredient11;
    }

    public String getIngredient12() {
        return ingredient12;
    }

    public void setIngredient12(String ingredient12) {
        this.ingredient12 = ingredient12;
    }

    public String getIngredient13() {
        return ingredient13;
    }

    public void setIngredient13(String ingredient13) {
        this.ingredient13 = ingredient13;
    }

    public String getIngredient14() {
        return ingredient14;
    }

    public void setIngredient14(String ingredient14) {
        this.ingredient14 = ingredient14;
    }

    public String getIngredient15() {
        return ingredient15;
    }

    public void setIngredient15(String ingredient15) {
        this.ingredient15 = ingredient15;
    }

    public String getIngredient16() {
        return ingredient16;
    }

    public void setIngredient16(String ingredient16) {
        this.ingredient16 = ingredient16;
    }

    public String getIngredient17() {
        return ingredient17;
    }

    public void setIngredient17(String ingredient17) {
        this.ingredient17 = ingredient17;
    }

    public String getIngredient18() {
        return ingredient18;
    }

    public void setIngredient18(String ingredient18) {
        this.ingredient18 = ingredient18;
    }

    public String getIngredient19() {
        return ingredient19;
    }

    public void setIngredient19(String ingredient19) {
        this.ingredient19 = ingredient19;
    }

    public String getIngredient20() {
        return ingredient20;
    }

    public void setIngredient20(String ingredient20) {
        this.ingredient20 = ingredient20;
    }

    public String getMeasure1() {
        return measure1;
    }

    public void setMeasure1(String measure1) {
        this.measure1 = measure1;
    }

    public String getMeasure2() {
        return measure2;
    }

    public void setMeasure2(String measure2) {
        this.measure2 = measure2;
    }

    public String getMeasure3() {
        return measure3;
    }

    public void setMeasure3(String measure3) {
        this.measure3 = measure3;
    }

    public String getMeasure4() {
        return measure4;
    }

    public void setMeasure4(String measure4) {
        this.measure4 = measure4;
    }

    public String getMeasure5() {
        return measure5;
    }

    public void setMeasure5(String measure5) {
        this.measure5 = measure5;
    }

    public String getMeasure6() {
        return measure6;
    }

    public void setMeasure6(String measure6) {
        this.measure6 = measure6;
    }

    public String getMeasure7() {
        return measure7;
    }

    public void setMeasure7(String measure7) {
        this.measure7 = measure7;
    }

    public String getMeasure8() {
        return measure8;
    }

    public void setMeasure8(String measure8) {
        this.measure8 = measure8;
    }

    public String getMeasure9() {
        return measure9;
    }

    public void setMeasure9(String measure9) {
        this.measure9 = measure9;
    }

    public String getMeasure10() {
        return measure10;
    }

    public void setMeasure10(String measure10) {
        this.measure10 = measure10;
    }

    public String getMeasure11() {
        return measure11;
    }

    public void setMeasure11(String measure11) {
        this.measure11 = measure11;
    }

    public String getMeasure12() {
        return measure12;
    }

    public void setMeasure12(String measure12) {
        this.measure12 = measure12;
    }

    public String getMeasure13() {
        return measure13;
    }

    public void setMeasure13(String measure13) {
        this.measure13 = measure13;
    }

    public String getMeasure14() {
        return measure14;
    }

    public void setMeasure14(String measure14) {
        this.measure14 = measure14;
    }

    public String getMeasure15() {
        return measure15;
    }

    public void setMeasure15(String measure15) {
        this.measure15 = measure15;
    }

    public String getMeasure16() {
        return measure16;
    }

    public void setMeasure16(String measure16) {
        this.measure16 = measure16;
    }

    public String getMeasure17() {
        return measure17;
    }

    public void setMeasure17(String measure17) {
        this.measure17 = measure17;
    }

    public String getMeasure18() {
        return measure18;
    }

    public void setMeasure18(String measure18) {
        this.measure18 = measure18;
    }

    public String getMeasure19() {
        return measure19;
    }

    public void setMeasure19(String measure19) {
        this.measure19 = measure19;
    }

    public String getMeasure20() {
        return measure20;
    }

    public void setMeasure20(String measure20) {
        this.measure20 = measure20;
    }

    public List<String> getIngredients() {
        List<String> ingredients = new ArrayList<>();
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
        List<String> measures = new ArrayList<>();
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

    public List<String> getIngredientsWithMeasures() {
        List<String> ingredients = getIngredients();
        List<String> measures = getMeasures();
        List<String> result = new ArrayList<>();

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


    @Override
    public String toString() {
        return "Meal{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", area='" + area + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
    public Map<String, Object> toFirebaseMap(String userId, String userEmail) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("alternate_name", alternateName);
        map.put("category", category);
        map.put("area", area);
        map.put("instructions", instructions);
        map.put("thumbnail", thumbnail);
        map.put("tags", tags);
        map.put("youtube_url", youtubeUrl);
        map.put("source_url", sourceUrl);
        map.put("image_source", imageSource);
        map.put("creative_commons_confirmed", creativeCommonsConfirmed);
        map.put("date_modified", dateModified);
        map.put("is_favorite", isFavorite);
        map.put("is_planned", isPlanned);
        map.put("plan_date", planDate);
        map.put("user_id", userId);
        map.put("user_email", userEmail);
        map.put("last_backup", System.currentTimeMillis());

        for (int i = 1; i <= 20; i++) {
            try {
                String ingredientField = "ingredient_" + i;
                String measureField = "measure_" + i;

                java.lang.reflect.Field ingredientFieldRef = getClass().getDeclaredField("ingredient" + i);
                java.lang.reflect.Field measureFieldRef = getClass().getDeclaredField("measure" + i);

                ingredientFieldRef.setAccessible(true);
                measureFieldRef.setAccessible(true);

                String ingredient = (String) ingredientFieldRef.get(this);
                String measure = (String) measureFieldRef.get(this);

                if (ingredient != null && !ingredient.trim().isEmpty()) {
                    map.put(ingredientField, ingredient.trim());
                }
                if (measure != null && !measure.trim().isEmpty()) {
                    map.put(measureField, measure.trim());
                }
            } catch (Exception e) {
            }
        }

        return map;
    }

    public FirebaseMeal toFirebaseMeal(String userId, String userEmail) {
        return new FirebaseMeal(this, userId, userEmail);
    }

    public Map<String, Object> toCompactFirebaseMap(String userId, String userEmail) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("category", category);
        map.put("area", area);
        map.put("thumbnail", thumbnail);
        map.put("is_favorite", isFavorite);
        map.put("user_id", userId);
        map.put("user_email", userEmail);
        map.put("backup_timestamp", System.currentTimeMillis());

        List<String> ingredientsList = getIngredients();
        if (!ingredientsList.isEmpty()) {
            map.put("ingredients", ingredientsList);
        }

        return map;
    }
    public Map<String, String> getIngredientsAsMap() {
        Map<String, String> ingredientMap = new HashMap<>();
        List<String> ingredients = getIngredients();
        List<String> measures = getMeasures();

        int size = Math.min(ingredients.size(), measures.size());
        for (int i = 0; i < size; i++) {
            String ingredient = ingredients.get(i);
            String measure = measures.get(i);
            if (!ingredient.isEmpty()) {
                ingredientMap.put(ingredient, measure != null ? measure : "");
            }
        }
        return ingredientMap;
    }

}