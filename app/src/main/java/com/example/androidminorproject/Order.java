package com.example.androidminorproject;

import java.util.List;

public class Order {
    private String orderId;
    private String utrNumber;
    private int total;
    private String date;
    private String status;
    private List<String> productNames;
    private List<Integer> productQuantities;
    private List<Integer> productTotals;

    public Order(String orderId, String utrNumber, int total, String date, String status, List<String> productNames, List<Integer> productQuantities, List<Integer> productTotals) {
        this.orderId = orderId;
        this.utrNumber = utrNumber;
        this.total = total;
        this.date = date;
        this.status = status;
        this.productNames = productNames;
        this.productQuantities = productQuantities;
        this.productTotals = productTotals;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUtrNumber() {
        return utrNumber;
    }

    public int getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getProductNames() {
        return productNames;
    }

    public List<Integer> getProductQuantities() {
        return productQuantities;
    }

    public List<Integer> getProductTotals() {
        return productTotals;
    }
}
