#Definición de API para cargar carros favoritos

1. El controlador encargado de tratar la petición se llama `ControladorCarroFavorito`.
2. Para mostrar los carros favoritos se invoca al endpoint `/carrosFavoritos`.
3. El usuario debe haber iniciado sesión para que el controlador devuelva los carros favoritos de dicho usuario.
4. Para realizar las operaciones de la BBDD se invoca a un objeto de tipo `RepositorioCarroFavorito`.
5. La lista con los carros favoritos de un usuario se guarda en una variable de Model con nombre "carrosFavoritos".