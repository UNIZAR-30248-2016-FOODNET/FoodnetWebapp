package es.unizar.es.foodnet.model.repository;

import es.unizar.es.foodnet.model.entity.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio encargado de realizar las operaciones CRUD sobre los pedidos
 */
public interface RepositorioPedido extends MongoRepository<Pedido,String> {
    // Busca un pedido a partir de su nombre
    Pedido findById(String Id);
}