package es.unizar.es.foodnet.model.entity;

import org.springframework.data.annotation.Id;

public class Supermercado {
    @Id
    private String id;
    private String nombre;

    //Mandatory in spite of being useless
    public Supermercado() {}

    public Supermercado(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return String.format(
                "Supermercado[id=%s, nombre='%s']",
                id, nombre);
    }
}
