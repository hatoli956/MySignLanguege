package com.example.mysignlanguege.model;

public class Business {
    String Name,Category,Phone,Email,Residence,Site;

    public Business(String name, String category, String phone, String email, String residence, String site) {
        Name = name;
        Category = category;
        Phone = phone;
        Email = email;
        Residence = residence;
        Site = site;
    }

    public Business() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getResidence() {
        return Residence;
    }

    public void setResidence(String residence) {
        Residence = residence;
    }

    public String getSite() {
        return Site;
    }

    public void setSite(String site) {
        Site = site;
    }


    @Override
    public String toString() {
        return "Business{" +
                "Name='" + Name + '\'' +
                ", Category='" + Category + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Email='" + Email + '\'' +
                ", Residence='" + Residence + '\'' +
                ", Site='" + Site + '\'' +
                '}';
    }
}
