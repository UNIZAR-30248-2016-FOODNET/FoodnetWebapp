#Definición de API para mostrar el historial de pedidos de un usuario

1. El controlador encargado de tratar la petición se llama ControladorPedido
1. Para mostrar el HTML del historial de pedidos se invoca a /historial
1. Para realizar las operaciones de la BBDD se invoca a un objeto de tipo RepositorioPedido.
1. El metodo encargado de tratar la peticion para mostrar el historial de un usuario se llama cargarPedidos
1. Para mostrar el historial de pedidos el usuario debe estar identificado
1. Un Pedido esta compuesto por los campos id (String), fecha (Date), usuario (Usuario), productos
(Lista de objetos de tipo ProductoCarro) y cost (double) y los métodos get para cada uno de ellos.