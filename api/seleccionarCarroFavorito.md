#Definición de API para seleccionar carro favorito

1. El controlador encargado de tratar la petición se llama `ControladorCarroFavorito`.
2. La petición solo la pueden realizar los usuarios registrados y logeados. 
3. La petición se realiza desde la página que muestra los carros favoritos de un usuario.
4. La petición se realiza clickando en el botón correspondiente `Añadir carro favorito al carrito`.
5. El método encargado de seleccionar (añadir los productos al carro) se llama `addFavoritoACarro` que es mapeado mediante el endpoint `/addFavoritoCarro`.
6. Para realizar las operaciones de la BBDD se invoca a un objeto de tipo `RepositorioCarroFavorito`.
7. La función JavaScript que es invocada al clickar sobre el botón, para acutalizar el carro es `addAllCarro`.