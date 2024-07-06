package com.example.app.Models;

public class RedeemModel {

    private int coins;
    private int icon;

    public RedeemModel(int coins, int icon) {
        this.coins = coins;
        this.icon = icon;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
