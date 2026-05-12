package com.java.paperless_miniprojet.model;


public class Utilisateur {

    private int    id;
    private String nom;
    private String email;
    private String motDePasse;
    private String role;

    public Utilisateur() {}

    public Utilisateur(int id, String nom, String email, String motDePasse, String role) {
        this.id         = id;
        this.nom        = nom;
        this.email      = email;
        this.motDePasse = motDePasse;
        this.role       = role;
    }

    public int    getId()         { return id; }
    public String getNom()        { return nom; }
    public String getEmail()      { return email; }
    public String getMotDePasse() { return motDePasse; }
    public String getRole()       { return role; }

    public void setId(int id)             { this.id = id; }
    public void setNom(String nom)        { this.nom = nom; }
    public void setEmail(String email)    { this.email = email; }
    public void setMotDePasse(String mdp) { this.motDePasse = mdp; }
    public void setRole(String role)      { this.role = role; }

    @Override
    public String toString() {
        return "Utilisateur{id=" + id + ", nom=" + nom + ", role=" + role + "}";
    }
}