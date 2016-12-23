# Pruebas manuales para la gestión de productos
1. Al iniciar sesión con el usuario adimistrador deberá aparecer un boton para la gestión de productos visible desde cualquiera de las páginas.
2. Un usuario corriente no debe poder acceder a la gestión de productos, si realiza una peticion a "/gestionarProductos" se debe redirigirle al catalogo y mostrarle un error
3. Si el usuario no ha realizado ningún pedido todavía, en la sección del historial no debe aparecer ningún pedido.
4. Al pulsar sobre gestionar productos, debe aparecer una tabla con la información de cada producto (Nombre, Supermercado, Categoría y Precio).
5. Junto a cada una de las filas con información del producto, deben aparecer dos botones, uno para editar y otro para borrar.
6. Al pulsar sobre el botón editar debe aparecer un pop-up con la información disponible para editar de ese producto y un botón para guardar cambios.
    7. Si se modifica alguno de los campos del pop-up anterior y se pulsa sobre el botón guardar, debe modificarse la tabla y el catalogo con los correspondientes cambios.
    8. Si se modifica algun campo pero no se pulsa sobre guardar, sino que se cierra el pop-up no debe realizarse ninguna modificación.
9. Si se pulsa sobre el botón  borrar de un producto, ese producto debe desaparecer de la tabla y del catálogo.
10. Solo deben mostrarse un maximo de n productos por página, para bver el resto el administrador debe seleccionar en el número de la página que quiere, en siguiento o anterior.
    11. El máximo de productos que pueden mostrarse por pagina puede modificarse a 10, 25, 50 o 100 sin necesidad de recargar la página, pero por defecto son 10.
    12. Debajo de la tabla debe aparecer el numero de productos totales y el intervalo y la página de los productos que se muestran en ese momento.
13. Sobre cada uno de los campos debe aparecer un botón para ordenar la tabla por dicho campo en orden creciente o decreciente.
14. Debajo de la tabla deben aparecer unos campos y un botón para insertar un nuevo producto con los campos anteriores.
    15. No debe permitirse insertar un nuevo producto si no están todos los campos completados
15. Arriba de la tabla debe aparecer un campo para la busqueda de productos en la tabla.
    16. Cuando se inserta texto en el campo de busqueda debe actualizarse la tabla para que solo aparezcan productos cuyo nombre encaje con dicho texto
17. El usuario administrador debe poder modificar todos sus datos salvo su email.
    18. Si el usuario administrador modifica alguno de sus datos la opción para gestionar productos debe permanecer activa.
