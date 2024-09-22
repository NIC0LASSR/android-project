
# Aplicacion Android

Desarrollar una aplicación meteorológica para dispositivos Android utilizando Kotlin como lenguaje de programación. 
La aplicación emplea Retrofit para realizar solicitudes a una API de predicción meteorológica y mostrar los datos en la interfaz de usuario.

## Funcionalidades de la aplicación

## Restricciones técnicas
1. Esta aplicación funcionará en dispositivos Android con sistema operativo versión 5 o superior
2. La información usada en la aplicación es de uso gratuito y será obtenida del API de [OpenWeather] https://openweathermap.org
3. Se solicitará al usuario permiso para acceder a su ubicación, necesario para el correcto funcionamiento de la aplicación

## Funcionalidades de usuario
Clima le permitirá al usuario consultar e interactuar con datos metereoligicos actuales de la ubicacion actual del usuario a través de una interfaz amigable. El usuario podrá:


-Obtención de la Ubicación Actual:
La aplicación utiliza la ubicación actual del dispositivo para obtener datos meteorológicos en tiempo real.

-Solicitud de Datos Meteorológicos:
La aplicación envía solicitudes a la API de OpenWeatherMap utilizando las coordenadas de la ubicación del usuario para obtener información meteorológica actualizada.

-Ver el clima actual: 
Temperatura, humedad, viento, sensación térmica, condiciones actuales.


## Diseño de interfaz de usuario:

La interfaz de usuario de la aplicación Clima esta inspirada en la interfaz de usuario de aplicaciones similares y en el uso de Material Design.
A continuación se presentan las principales pantallas:

1. Pantalla inicio
2. Permiso de ubicacion
3. Pantalla principal



![Pantalla de inicio](imagenes/clima_icono.png)