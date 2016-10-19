package es.unizar.es.foodnet.model.repository;

import es.unizar.es.foodnet.model.entity.Categoria;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio encargado de realizar las operaciones CRUD sobre las categorias
 */
public interface RepositorioCategoria extends MongoRepository<Categoria,String> {

    // Busca una categoria a partir de su nombre
    Categoria findByNombre(String nombre);
}
