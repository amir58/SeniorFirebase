package com.amirmohammed.seniorfirebase;

public class OrderData {
    private String orderId, userId,
            description, phone,
            firstLocation, lastLocation,
            date, time;

    public OrderData(String orderId, String userId,
                     String description, String phone,
                     String firstLocation, String lastLocation,
                     String date, String time) {
        this.orderId = orderId;
        this.userId = userId;
        this.description = description;
        this.phone = phone;
        this.firstLocation = firstLocation;
        this.lastLocation = lastLocation;
        this.date = date;
        this.time = time;
    }

    public OrderData() {
    }

    @Override
    public String toString() {
        return "OrderData{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", firstLocation='" + firstLocation + '\'' +
                ", lastLocation='" + lastLocation + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstLocation() {
        return firstLocation;
    }

    public void setFirstLocation(String firstLocation) {
        this.firstLocation = firstLocation;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
