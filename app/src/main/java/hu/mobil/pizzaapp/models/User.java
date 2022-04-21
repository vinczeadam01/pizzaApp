package hu.mobil.pizzaapp.models;

import java.util.HashMap;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String photo;
    private HashMap<String, Integer> cart;

    public User(String id, String firstName, String lastName, String email, String photo, HashMap<String, Integer> cart) {
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
