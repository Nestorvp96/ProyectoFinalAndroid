package com.example.gymstation.Objectos;

public class Favoritos {

    String Nombre;
    String Tipo;
    String Id;

    public Favoritos() {
    }

    public Favoritos(String nombre, String tipo, String id) {
        Nombre = nombre;
        Tipo = tipo;
        Id = id;
    }



    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
