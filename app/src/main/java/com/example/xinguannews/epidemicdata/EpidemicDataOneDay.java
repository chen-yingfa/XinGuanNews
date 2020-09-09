package com.example.xinguannews.epidemicdata;

public class EpidemicDataOneDay {
    Integer confirmed;
    Integer suspected;
    Integer cured;
    Integer dead;
    Integer severe;
    Integer risk;
    Integer inc24;
    public EpidemicDataOneDay() {}
    public EpidemicDataOneDay(Integer confirmed, Integer suspected, Integer cured,
                              Integer dead, Integer severe, Integer risk, Integer inc24) {
        this.confirmed = confirmed;
        this.suspected = suspected;
        this.cured = cured;
        this.dead = dead;
        this.severe = severe;
        this.risk = risk;
        this.inc24 = inc24;
    }

    public String toString() {
        return confirmed + " " + suspected + " " + cured + " " + dead + " " + severe + " " + risk + " " + inc24;
    }
}
