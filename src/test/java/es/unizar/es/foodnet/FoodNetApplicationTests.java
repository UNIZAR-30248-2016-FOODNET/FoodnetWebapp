package es.unizar.es.foodnet;

import es.unizar.es.foodnet.model.CategoriaTest;
import es.unizar.es.foodnet.model.ProductoTest;
import es.unizar.es.foodnet.model.SupermercadoTest;
import es.unizar.es.foodnet.model.UsuarioTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;


import org.junit.runners.Suite;

@RunWith(Suite.class)
@SuiteClasses(
		{
				CategoriaTest.class,
				ProductoTest.class,
				SupermercadoTest.class,
				UsuarioTest.class,

		})
public class FoodNetApplicationTests {}