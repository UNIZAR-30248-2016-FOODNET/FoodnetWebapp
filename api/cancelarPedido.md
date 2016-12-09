#Definición de API para la cancelación de un pedido

1. El controlador encargado de tratar la petición se llama ControladorPedido
1. Para cancelar un pedido se invoca al endpoint `/cancelarPedido` y se le pasará el id del pedido por petición post
    1. El nombre del atributo id del pedido que le llega al controlador se llamará idPedido
1. El método devolverá una redirección al historial de pedidos con su correspondiente feedback