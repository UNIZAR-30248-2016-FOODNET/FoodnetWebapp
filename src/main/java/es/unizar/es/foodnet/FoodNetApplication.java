package es.unizar.es.foodnet;

import es.unizar.es.foodnet.model.entity.Product;
import es.unizar.es.foodnet.model.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodNetApplication implements CommandLineRunner {

	@Autowired
	private ProductRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(FoodNetApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();

		// save a couple of customers
		repository.save(new Product("Arroz", "Mercadona"));
		repository.save(new Product("Pollo", "Mercadona"));

		// fetch all customers
		System.out.println("Products found with findAll():");
		System.out.println("------------------------------");
		repository.findAll().forEach(System.out::println);
		System.out.println();

		// fetch an individual customer
		System.out.println("Product found with findByName('Arroz'):");
		System.out.println("---------------------------------------");
		System.out.println(repository.findByName("Arroz"));

		System.out.println("Customers found with findByBrand('Mercadona'):");
		System.out.println("----------------------------------------------");
		repository.findByBrand("Mercadona").forEach(System.out::println);
	}

}
