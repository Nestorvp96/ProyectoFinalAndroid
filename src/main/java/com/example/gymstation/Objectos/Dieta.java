package com.example.gymstation.Objectos;

public class Dieta {

    String email;
    int proteinas;
    int Carbos;
    int Frutas;
    int Verduras;
    int Leguminosas;
    int Lacteos;

    public Dieta() {
    }

    public Dieta(String email, int proteinas, int carbos, int frutas, int verduras, int leguminosas, int lacteos) {
        this.email = email;
        this.proteinas = proteinas;
        Carbos = carbos;
        Frutas = frutas;
        Verduras = verduras;
        Leguminosas = leguminosas;
        Lacteos = lacteos;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getProteinas() {
        return proteinas;
    }

    public void setProteinas(int proteinas) {
        this.proteinas = proteinas;
    }

    public int getCarbos() {
        return Carbos;
    }

    public void setCarbos(int carbos) {
        Carbos = carbos;
    }

    public int getFrutas() {
        return Frutas;
    }

    public void setFrutas(int frutas) {
        Frutas = frutas;
    }

    public int getVerduras() {
        return Verduras;
    }

    public void setVerduras(int verduras) {
        Verduras = verduras;
    }

    public int getLeguminosas() {
        return Leguminosas;
    }

    public void setLeguminosas(int leguminosas) {
        Leguminosas = leguminosas;
    }

    public int getLacteos() {
        return Lacteos;
    }

    public void setLacteos(int lacteos) {
        Lacteos = lacteos;
    }
}
