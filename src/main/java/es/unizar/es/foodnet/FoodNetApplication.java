package es.unizar.es.foodnet;

import es.unizar.es.foodnet.model.entity.Categoria;
import es.unizar.es.foodnet.model.entity.Producto;
import es.unizar.es.foodnet.model.entity.Supermercado;
import es.unizar.es.foodnet.model.repository.RepositorioCategoria;
import es.unizar.es.foodnet.model.repository.RepositorioPedido;
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
	private final RepositorioPedido repositorioPedido;

	@Autowired
	public FoodNetApplication(RepositorioCategoria repositorioCategoria, RepositorioSupermercado repositorioSupermercado,
							  RepositorioProducto repositorioProducto, RepositorioPedido repositorioPedido) {
		this.repositorioCategoria = repositorioCategoria;
		this.repositorioSupermercado = repositorioSupermercado;
		this.repositorioProducto = repositorioProducto;
		this.repositorioPedido = repositorioPedido;
	}

	public static void main(String[] args) {
		SpringApplication.run(FoodNetApplication.class, args);
	}

	/**
	 * Lo que se incluya dentro de este metodo se ejecuta al lanzar la
	 * aplicacion de Spring
     */
	@Override
	public void run(String... args) throws Exception {	}
}
