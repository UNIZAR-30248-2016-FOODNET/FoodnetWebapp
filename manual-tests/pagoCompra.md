# Pruebas manuales del pago de una compra
1. Sin haber iniciado sesión no se debería poder acceder a `/finalizarPago`, si se detecta que no está logueado, se redirige a página de login, si se está logueado se puede acceder sin problemas.
2. Al acceder al panel de pago de compra, automáticamente debería detectar nombre, apellidos y dirección e insertarlo en los debidos campos de dicha página.
3. Si se borra cualquiera de los dos campos (Nombre y apellidos y dirección) o los dos, no debe permitir realizar el pago.
4. Si se selecciona como método de pago Paypal, si no se rellena el campo dirección de Paypal no debe permitir realizar el pago.
5. Al realizar el pago con éxito se debe haber redirigido a la página principal y haber mostrado un mensaje de feedback confirmando la compra.
