# Gastos API: Backend para Gestión de Gastos Compartidos

Una API REST robusta y segura, construida con Kotlin y Spring Boot, para gestionar los gastos y las deudas dentro de grupos de amigos, compañeros de viaje o de piso.

---

## 📖 Tabla de Contenidos

* [Acerca del Proyecto](#-acerca-del-proyecto)
* [🚀 Características Principales](#-características-principales)
* [🛠️ Tecnologías Utilizadas](#️-tecnologías-utilizadas)
* [📚 Documentación de la API (Swagger)](#-documentación-de-la-api-swagger)
* [🏁 Cómo Empezar](#-cómo-empezar)
    * [Prerrequisitos](#prerrequisitos)
    * [Instalación y Configuración](#instalación-y-configuración)
* [💡 Ejemplos de Uso](#-ejemplos-de-uso)
* [🏗️ Arquitectura y Decisiones de Diseño](#️-arquitectura-y-decisiones-de-diseño)
* [📧 Contacto](#-contacto)

---

## 🌟 Acerca del Proyecto

**Gastos API** es un backend completo diseñado para servir como el cerebro de una aplicación de gestión de gastos compartidos, similar a aplicaciones como Splitwise. El proyecto nació como una forma de aplicar y demostrar conocimientos avanzados en el desarrollo backend, incluyendo la creación de una arquitectura limpia, la implementación de un sistema de seguridad moderno y el diseño de una API RESTful lógica y eficiente.

La API permite a los usuarios registrarse, crear grupos, invitar a miembros, registrar gastos y, lo más importante, utiliza un algoritmo para calcular y simplificar las deudas, minimizando el número de transacciones necesarias para saldar las cuentas entre los miembros.

---

## 🚀 Características Principales

* **Autenticación y Seguridad:** Sistema de registro y login seguro con contraseñas hasheadas (BCrypt) y autenticación basada en tokens **JWT**.
* **Gestión de Grupos:**
    * Creación y eliminación de grupos.
    * Gestión de miembros con roles (`ADMIN`, `MEMBER`).
    * Permisos granulares basados en roles para acciones críticas (ej: solo un `ADMIN` puede añadir o eliminar miembros).
* **Gestión de Gastos:**
    * Creación y eliminación de gastos dentro de un grupo.
    * División de gastos equitativa entre los miembros.
* **Cálculo de Saldos Inteligente:**
    * Cálculo del balance neto de cada miembro (cuánto ha pagado vs. cuánto debe).
    * **Algoritmo de simplificación de deudas** para generar el mínimo número de pagos necesarios para que todos queden en paz.
* **API Documentada:** Documentación interactiva completa generada con **Swagger (OpenAPI)**.
* **Manejo de Errores Profesional:** Implementación de un manejador de excepciones global (`@RestControllerAdvice`) para devolver respuestas de error claras y estandarizadas.
* **Validación de Datos:** Uso de `jakarta.validation` para asegurar la integridad de los datos de entrada en todos los endpoints.

---

## 🛠️ Tecnologías Utilizadas

Este proyecto fue construido utilizando un stack moderno y robusto, enfocado en las mejores prácticas del ecosistema de Spring.

* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **Framework:** [Spring Boot 3](https://spring.io/projects/spring-boot)
* **Seguridad:** [Spring Security 6](https://spring.io/projects/spring-security) (autenticación JWT)
* **Base de Datos:** [Spring Data JPA](https://spring.io/projects/spring-data-jpa) con Hibernate
* **Motor de Base de Datos:** [PostgreSQL](https://www.postgresql.org/)
* **Documentación:** [Springdoc OpenAPI (Swagger UI)](https://springdoc.org/)
* **Validación:** Jakarta Bean Validation
* **Build Tool:** Gradle

---

## 📚 Documentación de la API (Swagger)

La API está completamente documentada con Swagger UI. Una vez que la aplicación esté corriendo, puedes acceder a la documentación interactiva en la siguiente URL:

**[http://localhost:8080/api-docs.html](http://localhost:8080/api-docs.html)**

Desde la interfaz de Swagger, podrás ver todos los endpoints, sus parámetros, los cuerpos de las peticiones y las posibles respuestas. También podrás **probar los endpoints protegidos** directamente desde la UI:
1.  Usa el endpoint `POST /api/auth/login` para obtener un token JWT.
2.  Haz clic en el botón **"Authorize"** en la parte superior derecha.
3.  Pega tu token en el campo de texto.
4.  ¡Listo! Todas tus peticiones desde Swagger estarán autenticadas.

---

## 🏁 Cómo Empezar

Sigue estos pasos para tener una copia del proyecto funcionando en tu máquina local.

### Prerrequisitos

Asegúrate de tener instalado lo siguiente:
* JDK 17 o superior.
* Gradle 8.0 o superior.
* Una instancia de PostgreSQL corriendo en tu máquina.

### Instalación y Configuración

1.  **Clona el repositorio:**
    ```bash
    git clone [https://github.com/tu-usuario/gastos-api.git](https://github.com/tu-usuario/gastos-api.git)
    cd gastos-api
    ```

2.  **Configura la Base de Datos:**
    * Crea una nueva base de datos en tu instancia de PostgreSQL (ej: `gastos_dev`).

3.  **Configura las Variables de Entorno:**
    * En la raíz del proyecto, crea un fichero `application.properties` (si no existe) o modifica el existente.
    * Asegúrate de que las siguientes propiedades estén configuradas con tus valores locales:
    ```properties
    # Conexión a tu base de datos local
    spring.datasource.url=jdbc:postgresql://localhost:5432/gastos_dev
    spring.datasource.username=tu_usuario_postgres
    spring.datasource.password=tu_contraseña_postgres

    # Clave secreta para firmar los tokens JWT en desarrollo
    jwt.secret=EstaEsUnaClaveSecretaLargaParaDesarrolloNoUsarEnProduccion
    jwt.expiration-in-ms=86400000
    ```

4.  **Ejecuta la aplicación:**
    ```bash
    ./gradlew bootRun
    ```
    ¡La API debería estar corriendo en `http://localhost:8080`!

---

## 💡 Ejemplos de Uso

Aquí tienes un par de ejemplos de cómo interactuar con la API usando cURL.

1.  **Registrar un nuevo usuario:**
    ```bash
    curl -X POST http://localhost:8080/api/auth/registro \
    -H "Content-Type: application/json" \
    -d '{
          "nombre": "Ana García",
          "email": "ana.garcia@ejemplo.com",
          "password": "passwordSegura123"
        }'
    ```

2.  **Iniciar sesión para obtener un token:**
    ```bash
    curl -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{
          "email": "ana.garcia@ejemplo.com",
          "password": "passwordSegura123"
        }'
    ```
    *Copia el `token` que recibes en la respuesta.*

3.  **Crear un nuevo grupo (petición autenticada):**
    ```bash
    TOKEN="pega_tu_token_aqui"

    curl -X POST http://localhost:8080/api/grupos \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN" \
    -d '{
          "nombre": "Viaje a la Montaña",
          "descripcion": "Gastos del viaje de fin de semana."
        }'
    ```

---

## 🏗️ Arquitectura y Decisiones de Diseño

* **Arquitectura en Capas:** El proyecto sigue un diseño clásico de capas (Controlador -> Servicio -> Repositorio) para asegurar una clara separación de responsabilidades y un código mantenible.
* **Seguridad JWT Stateless:** Se optó por una autenticación stateless basada en JWT para asegurar la escalabilidad y la independencia del cliente (web, móvil, etc.).
* **Mapeo con DTOs:** Se utiliza el patrón DTO (Data Transfer Object) para todas las comunicaciones de la API, asegurando que el modelo de datos interno no se exponga y que el contrato de la API sea estable.
* **Gestión de Excepciones Centralizada:** Un `@RestControllerAdvice` maneja todas las excepciones de negocio, proveyendo respuestas de error consistentes y claras.

---

## 📧 Contacto

[Tu Nombre] - [miguel.lozano.cerrada@gmail.com]

Enlace al Proyecto: [https://github.com/tortilicious/portfolio/tree/main/gastos-api](https://github.com/tortilicious/portfolio/tree/main/gastos-api)


