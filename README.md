# Taller Garaje Parte 2: Persistencia, Fachadas y Controladores

## Descripción
Este proyecto implementado en la clase de desarrollo de aplicaciones empresariales es un sistema de gestión de vehículos en un garaje utilizando Java EE. 
La arquitectura sigue un diseño en capas que incluye Modelo, Persistencia, Fachada y Controlador.

## Arquitectura
- **Paquete `com.uts.taller1.taller_garaje2.modelo`**: Contiene la entidad `Vehiculo` que representa los datos del vehículo.
- **Paquete `com.uts.taller1.taller_garaje2.persistence`**: Incluye `VehiculoDAO` para operaciones CRUD con JDBC.
- **Paquete `com.uts.taller1.taller_garaje2.fecade`**: Contiene `VehiculoFacade` con la lógica de negocio y validaciones.
- **Paquete `com.uts.taller1.taller_garaje2.controller`**: Aloja `VehiculoServlet` para manejar peticiones HTTP.
- **Paquete `com.uts.taller1.taller_garaje2.exception`**: Define `BusinessException` para errores de negocio.


## Cómo Ejecutar el Sistema
1. **Requisitos**:
   - (IDE)Apache Neetbeens 23
   - Java 8+ (JDK).
   - Servidor GlassFish 7.
   - MySQL con base de datos `garaje` creada
   - CREATE DATABASE garage2;
USE garage2;

CREATE TABLE vehiculos (
 id INT AUTO_INCREMENT PRIMARY KEY,
 placa VARCHAR(20) NOT NULL,
 marca VARCHAR(30) NOT NULL,
 modelo VARCHAR(30) NOT NULL,
 color VARCHAR(20),
 propietario VARCHAR(50) NOT NULL
);.

2. **Configuración**:
   - Agrega el driver MySQL (`mysql-connector-java-8.0.2.jar`) al `lib` de GlassFish .
   - crear el servidor de glassfish con el dive anterior
   - crear el dive para la coneccion a l base de datos 
