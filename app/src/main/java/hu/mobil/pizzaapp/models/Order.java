package hu.mobil.pizzaapp.models;

import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private String userId;
    private String orderlist;
    private int price;

    public Order() {
    }

    public Order(String userId, String orderlist, int price) {
        this.userId = userId;
        this.orderlist = orderlist;
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderlist() {
        return orderlist;
    }

    public int getPrice() {
        return price;
    }

    public static String list2string(List<Food> list) {
        StringBuilder res = new StringBuilder();
        for(Food food: list) {
            res.append(food.getName()).append(", ");
        }
        return res.toString();
    }
}
