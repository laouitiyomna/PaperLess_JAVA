package com.java.paperless_miniprojet.model;


public class Objectif {

    private int    id;
    private String departement;
    private int    valeur;
    private String mois;

    public Objectif() {}

    public Objectif(int id, String departement, int valeur, String mois) {
        this.id          = id;
        this.departement = departement;
        this.valeur      = valeur;
        this.mois        = mois;
    }

    public int    getId()          { return id; }
    public String getDepartement() { return departement; }
    public int    getValeur()      { return valeur; }
    public String getMois()        { return mois; }

    public void setId(int id)            { this.id = id; }
    public void setDepartement(String d) { this.departement = d; }
    public void setValeur(int valeur)    { this.valeur = valeur; }
    public void setMois(String mois)     { this.mois = mois; }

    // Méthode métier : vérifier si dépassé
    public boolean estDepasse(int consommation) {
        return consommation > this.valeur;
    }

    // Méthode métier : calculer l'écart
    public int calculerEcart(int consommation) {
        return consommation - this.valeur;
    }

    @Override
    public String toString() {
        return "Objectif{dept=" + departement + ", valeur=" + valeur + ", mois=" + mois + "}";
    }
}