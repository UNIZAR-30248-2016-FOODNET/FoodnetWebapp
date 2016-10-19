#Definición de API para la autenticacion de usuarios

1. El controlador encargado de tratar la petición se llama ControladorUsuario
1. Para mostrar el HTML del registro de usuario se invoca a /login
1. Para realizar las operaciones de la BBDD se invoca a un objeto de tipo RepositorioUsuarios.
  1. El metodo encargado de tratar la peticion para autenticar a un usuario se llama autenticarUsuario
1. Para autenticarse en el sistema el usuario debera introducir el email y la contraseña 
