package es.unizar.es.foodnet.model.repository;

import es.unizar.es.foodnet.model.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio encargado de realizar las operaciones CRUD sobre los usuarios
 */
public interface RepositorioUsuario extends MongoRepository<Usuario,String> {
    Usuario findById(String id);
    Usuario findByEmail(String email);
}
