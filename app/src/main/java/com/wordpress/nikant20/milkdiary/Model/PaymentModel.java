package com.wordpress.nikant20.milkdiary.Model;

/**
 * Created by nikant20 on 12/17/2017.
 */

public class PaymentModel {
    Float grandTotal;
    Float netAmountRemaining;
    Float paidAmount;
    String currentDate;
    public PaymentModel(){}

    public PaymentModel(Float grandTotal, Float netAmountRemaining, Float paidAmount, String currentDate) {
        this.grandTotal = grandTotal;
        this.netAmountRemaining = netAmountRemaining;
        this.paidAmount = paidAmount;
        this.currentDate = currentDate;
    }

    public Float getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Float grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Float getNetAmountRemaining() {
        return netAmountRemaining;
    }

    public void setNetAmountRemaining(Float netAmountRemaining) {
        this.netAmountRemaining = netAmountRemaining;
    }

    public Float getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Float paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    @Override
    public String toString() {
        return "PaymentModel{" +
                "grandTotal=" + grandTotal +
                ", netAmountRemaining=" + netAmountRemaining +
                ", paidAmount=" + paidAmount +
                ", currentDate='" + currentDate + '\'' +
                '}';
    }
}
