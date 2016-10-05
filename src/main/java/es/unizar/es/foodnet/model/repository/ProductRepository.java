package es.unizar.es.foodnet.model.repository;

import es.unizar.es.foodnet.model.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Fran Menendez Moya on 5/10/16.
 */
public interface ProductRepository extends MongoRepository<Product,String> {

    Product findByName(String name);
    List<Product> findByBrand(String brand);

}
