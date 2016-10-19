#Definición de API para la gestión de los usuarios

1. El controlador encargado de tratar la petición se llama ControladorUsuario
1. Para mostrar el HTML del registro de usuario se invoca a /registrarse
1. Para mostrar el HTML de la modificación de datos de usuario se invoca a /modificarCampos
1. Para mostrar el HTML de la eliminación de un usuario se invoca a /eliminar
1. Para realizar las operaciones CRUD (registrar, modificar, eliminar, ver) de la BBDD se invoca a un objeto de tipo RepositorioUsuarios.
  1. El metodo encargado de tratar la peticion /registrarse se llama registrarUsuario
  1. El metodo encargado de tratar la peticion /modificarCampos se llama modificarUsuario
  1. El metodo encargado de tratar la peticion /eliminar se llama eliminarUsuario
1. Como mínimo un usuario deberá tener los siguientes campos con estos nombres: nombre, apellidos, email, dirección y password.
