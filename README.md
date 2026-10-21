# 🏥 MedTech — Sistema de Gestión Clínica y Expedientes Médicos

Plataforma integral para la administración de consultas médicas, registro de pacientes, asignación de personal de salud, control de inventario farmacéutico, visualización de métricas analíticas y gestión de bases de datos relacionales.

## 📋 Tabla de contenidos

- [Descripción del proyecto](#-descripción-del-proyecto)
- [Tecnologías y arquitectura](#️-tecnologías-y-arquitectura)
- [Requisitos previos](#-requisitos-previos)
- [Instalación y ejecución local](#-instalación-y-ejecución-local)
- [Credenciales de acceso](#-credenciales-de-acceso)
- [Módulos del sistema](#-módulos-del-sistema)
- [Consumo de API externa](#-consumo-de-api-externa)
- [Respaldo y restauración de la base de datos](#-respaldo-y-restauración-de-la-base-de-datos)
- [Detener la aplicación](#-detener-la-aplicación)

---

## 📌 Descripción del proyecto

**MedTech** es una aplicación web diseñada para centralizar y optimizar la gestión de una clínica médica mediante una arquitectura cliente-servidor.

El sistema permite administrar pacientes, médicos, citas médicas, diagnósticos, prescripciones e insumos farmacéuticos. Además, incorpora un panel de reportes analíticos, autenticación centralizada y herramientas para respaldar y restaurar la información almacenada.

### Objetivos principales

- Centralizar la información clínica y administrativa.
- Facilitar la gestión de pacientes y profesionales de la salud.
- Organizar la programación y el seguimiento de citas médicas.
- Gestionar diagnósticos y prescripciones médicas.
- Controlar el inventario de insumos y detectar niveles críticos de existencias.
- Visualizar indicadores mediante gráficos y reportes.
- Implementar autenticación mediante Keycloak.
- Mantener una estructura de base de datos versionada mediante Liquibase.

## 🛠️ Tecnologías y arquitectura

| Componente              | Tecnología                  | Descripción                                        |
| ----------------------- | --------------------------- | -------------------------------------------------- |
| Backend                 | Spring Boot                 | Desarrollo de la API y lógica de negocio.          |
| Gestión de dependencias | Maven                       | Compilación y administración de dependencias Java. |
| Persistencia            | Spring Data JPA e Hibernate | Acceso y mapeo objeto-relacional.                  |
| Documentación de API    | Swagger / OpenAPI           | Documentación de los endpoints.                    |
| Frontend                | React                       | Interfaz de usuario.                               |
| Herramientas frontend   | Vite                        | Entorno de desarrollo y construcción.              |
| Estilos                 | Bootstrap                   | Diseño y componentes de interfaz.                  |
| Autenticación           | Keycloak                    | Gestión centralizada de usuarios y sesiones.       |
| Protocolos de seguridad | OAuth 2.0 / OpenID Connect  | Autorización e inicio de sesión.                   |
| Base de datos           | PostgreSQL                  | Almacenamiento de información relacional.          |
| Migraciones             | Liquibase                   | Versionado y control de cambios de esquema.        |
| Integración externa     | Spring RestTemplate         | Consumo de servicios REST externos.                |
| Infraestructura         | Docker / Docker Compose     | Ejecución de servicios en contenedores.            |

## 📋 Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener instaladas las siguientes herramientas:

1. **Docker Desktop**, con Docker Compose habilitado.
2. **Java Development Kit (JDK) 21**. Se recomienda [Eclipse Temurin JDK 21 LTS](https://adoptium.net/temurin/releases/?version=21).
3. **Node.js**, versión 20 LTS o 22 LTS.
4. **Git**, para clonar y administrar el repositorio.

Verifica las instalaciones desde una terminal:

```bash
java -version
node -v
npm -v
git --version
docker --version
docker compose version
```

## 🚀 Instalación y ejecución local

### 1. Clonar el repositorio

Reemplaza `TU_USUARIO` por el nombre de usuario u organización propietaria del repositorio.

```bash
git clone https://github.com/TU_USUARIO/medtech-app.git
cd medtech-app
```

### 2. Instalar las dependencias del frontend

Desde la raíz del proyecto, ejecuta:

```bash
npm install
```

Este comando instala las dependencias definidas en `package.json`.

### 3. Levantar la infraestructura con Docker

Inicia PostgreSQL y Keycloak en segundo plano:

```bash
docker compose -f src/main/docker/postgresql.yml up -d
docker compose -f src/main/docker/keycloak.yml up -d
```

Comprueba que los contenedores estén en ejecución:

```bash
docker ps
```

**Importante:** espera a que PostgreSQL esté listo antes de iniciar el backend. Keycloak también necesita tiempo para completar su inicialización y, si corresponde, importar la configuración del realm `jhipster`.

Puedes consultar los registros de cada servicio con:

```bash
docker compose -f src/main/docker/postgresql.yml logs -f
```

```bash
docker compose -f src/main/docker/keycloak.yml logs -f
```

Presiona `Ctrl + C` para salir de la visualización de registros sin detener los contenedores.

### 4. Iniciar el backend (Spring Boot)

Abre una terminal en la raíz del proyecto.

**Windows — CMD:**

```bat
mvnw.cmd
```

**Windows — PowerShell:**

```powershell
.\mvnw.cmd
```

**Linux / macOS:**

```bash
chmod +x mvnw
./mvnw
```

El comando ejecuta la compilación y el inicio de la aplicación mediante Maven Wrapper.

Cuando el backend haya iniciado correctamente, estará disponible en:

- **Backend:** http://localhost:8080/

> Si el proyecto requiere perfiles específicos de Spring o variables de entorno, configura los valores correspondientes antes de iniciarlo.

### 5. Iniciar el frontend (React)

Abre una segunda terminal en la raíz del proyecto y ejecuta:

```bash
npm start
```

Si la configuración del proyecto está preparada para iniciar el servidor de desarrollo en el puerto 9000, podrás acceder a la aplicación desde:

- **Frontend:** http://localhost:9000/

> Si `npm start` no está definido en `package.json`, verifica los scripts disponibles con `npm run`. El comando y el puerto deben coincidir con la configuración real del frontend.

## 🔐 Credenciales de acceso

Al seleccionar **Iniciar sesión / Sign in**, la aplicación redirige al usuario al servicio de autenticación de Keycloak.

Las siguientes credenciales son las predeterminadas del entorno de desarrollo descrito:

| Rol              | Usuario | Contraseña |
| ---------------- | ------- | ---------- |
| Administrador    | `admin` | `admin`    |
| Usuario estándar | `user`  | `user`     |

**Advertencia de seguridad:** estas credenciales son únicamente para pruebas locales, siempre que coincidan con la configuración importada de Keycloak. No deben utilizarse en producción. Cambia las contraseñas predeterminadas y evita publicar credenciales reales en el repositorio.

## 📦 Módulos del sistema

### 1. Gestión de entidades (CRUD)

Los formularios de administración están disponibles desde el menú superior **Entities** y, según la configuración de la interfaz, mediante los accesos directos del dashboard.

#### 👥 Pacientes

Permite registrar y administrar la información de los pacientes:

- Datos personales.
- Identificación única.
- Fecha de nacimiento.
- Tipo de sangre.

#### 🩺 Médicos

Permite gestionar la información del personal médico:

- Datos del profesional.
- Licencias profesionales.
- Especialidades médicas.

#### 📅 Citas médicas

Permite programar y administrar citas vinculando a los pacientes con los médicos correspondientes.

Estados contemplados:

- `PENDIENTE`
- `COMPLETADA`
- `CANCELADA`

#### 📝 Diagnósticos y prescripciones

Permite registrar diagnósticos derivados de las consultas y asociar recetas médicas con las citas correspondientes.

#### 💊 Insumos y farmacia

Permite administrar el inventario de insumos clínicos y farmacéuticos mediante:

- Registro de productos e insumos.
- Control de precios unitarios.
- Seguimiento de existencias.
- Identificación de niveles críticos de inventario.

### 2. Reportes analíticos (`/reports`)

El módulo de reportes proporciona información resumida para facilitar el seguimiento de la actividad clínica y administrativa.

| Reporte                         | Descripción                                                                                                               |
| ------------------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| Citas por médico                | Gráfico de las citas atendidas por cada médico.                                                                           |
| Distribución por tipo de sangre | Gráfico de la cantidad de pacientes según su tipo de sangre.                                                              |
| Citas por estado                | Resumen cuantitativo de citas completadas y pendientes, con posibilidad de mostrar otros estados según la implementación. |
| Inventario crítico              | Identificación de insumos con existencias iguales o inferiores a 10 unidades.                                             |
| Historial de diagnósticos       | Listado histórico de los últimos diagnósticos y prescripciones registrados.                                               |

**Umbral de inventario crítico:** se consideran críticos los insumos con un stock menor o igual a 10 unidades.

### 3. Consumo de API externa

El backend integra un servicio externo de tipos de cambio mediante `RestTemplate`.

**Servicio utilizado:** [Open Exchange Rates API — open.er-api.com](https://open.er-api.com/)

La información de tipos de cambio se presenta en la cabecera del panel de reportes para proporcionar una referencia actualizada de las cotizaciones disponibles.

La consulta depende de la disponibilidad del servicio externo y de la conectividad a Internet.

### 4. Respaldo y restauración de la base de datos

El proyecto contempla procedimientos para crear copias de seguridad de PostgreSQL y recuperar información previamente respaldada.

#### Generar un respaldo

El siguiente ejemplo utiliza `pg_dump` desde el contenedor de PostgreSQL. Sustituye `NOMBRE_CONTENEDOR`, `USUARIO_BD` y `NOMBRE_BD` por los valores reales de tu configuración.

```bash
docker exec NOMBRE_CONTENEDOR pg_dump \
  -U USUARIO_BD \
  -d NOMBRE_BD \
  > backup.sql
```

El archivo `backup.sql` se genera en el directorio de la máquina anfitriona desde el que se ejecuta el comando.

#### Restaurar un respaldo

Para restaurar un archivo SQL compatible con `psql`:

```bash
docker exec -i NOMBRE_CONTENEDOR psql \
  -U USUARIO_BD \
  -d NOMBRE_BD \
  < backup.sql
```

**Precauciones:**

- Verifica el nombre de la base de datos y del contenedor antes de ejecutar los comandos.
- Comprueba que el archivo de respaldo sea válido y corresponda a la base de datos esperada.
- Realiza un respaldo antes de restaurar datos.
- Una restauración puede crear conflictos con objetos existentes o modificar información. Utiliza una base de datos de prueba cuando corresponda.
- Mantén las copias de seguridad en un lugar seguro y fuera del repositorio público.

## 🛑 Detener la aplicación

### 1. Detener el backend y el frontend

En las terminales donde se están ejecutando Spring Boot y React, presiona:

```text
Ctrl + C
```

### 2. Detener los contenedores de PostgreSQL y Keycloak

Desde la raíz del proyecto, ejecuta:

```bash
docker compose -f src/main/docker/postgresql.yml down
docker compose -f src/main/docker/keycloak.yml down
```

Este procedimiento detiene y elimina los contenedores administrados por esos archivos Compose, pero normalmente conserva los datos almacenados en volúmenes persistentes.

**No utilices `docker compose down -v`** si deseas conservar los datos de la base de datos, ya que la opción `-v` puede eliminar los volúmenes asociados.

## 📄 Licencia

La licencia del proyecto debe definirse según las condiciones de distribución y uso establecidas por sus autores. Si deseas publicarlo como software de código abierto, añade un archivo `LICENSE` en la raíz del repositorio.

---

**MedTech** — Sistema de Gestión Clínica y Expedientes Médicos.

_Proyecto de desarrollo de software orientado a la gestión clínica, la administración de información médica y el análisis de datos._
