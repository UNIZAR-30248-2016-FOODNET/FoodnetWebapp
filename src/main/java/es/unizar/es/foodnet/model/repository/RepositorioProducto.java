package es.unizar.es.foodnet.model.repository;

import es.unizar.es.foodnet.model.entity.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Fran Menendez Moya on 5/10/16.
 */
@Repository
public interface RepositorioProducto extends MongoRepository<Producto,String> {

    Producto findByNombre(String nombre);

    //@Query( value="{ category.$name: ?0 }")
    //List<Producto> findByCategory(String category);

    //@Query( value="{ supermarket.$name: ?0 }")
    //List<Producto> findBySupermarket(String supermarket);

}
