package es.unizar.es.foodnet.model.service;

import es.unizar.es.foodnet.model.entity.Producto;

/**
 * Esta clase representa un producto de un carro, el cual esta formado por una cantidad y un producto
 */
public class ProductoCarro {

    private Producto producto;
    private Integer cantidadProducto;

    public ProductoCarro(Producto producto, Integer cantidadProducto) {
        this.producto = producto;
        this.cantidadProducto = cantidadProducto;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(Integer cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }
}
