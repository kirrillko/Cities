package com.example.lab2cities;

public class Place {
    private int place_id;
    private String city;
    private String category;
    private String name;
    private String description;
    private String address;
    private String phone_number;
    private String website;
    private double latitude;
    private double longitude;
    private boolean is_favorite;

    public Place(int placeID, String city, String category, String name, String description, String address, String phoneNumber, String website, double latitude, double longitude, boolean isFavorite) {
        this.place_id = placeID;
        this.city = city;
        this.category = category;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone_number = phoneNumber;
        this.website = website;
        this.latitude = latitude;
        this.longitude = longitude;
        this.is_favorite = isFavorite;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }
}
