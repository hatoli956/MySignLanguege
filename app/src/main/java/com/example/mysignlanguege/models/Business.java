package com.example.mysignlanguege.models;

import java.io.Serializable;

public class Business implements Serializable {

    protected String id;
    protected String name, category, phone, email, street, website, city, details;
    protected String image;     // נתיב התמונה ב-Storage
    protected String imageUrl;  // כתובת URL לתמונה מ-Firebase Storage
    protected User admin;
    protected String ownerId;

    public Business() {
        // Default constructor
    }

    public Business(String id, String name, String category, String phone, String email, String street,
                    String website, String city, String details, String image) {
        this(id, name, category, phone, email, street, website, city, details, image, null, null);
    }

    public Business(String id, String name, String category, String phone, String email, String street,
                    String website, String city, String details, String image, User admin, String ownerId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.phone = phone;
        this.email = email;
        this.street = street;
        this.website = website;
        this.city = city;
        this.details = details;
        this.image = image;
        this.admin = admin;
        this.ownerId = ownerId;
    }

    public Business(Business business) {
        this.id = business.id;
        this.name = business.name;
        this.category = business.category;
        this.phone = business.phone;
        this.email = business.email;
        this.street = business.street;
        this.website = business.website;
        this.city = business.city;
        this.details = business.details;
        this.image = business.image;
        this.imageUrl = business.imageUrl;
        this.admin = business.admin;
        this.ownerId = business.ownerId;
    }

    // Getters and setters

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Business{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", street='" + street + '\'' +
                ", website='" + website + '\'' +
                ", city='" + city + '\'' +
                ", details='" + details + '\'' +
                ", image='" + image + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", admin=" + admin +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }
}
