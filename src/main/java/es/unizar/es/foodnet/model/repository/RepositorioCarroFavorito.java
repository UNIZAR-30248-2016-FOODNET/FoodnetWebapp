package es.unizar.es.foodnet.model.repository;

import es.unizar.es.foodnet.model.entity.CarroFavorito;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio encargado de realizar las operaciones CRUD sobre los carros favoritos
 */
public interface RepositorioCarroFavorito extends MongoRepository<CarroFavorito,String> {

    CarroFavorito findByNombre(String nombre);
}
