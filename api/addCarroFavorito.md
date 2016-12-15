#Definición de API para la adición de un carro favorito

1. El controlador encargado de tratar la petición se llama `ControladorCarroFavorito`.
2. Para añadir un pedido se invoca al endpoint `/addFavorito` y se le pasa un nombre que se le habrá pedido al usuario, además del carro actual.
3. La petición solo se podrá realizar desde la pantalla del carro `carro.html` de productos.
4. La petición solo la pueden realizar los usuarios registrados y logeados.  
5. Para realizar las operaciones de la BBDD se invoca a un objeto de tipo `RepositorioCarroFavorito`.
6. Un CarroFavorito está compuesto por los campos id (String), nombre (String), usuario (Usuario), productos (Lista de objetos de tipo ProductoCarro) y coste (double). 