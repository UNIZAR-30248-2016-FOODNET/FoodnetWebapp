#Definición de API para mostrar una lista de productos

1. El controlador encargado de tratar la petición se llamará ControladorProducto
1. Para mostrar el HTML de la lista se invocará a /catalogo
1. El método encargado de tratar la petición a /catalogo se llamará cargarCatalogo, devolviendo como String el nombre del html del catálogo, no tendrá parámetros (excepto el modelo). Antes de devolver el html, habrá que asignar a la variable de modelo llamada "listaProductos" todos los productos obtenidos de la bbdd
1. Para obtener todos los productos de la bbdd se invocará a un objeto de
tipo RepositorioProducto y a su método findAll(), el cual devuelve una lista de productos
1. Como mínimo un producto deberá tener los siguientes campos con estos nombres: categoria (fruta, verdura, etc), imagen (String con la url), nombre, precio, descripcion
