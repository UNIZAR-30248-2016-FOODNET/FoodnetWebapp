package es.unizar.es.foodnet;

import es.unizar.es.foodnet.model.entity.Categoria;
import es.unizar.es.foodnet.model.entity.Producto;
import es.unizar.es.foodnet.model.entity.Supermercado;
import es.unizar.es.foodnet.model.repository.RepositorioCategoria;
import es.unizar.es.foodnet.model.repository.RepositorioProducto;
import es.unizar.es.foodnet.model.repository.RepositorioSupermercado;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodNetApplication implements CommandLineRunner {

	@Autowired
	private RepositorioProducto repositorioProducto;
	@Autowired
	private RepositorioCategoria repositorioCategoria;
	@Autowired
	private RepositorioSupermercado repositorioSupermercado;

	public static void main(String[] args) {
		SpringApplication.run(FoodNetApplication.class, args);
	}

	/**
	 * Todo lo que se incluya dentro de este metodo se ejecuta al lanzar la
	 * aplicacion de Spring
     */
	@Override
	public void run(String... args) throws Exception {

		repositorioProducto.deleteAll();
		repositorioCategoria.deleteAll();
		repositorioSupermercado.deleteAll();

		// Save one category
		repositorioCategoria.save(new Categoria("Lacteos"));
		// Save one supermarket
		repositorioSupermercado.save(new Supermercado("Mercadona"));

		Categoria lacteo = repositorioCategoria.findByNombre("Lacteos");
		Supermercado mercadona = repositorioSupermercado.findByNombre("Mercadona");

		// Save 5 product and assign its category
		repositorioProducto.save(new Producto(lacteo, mercadona, "Yogurt", 0.20, "http://placehold.it/650x450"));
		repositorioProducto.save(new Producto(lacteo, mercadona, "Yogurt2", 3.80, "http://placehold.it/650x450"));
		repositorioProducto.save(new Producto(lacteo, mercadona, "Yogurt3", 1.2, "http://placehold.it/650x450"));
		repositorioProducto.save(new Producto(lacteo, mercadona, "Yogurt4", 0.20, "http://placehold.it/650x450"));
		repositorioProducto.save(new Producto(lacteo, mercadona, "Yogurt5", 0.70, "http://placehold.it/650x450"));

		// fetch all customers
		System.out.println("Products found with findAll():");
		System.out.println("------------------------------");
		repositorioProducto.findAll().forEach(System.out::println);
		System.out.println();

		// fetch an individual customer
		System.out.println("Producto found with findByName('Yogurt'):");
		System.out.println("---------------------------------------");
		System.out.println(repositorioProducto.findByNombre("Yogurt"));
		System.out.println();
/*
		System.out.println("Customers found with findBySupermarket('Mercadona'):");
		System.out.println("----------------------------------------------");
		productRepository.findBySupermarket("Mercadona").forEach(System.out::println);
		System.out.println();

		System.out.println("Customers found with findByCategory('Lacteos'):");
		System.out.println("----------------------------------------------");
		productRepository.findByCategory("Lacteos").forEach(System.out::println);
		*/
	}

}
