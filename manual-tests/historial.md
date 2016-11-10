# Pruebas manuales del historial
1. Sin haber iniciado sesión no se debe poder acceder al historial.
2. Si se accede al historial aparecen los pedidos realizados por el usuario logueado en paneles con el id del pedido y la fecha de realización.
3. Si el usuario no ha realizado ningún pedido todavía, en la sección del historial no debe aparecer ningún pedido.
4. Una vez realizado el pago de un pedido con, por ejemplo, 3 yogur, 2 yogur2 y un yogur3, si se accede al historial debe aparecer el panel correspondiente.
5. Si se pulsa sobre el id del pedido debe desplegarse del panel información con los productos incluídos en el pedido, en el caso anterior 3 yogur, 2 yogur2 y un yogur3. Además del nombre del producto debe aparecer la cantidad de él en el pedido, el supermercado al que pertenece y el precio resultante de multiplicar el precio unitario por la cantidad. Para finalizar debe aparecer el precio total del pedido (gastos incluídos).
6. Si sobre un pedido con el panel de información desplegado se vuelve a pulsar sobre el id de este, debe cerrarse (replegarse) volviendo a mostrar solo los datos iniciales (id del pedido y fecha).
7. Si el historial tiene más de un pedido y alguno de ellos está desplegado, al intentar desplegar otro, se debe replegar el desplegado y desplegar al que se intenta desplegar (modo acordeón). 

