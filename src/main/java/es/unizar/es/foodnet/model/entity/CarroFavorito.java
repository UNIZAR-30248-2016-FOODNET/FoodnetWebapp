package es.unizar.es.foodnet.model.entity;

import es.unizar.es.foodnet.model.service.ProductoCarro;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class CarroFavorito {

    @Id
    private String id;
    @Indexed(unique = true)
    private String nombre;

    @DBRef
    private Usuario usuario;

    private List<ProductoCarro> productos;
    private double coste;

    public CarroFavorito() {}

    public CarroFavorito(String newN, Usuario newU, List<ProductoCarro> newP) {
        nombre = newN;
        usuario = newU;
        productos = newP;
        coste = 6.9;
        if(newP != null) {
            for (ProductoCarro p : productos) {
                coste += p.getCantidadProducto() * p.getProducto().getPrecio();
            }
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String newN) {
        nombre = newN;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<ProductoCarro> getProductos() {
        return productos;
    }

    public double getCoste() {
        return coste;
    }
}
