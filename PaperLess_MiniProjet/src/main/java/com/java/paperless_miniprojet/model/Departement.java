package com.java.paperless_miniprojet.model;

public class Departement {

    private int    id;
    private String nom;
    private int    objectifMensuel;

    public Departement() {}

    public Departement(int id, String nom, int objectifMensuel) {
        this.id              = id;
        this.nom             = nom;
        this.objectifMensuel = objectifMensuel;
    }

    public int    getId()              { return id; }
    public String getNom()             { return nom; }
    public int    getObjectifMensuel() { return objectifMensuel; }

    public void setId(int id)                    { this.id = id; }
    public void setNom(String nom)               { this.nom = nom; }
    public void setObjectifMensuel(int objectif) { this.objectifMensuel = objectif; }

    @Override
    public String toString() { return nom; }
}