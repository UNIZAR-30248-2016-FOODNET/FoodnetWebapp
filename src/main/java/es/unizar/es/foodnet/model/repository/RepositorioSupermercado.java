package es.unizar.es.foodnet.model.repository;

import es.unizar.es.foodnet.model.entity.Supermercado;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorioSupermercado extends MongoRepository<Supermercado,String> {
    Supermercado findByNombre(String nombre);
}
