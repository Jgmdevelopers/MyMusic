# Aplicación de Reproductor de Música

Este proyecto es una aplicación de reproductor de música creada en Java utilizando `BasicPlayer` para la reproducción de archivos MP3 y una base de datos MySQL para almacenar y gestionar las canciones.

## Características

- Reproducción de archivos MP3.
- Pausar, reanudar y detener la reproducción.
- Mostrar el tiempo de reproducción actual y total.
- Actualizar y mostrar la barra de progreso de la canción.
- Almacenar y gestionar canciones en una base de datos MySQL.
- Incrementar el contador de reproducciones de las canciones.
- Listar y reproducir las canciones más reproducidas.

## Capturas de Pantalla

### Interfaz del Reproductor de Música
![Reproductor de Música](src/main/java/resources/rep.png)

### Reproductor de Música con Lista de Reproducción
![Reproductor de Música con Lista de Reproducción](src/main/java/resources/rep2.png)

## Tecnologías Utilizadas

- Java
- MySQL
- Swing (para la interfaz gráfica)
- BasicPlayer (para la reproducción de audio)

## Cómo Usar

1. Clona el repositorio.
2. Configura tu base de datos MySQL utilizando el script SQL proporcionado.
3. Compila y ejecuta la aplicación usando un IDE o terminal.

## Configuración de la Base de Datos

Asegúrate de tener MySQL instalado. Utiliza el script `music_library.sql` para configurar el esquema de la base de datos y los datos iniciales.

## Contribuciones

¡Se aceptan pull requests! Para cambios importantes, por favor abre primero un issue para discutir sobre lo que te gustaría cambiar.

## Licencia

[MIT](LICENSE)
