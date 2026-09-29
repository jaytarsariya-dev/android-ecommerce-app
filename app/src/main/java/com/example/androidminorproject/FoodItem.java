//package com.example.androidminorproject;
//
//public class FoodItem {
//    private String foodId;
//    private String foodName;
//    private String foodPrice;
//    private String foodDescription;
//    private String imageUrl;
//
//    // Empty constructor for Firestore
//    public FoodItem() {
//    }
//
//    public FoodItem(String foodId, String foodName, String foodPrice, String foodDescription, String imageUrl) {
//        this.foodId = foodId;
//        this.foodName = foodName;
//        this.foodPrice = foodPrice;
//        this.foodDescription = foodDescription;
//        this.imageUrl = imageUrl;
//    }
//
//    // Getters and setters
//    public String getFoodId() {
//        return foodId;
//    }
//
//    public void setFoodId(String foodId) {
//        this.foodId = foodId;
//    }
//
//    public String getFoodName() {
//        return foodName;
//    }
//
//    public void setFoodName(String foodName) {
//        this.foodName = foodName;
//    }
//
//    public String getFoodPrice() {
//        return foodPrice;
//    }
//
//    public void setFoodPrice(String foodPrice) {
//        this.foodPrice = foodPrice;
//    }
//
//    public String getFoodDescription() {
//        return foodDescription;
//    }
//
//    public void setFoodDescription(String foodDescription) {
//        this.foodDescription = foodDescription;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//}
//
//

package com.example.androidminorproject;

public class FoodItem {
    private String foodName;
    private String foodPrice;
    private String foodDescription;
    private String imageUrl;
    private String foodId;

    public FoodItem() {
        // No-arg constructor required
    }

    public FoodItem(String foodId, String foodName, String foodPrice, String foodDescription, String imageUrl) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDescription = foodDescription;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}

