package com.example.acikocak;

import java.util.ArrayList;

public class ProductionDetails {
    public String shift;
    public String markup;
    public String weigher;
    public String ore;
    public String destination;
    public ArrayList<String> trucks;

    public ProductionDetails() {
        // Default constructor for Firebase
    }

    public ProductionDetails(String shift, String markup, String weigher, String ore, String destination, ArrayList<String> trucks) {
        this.shift = shift;
        this.markup = markup;
        this.weigher = weigher;
        this.ore = ore;
        this.destination = destination;
        this.trucks = trucks;
    }

    @Override
    public String toString() {
        return "Vardiya: " + shift +
               "\nMarkup: " + markup +
               "\nKantarcı: " + weigher +
               "\nCevher: " + ore +
               "\nGönderilen Yer: " + destination +
               "\nKamyonlar: " + trucks;
    }
}