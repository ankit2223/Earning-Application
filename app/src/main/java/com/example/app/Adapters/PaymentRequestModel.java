package com.example.app.Adapters;

public class PaymentRequestModel {

    private String paymentMethod,paymentDetails,status,date;
    private int coins;
    private String uId;


    public PaymentRequestModel(String paymentMethod, String paymentDetails, String status, String date, int coins) {
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
        this.status = status;
        this.date = date;
        this.coins = coins;
    }

    public PaymentRequestModel() {
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
