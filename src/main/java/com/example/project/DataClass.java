package com.example.project;


public class DataClass {
    private String dataTitle;
    private String dataYear;
    private Integer dataPrice;
    private String dataImage;
    private String key;
    private User Username;

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

    public DataClass(String dataTitle, String dataYear, Integer dataPrice, String dataImage,User Username) {
        this.dataTitle = dataTitle;
        this.dataYear = dataYear;
        this.dataPrice = dataPrice;
        this.dataImage = dataImage;
        this.Username = Username;
    }

    public DataClass() {
    }

    public User getUsername() {
        return Username;
    }

    public void setUsername(User Username) {
        this.Username = Username;
    }
}
