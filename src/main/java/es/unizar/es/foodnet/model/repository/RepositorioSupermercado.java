package es.unizar.es.foodnet.model.repository;

import es.unizar.es.foodnet.model.entity.Supermercado;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio encargado de realizar las operaciones CRUD sobre los supermercados
 */
public interface RepositorioSupermercado extends MongoRepository<Supermercado,String> {

    // Busca un supermercado a partir de su nombre
    Supermercado findByNombre(String nombre);
}
