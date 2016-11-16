package es.unizar.es.foodnet;

import es.unizar.es.foodnet.model.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(
		{
				CategoriaTest.class,
				ProductoTest.class,
				SupermercadoTest.class,
				UsuarioTest.class,
				PedidoTest.class,

		})
public class FoodNetApplicationTests {}