package es.unizar.es.foodnet.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Categoria {
    @Id
    private String id;
    @Indexed(unique = true)
    private String nombre;

    //Mandatory in spite of being useless
    public Categoria() {}

    public Categoria(String nombre) {
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
                "Categoria[id=%s, nombre='%s']",
                id, nombre);
    }
}
