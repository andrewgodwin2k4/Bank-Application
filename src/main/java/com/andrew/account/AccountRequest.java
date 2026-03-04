package com.andrew.account;

public class AccountRequest {

    private String name;
    private double initialDeposit;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getInitialDeposit() { return initialDeposit; }
    public void setInitialDeposit(double initialDeposit) {
        this.initialDeposit = initialDeposit;
    }
}