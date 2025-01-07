package com.example.mysignlanguege.model;

public class Business {

    String id;
    String name,category,phone,email,street,website,area,city,details;

    String image;

    User admin;



    public Business(String id, String name, String category, String phone, String email, String street, String website, String area, String city, String details, String image, User admin) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.phone = phone;
        this.email = email;
        this.street = street;
        this.website = website;
        this.area = area;
        this.city = city;
        this.details = details;
        this.image = image;
        this.admin = admin;
    }
    public Business() {
    }
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
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
                ", area='" + area + '\'' +
                ", city='" + city + '\'' +
                ", details='" + details + '\'' +
                ", image='" + image + '\'' +
                ", admin=" + admin +
                '}';
    }
}
