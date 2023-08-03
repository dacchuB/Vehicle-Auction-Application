package com.example.project;

public class DataModel {

    private String dataTitle;
    private String dataYear;
    private Integer dataPrice;
    private String dataImage;
    private String key;
    private String highestBidder; // Add a field for the highest bidder's username
    // Add additional fields as needed

    public DataModel() {
        // Default constructor required for Firebase
    }

    public DataModel(String dataTitle, String dataYear, Integer dataPrice, String dataImage, String highestBidder) {
        this.dataTitle = dataTitle;
        this.dataYear = dataYear;
        this.dataPrice = dataPrice;
        this.dataImage = dataImage;
        this.highestBidder = highestBidder;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataYear() {
        return dataYear;
    }

    public Integer getDataPrice() {
        return dataPrice;
    }

    public String getDataImage() {
        return dataImage;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    // Add setters if necessary
}

