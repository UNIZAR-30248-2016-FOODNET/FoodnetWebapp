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
    private double cost;
    private String estado;
    private List<ProductoCarro> productos;
    public static final String PEDIDO_REALIZADO = "REALIZADO";
    public static final String PEDIDO_EN_CURSO = "ENCURSO";
    public static final String PEDIDO_CANCELADO = "CANCELADO";


    public Pedido(){}

    public Pedido(Usuario u,Date d,List<ProductoCarro> l) {
        usuario=u;
        fecha=d;
        productos = l;
        cost = 6.9;
        estado = PEDIDO_REALIZADO;
        if(l != null) {
            for (ProductoCarro p : productos) {
                cost += p.getCantidadProducto() * p.getProducto().getPrecio();
            }
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

    public String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(fecha);
    }

    public String getId() {
        return id;
    }

    /**
     * Devuelve el estado de un pedido, que puede ser:
     * R
     * @return
     */
    public String getEstado() {
        return estado;
    }

    public boolean cancelarPedido() {
        if(estado.equalsIgnoreCase(PEDIDO_EN_CURSO) || estado.equalsIgnoreCase(PEDIDO_CANCELADO))
            return false;

        estado = PEDIDO_CANCELADO;
        return true;

    }
}