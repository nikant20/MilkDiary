package com.wordpress.nikant20.milkdiary.Model;

import java.util.Date;

/**
 * Created by nikant20 on 12/6/2017.
 */

public class CostModel {
    Float milkInLitres;
    Float rate;
    String date;

    public CostModel(){}

    public CostModel(Float milkInLitres, Float rate, String date) {
        this.milkInLitres = milkInLitres;
        this.rate = rate;
        this.date = date;
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

    @Override
    public String toString() {
        return "CostModel{" +
                "milkInLitres=" + milkInLitres +
                ", rate=" + rate +
                ", date=" + date +
                '}';
    }
}