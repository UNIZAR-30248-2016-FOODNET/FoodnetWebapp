package es.unizar.es.foodnet.model.entity;

import es.unizar.es.foodnet.model.service.ProductoCarro;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Document
public class Pedido {
    @Id
    private String id;

    @DBRef
    private Usuario usuario;

    private Date fecha;
    private List<ProductoCarro> productos;
    private double cost;


    public Pedido(){}

    public Pedido(Usuario u,Date d,List<ProductoCarro> l) {
        usuario=u;
        fecha=d;
        productos = l;
        cost = 6.9;
        for(ProductoCarro p: productos){
            cost+=p.getCantidadProducto()*p.getProducto().getPrecio();
        }
    }

    public Usuario getUsuario(){
        return usuario;
    }

    public String getFecha(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(fecha);
    }

    //TODO: añadir campo descripcion al constructor cuando sea necesario
    public List<ProductoCarro> getProductos() {
        return productos;
    }

    /**
     *
     * @return el precio total de los productos seleccionados en el pedido
     */
    public double getCost(){
        return cost;
    }

    public String getIdPedido() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(fecha);
    }

}