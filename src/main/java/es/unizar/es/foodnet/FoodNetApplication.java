package es.unizar.es.foodnet;

import es.unizar.es.foodnet.model.entity.Categoria;
import es.unizar.es.foodnet.model.entity.Producto;
import es.unizar.es.foodnet.model.entity.Supermercado;
import es.unizar.es.foodnet.model.entity.Usuario;
import es.unizar.es.foodnet.model.repository.*;
import es.unizar.es.foodnet.model.service.Password;
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
	private final RepositorioUsuario repositorioUsuario;

	@Autowired
	public FoodNetApplication(RepositorioCategoria repositorioCategoria, RepositorioSupermercado repositorioSupermercado,
							  RepositorioProducto repositorioProducto, RepositorioPedido repositorioPedido,
							  RepositorioUsuario repositorioUsuario) {
		this.repositorioCategoria = repositorioCategoria;
		this.repositorioSupermercado = repositorioSupermercado;
		this.repositorioProducto = repositorioProducto;
		this.repositorioPedido = repositorioPedido;
		this.repositorioUsuario = repositorioUsuario;
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
		Usuario user = repositorioUsuario.findByEmail("admin@admin.com");
		if(user == null){
			Password p = new Password();
			repositorioUsuario.save(new Usuario("Administrador","Foodnet","admin@admin.com",
					p.generatePassword("admin"),"Direccion Admin",true));
		}
	}
}
