# Intro
Este proyecto implementa buenas prácticas de desarrollo tales cómo principos solid, diseño orientado al dominio, diseño orientado a pruebas, etc. Esta API simula la consulta de precios. 

# Pre-Requisitos 
- Java 11
- Maven 3.9.5

# Generalidades del proyecto
- Estilo de Arquitectura: hexagonal
- Archivo docker disponible
- Archivos de pipelines de ejemplo
- Capas:
  1.	Infrastructure
  2.	Aplicación
  3.	Domain
- comandos para instalar dependencias: `mvn install`
- comando para servir el api: ``mvn spring-boot:run``

# Endpoints del API
- versión (GET): http://localhost:8080/v1.0/version
- consulta de precios (GET): http://localhost:8080/v1.0/prices/{categoriaId}/{productoId}?dateApply={fecha_aplicacion}
  - Ejemplo: ``http://localhost:8080/v1.0/prices/1/35455?dateApply=2020-06-14 16:00:00``
  - Respuesta: ``{
    "statusCode": 200,
    "success": true,
    "errorApi": null,
    "data": {
    "productoId": 35455,
    "cadenaId": 1,
    "tarifa": 2,
    "fechasAplicacion": "2020-06-14T15:00:00",
    "precioFinal": 25.45,
    "moneda": "EUR"
    }
    }``
# Build and Test
TODO: Describe and show how to build your code and run the tests. 

# Contribute
TODO: Explain how other users and developers can contribute to make your code better. 

If you want to learn more about creating good readme files then refer the following [guidelines](https://docs.microsoft.com/en-us/azure/devops/repos/git/create-a-readme?view=azure-devops). You can also seek inspiration from the below readme files:
- [ASP.NET Core](https://github.com/aspnet/Home)
- [Visual Studio Code](https://github.com/Microsoft/vscode)
- [Chakra Core](https://github.com/Microsoft/ChakraCore)