Controla Gastos V3 – Evaluación 3

Aplicación Android desarrollada en Kotlin + Jetpack Compose, conectada a un microservicio Spring Boot desplegado en AWS EC2, e integrando una API externa de tipo currency/exchange rates.

Vicente Varela Ríos
Funcionalidades
 Android (Kotlin + Compose)

Crear, listar, editar y eliminar gastos.

Guardar descripción, monto, categoría y fecha.

Validaciones de campos.

Conexión al backend mediante Retrofit.

Consumo de API externa (valor del dólar USD → CLP).

Navegación con Jetpack Navigation.

Vista para agregar gastos con feedback visual.

Ejecución en dispositivo físico Android.

 Backend (Spring Boot)

CRUD completo de gastos.

DTO + Entity + Repository + Service + Controller.

Conexión a base de datos PostgreSQL en Render.

Despliegue en AWS EC2.

CORS habilitado para permitir conexión desde Android.

Exposición de endpoints REST.

 Endpoints Utilizados
 Microservicio Control de Gastos

Base URL en producción (EC2):

http://3.239.9.101:8080/api/gastos

Listar gastos

GET /api/gastos

 Crear gasto

POST /api/gastos

 Obtener gasto por ID

GET /api/gastos/{id}

 Actualizar gasto

PUT /api/gastos/{id}

 Eliminar gasto

DELETE /api/gastos/{id}

 API Externa Utilizada
 ExchangeRate-API (valor USD)

Endpoint consumido:

https://v6.exchangerate-api.com/v6/<API_KEY>/latest/USD


Datos usados:

Conversión USD → CLP mostrada en la app.

 Pasos para Ejecutar el Proyecto
 1. Backend Spring Boot

Instalar Java 17 y Maven.

Configurar application.properties con la conexión PostgreSQL.

Ejecutar:

mvn clean package -DskipTests


Ejecutar el .jar local:

java -jar target/control-gastos-backend-0.0.1-SNAPSHOT.jar


(Opcional) Subir a EC2 mediante SCP/WinSCP/PuTTY.

2. Aplicación Android

Abrir el proyecto en Android Studio.

Editar la URL del backend en RetrofitInstance.kt:

private const val BASE_URL = "http://3.239.9.101:8080/api/"


Asegurar permiso de Internet en el manifest.

Ejecutar la app en un dispositivo físico o emulador.
