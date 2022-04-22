package hu.mobil.pizzaapp.models;

import java.io.Serializable;
import java.util.HashMap;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String photo;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private HashMap<String, Integer> cart;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public User(String id, String firstName, String lastName, String email, String address, String photo, HashMap<String, Integer> cart) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.photo = photo;
        this.cart = cart;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public HashMap<String, Integer> getCart() {
        return cart;
    }


}
