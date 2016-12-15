package es.unizar.es.foodnet.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Producto {
    @Id
    private String id;
    @DBRef
    private Categoria categoria;
    @DBRef
    private Supermercado supermercado;
    private String nombre;
    private double precio;
    private String imagen;
    private String descripcion;

    //Mandatory in spite of being useless
    public Producto() {}

    //TODO: añadir campo descripcion al constructor cuando sea necesario
    public Producto(Categoria categoria, Supermercado supermercado, String nombre,
                    double precio, String imagen, String descripcion) {
        this.categoria = categoria;
        this.supermercado = supermercado;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Supermercado getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(Supermercado supermercado) {
        this.supermercado = supermercado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return String.format(
                "Producto[id=%s, name='%s', supermarket='%s', price='%s', " +
                        "category='%s']",
                id, nombre, supermercado.getNombre(), precio, categoria.getNombre());
    }
}