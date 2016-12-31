#Definición de API para borrar un carro favorito

1. El controlador encargado de tratar la petición se llama `ControladorCarroFavorito`.
2. Para mostrar los carros favoritos se invoca al endpoint `/borrarCarroFavorito`.
3. El usuario debe haber iniciado sesión con la cuenta del usuario del carro para que el controlador borre dicho carro.
4. Para realizar las operaciones de la BBDD se invoca a un objeto de tipo `RepositorioCarroFavorito`.
5. El carro favorito a borrar se indica en el parametro nombreCarroFavorito de la peticion.