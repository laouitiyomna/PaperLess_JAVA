package com.java.paperless_miniprojet.model;



public class Consommation {

    private int    id;
    private String departement;
    private int    quantite;
    private String date;

    public Consommation() {}

    public Consommation(int id, String departement, int quantite, String date) {
        this.id          = id;
        this.departement = departement;
        this.quantite    = quantite;
        this.date        = date;
    }

    public int    getId()          { return id; }
    public String getDepartement() { return departement; }
    public int    getQuantite()    { return quantite; }
    public String getDate()        { return date; }

    public void setId(int id)                      { this.id = id; }
    public void setDepartement(String departement) { this.departement = departement; }
    public void setQuantite(int quantite)          { this.quantite = quantite; }
    public void setDate(String date)               { this.date = date; }

    @Override
    public String toString() {
        return "Consommation{id=" + id + ", dept=" + departement + ", qte=" + quantite + "}";
    }
}