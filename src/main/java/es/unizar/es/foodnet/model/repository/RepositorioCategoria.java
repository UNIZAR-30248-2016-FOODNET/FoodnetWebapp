package es.unizar.es.foodnet.model.repository;

import es.unizar.es.foodnet.model.entity.Categoria;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorioCategoria extends MongoRepository<Categoria,String> {
    Categoria findByNombre(String nombre);
}
