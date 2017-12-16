package com.wordpress.nikant20.milkdiary.Model;

import java.util.Date;

/**
 * Created by nikant20 on 12/6/2017.
 */

public class CostModel {
    Float milkInLitres;
    Float rate;
    String date;
    Float total;

    public CostModel(){}

    public CostModel(Float milkInLitres, Float rate, String date, Float total) {
        this.milkInLitres = milkInLitres;
        this.rate = rate;
        this.date = date;
        this.total = total;
    }

    public Float getMilkInLitres() {
        return milkInLitres;
    }

    public void setMilkInLitres(Float milkInLitres) {
        this.milkInLitres = milkInLitres;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CostModel{" +
                "milkInLitres=" + milkInLitres +
                ", rate=" + rate +
                ", date='" + date + '\'' +
                ", total=" + total +
                '}';
    }
}