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

import java.util.ArrayList;

@SpringBootApplication
public class FoodNetApplication implements CommandLineRunner {

	private final RepositorioProducto repositorioProducto;
	private final RepositorioCategoria repositorioCategoria;
	private final RepositorioSupermercado repositorioSupermercado;

	@Autowired
	public FoodNetApplication(RepositorioCategoria repositorioCategoria, RepositorioSupermercado repositorioSupermercado, RepositorioProducto repositorioProducto) {
		this.repositorioCategoria = repositorioCategoria;
		this.repositorioSupermercado = repositorioSupermercado;
		this.repositorioProducto = repositorioProducto;
	}

	public static void main(String[] args) {
		SpringApplication.run(FoodNetApplication.class, args);
	}

	/**
	 * Lo que se incluya dentro de este metodo se ejecuta al lanzar la
	 * aplicacion de Spring
     */
	@Override
	public void run(String... args) throws Exception {

		// Delete all the content
		repositorioProducto.deleteAll();
		repositorioCategoria.deleteAll();
		repositorioSupermercado.deleteAll();

		// Save one category
		repositorioCategoria.save(new Categoria("Lacteos"));
		// Save one supermarket
		repositorioSupermercado.save(new Supermercado("Mercadona"));

		Categoria lacteo = repositorioCategoria.findByNombre("Lacteos");
		Supermercado mercadona = repositorioSupermercado.findByNombre("Mercadona");

		// Store 5 products
		Producto p1 = new Producto(lacteo, mercadona, "Yogurt", 0.20, "http://placehold.it/650x450&text=Yogurt");
		Producto p2 = new Producto(lacteo, mercadona, "Yogurt2", 3.80, "http://placehold.it/650x450&text=Yogurt2");
		Producto p3 = new Producto(lacteo, mercadona, "Yogurt3", 1.2, "http://placehold.it/650x450&text=Yogurt3");
		Producto p4 = new Producto(lacteo, mercadona, "Yogurt4", 0.20, "http://placehold.it/650x450&text=Yogurt4");
		Producto p5 = new Producto(lacteo, mercadona, "Yogurt5", 0.70, "http://placehold.it/650x450&text=Yogurt5");

		ArrayList<Producto> productosPrevios = new ArrayList<>();
		productosPrevios.add(p1);
		productosPrevios.add(p2);
		productosPrevios.add(p3);
		productosPrevios.add(p4);
		productosPrevios.add(p5);

		repositorioProducto.save(p1);
		repositorioProducto.save(p2);
		repositorioProducto.save(p3);
		repositorioProducto.save(p4);
		repositorioProducto.save(p5);

		//Comprobar que se han introducido correctamente los 5 productos
		ArrayList<Producto> productosObtenidos = (ArrayList<Producto>) repositorioProducto.findAll();
		for(int i = 0; i < productosObtenidos.size(); i++){
			if(!((productosObtenidos.get(i).getNombre().equals(productosPrevios.get(i).getNombre())) &&
					(!productosObtenidos.get(i).getSupermercado().equals(productosPrevios.get(i).getSupermercado())))){
				System.exit(1);
			}
		}

		System.out.println("===========================================");
		System.out.println("INICIALIZACION MONGODB COMPLETADA CON EXITO");
		System.out.println("===========================================");

		//TODO: Cuando toque, borrar o descomentar
		/*
		// fetch an individual customer
		System.out.println("Producto found with findByName('Yogurt'):");
		System.out.println("---------------------------------------");
		System.out.println(repositorioProducto.findByNombre("Yogurt"));
		System.out.println();
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
