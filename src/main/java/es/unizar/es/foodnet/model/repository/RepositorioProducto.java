package es.unizar.es.foodnet.model.repository;

import es.unizar.es.foodnet.model.entity.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio encargado de realizar las operaciones CRUD sobre los productos
 */
public interface RepositorioProducto extends MongoRepository<Producto,String> {

    // Busca un producto a partir de su nombre
    Producto findByNombre(String nombre);

    // Busca un producto a partir de su id
    Producto findById(String id);

    //@Query( value="{ category.$name: ?0 }")
    //List<Producto> findByCategory(String category);

    //@Query( value="{ supermarket.$name: ?0 }")
    //List<Producto> findBySupermarket(String supermarket);

}
