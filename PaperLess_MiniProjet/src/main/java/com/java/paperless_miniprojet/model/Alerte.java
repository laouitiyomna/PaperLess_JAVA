package com.java.paperless_miniprojet.model;


public class Alerte {

    private int    id;
    private String departement;
    private int    consomme;
    private int    objectif;
    private int    ecart;
    private String statut;

    public Alerte() {}

    public Alerte(int id, String departement, int consomme, int objectif, int ecart, String statut) {
        this.id          = id;
        this.departement = departement;
        this.consomme    = consomme;
        this.objectif    = objectif;
        this.ecart       = ecart;
        this.statut      = statut;
    }

    public int    getId()          { return id; }
    public String getDepartement() { return departement; }
    public int    getConsomme()    { return consomme; }
    public int    getObjectif()    { return objectif; }
    public int    getEcart()       { return ecart; }
    public String getStatut()      { return statut; }

    public void setId(int id)              { this.id = id; }
    public void setDepartement(String d)   { this.departement = d; }
    public void setConsomme(int consomme)  { this.consomme = consomme; }
    public void setObjectif(int objectif)  { this.objectif = objectif; }
    public void setEcart(int ecart)        { this.ecart = ecart; }
    public void setStatut(String statut)   { this.statut = statut; }

    @Override
    public String toString() {
        return "Alerte{dept=" + departement + ", statut=" + statut + "}";
    }
}