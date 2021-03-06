package com.wordpress.nikant20.milkdiary.Model;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nikant20 on 11/7/2017.
 */

public class User {
    String userId;
    String name;
    String email;
    String address;
    String phone;
    String typeofUser;
    String image;

    public User(){}

    public User(String name, String email, String address, String phone, String typeofUser, String image) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.typeofUser = typeofUser;
        this.image = image;
    }
    public User(String name, String email, String address, String phone,String image) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTypeofUser() {
        return typeofUser;
    }

    public void setTypeofUser(String typeofUser) {
        this.typeofUser = typeofUser;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", typeofUser='" + typeofUser + '\'' +
                ", image='" + image + '\'' +
                '}';
    }


}
