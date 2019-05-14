package com.example.gymstation.Objectos;

public class Ejercicio {

    String Nombre;
    String Descripcion;
    String Imagen;
    String Url;
    String Tipo;

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public Ejercicio() {
    }

    public Ejercicio(String nombre, String descripcion, String imagen, String url, String tipo) {
        Nombre = nombre;
        Descripcion = descripcion;
        Imagen = imagen;
        Url = url;
        Tipo = tipo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
