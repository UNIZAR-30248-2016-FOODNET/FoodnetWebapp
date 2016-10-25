#Definición de API para la gestión de los usuarios

1. El controlador encargado de tratar la petición se llama ControladorUsuario
1. Para mostrar el HTML del registro de usuario se invoca a /registrarse
1. Para mostrar el HTML de la modificación de datos de usuario se invoca a /modificarDatos
1. Para mostrar el HTML de la eliminación de un usuario se invoca a /eliminarUsuario
1. Para realizar las operaciones CRUD (registrar, modificar, eliminar, ver) de la BBDD se invoca a un objeto de tipo RepositorioUsuarios.
  1. El metodo encargado de tratar la peticion /registrarse se llama registrarUsuario. No puede haber dos usuarios con el mismo email
    1. La pagina para el login y registro se llama registro.html.
  1. El metodo encargado de tratar la peticion /modificarDatos se llama modificarDatosUsuario. Se podrá cambiar tanto el email original del registro, así como la contraseña. Si se produce un error al modificar los datos por no encontrar al usuario, se redirige a la pagina de login, y si hay un error al cambiar la contraseña, se vuelve a la pagina de modificacion mostrando un error. Si se cambia correctamente, se titua en la pagina de modificacion de datos y muestra un mensaje de confirmacion.
    1. La pagina para modificar datos de usuario se llama modificarDatosUsuario.html
  1. El metodo encargado de tratar la peticion /eliminar se llama eliminarUsuario. Una vez eliminado vuelve a la pagina de login/registro.
    1. La página de login/registro se llama registro.html.
1. Como mínimo un usuario deberá tener los siguientes campos con estos nombres: nombre, apellidos, email, dirección y password.
