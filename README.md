# Money-Converter

Conversor de divisas desarrollado en **Java**, con un menú interactivo en la terminal. Permite convertir dólares estadounidenses a pesos colombianos o elegir un par de monedas y consultar su conversión mediante **ExchangeRate-API**.

Desarrollé este proyecto como parte de mis estudios en el **bootcamp intensivo de 7 meses de Oracle y Alura**, dentro del **Programa Oracle Next Education F2 T7 Back-end**, perteneciente a **ONE: Oracle Next Education**. Fue un challenge para aplicar programación orientada a objetos, consumo de APIs, procesamiento de JSON y manejo de entradas por consola. La primera versión registrada en este repositorio es del 4 de noviembre de 2024.

## ¿Qué hace?

- Convierte un monto de **USD a COP** mediante una opción directa.
- Permite una **conversión personalizada**, indicando los códigos de origen y destino, por ejemplo `EUR` y `USD`.
- Muestra el monto original, el monto convertido y la tasa de cambio utilizada.
- Rechaza montos negativos y códigos de moneda vacíos; la API determina si los códigos son compatibles.
- Mantiene el menú disponible para realizar nuevas conversiones hasta seleccionar la opción de salida.

Es una aplicación de consola: se ejecuta en una terminal o desde un IDE. No incluye una interfaz gráfica, un servidor web ni funciones para transferir dinero. Las conversiones requieren conexión a internet y una clave válida del proveedor.

## API utilizada

El proyecto utiliza [ExchangeRate-API](https://www.exchangerate-api.com), concretamente su endpoint **Pair Conversion** de la versión 6. Por cada conversión, `ApiClient` envía una petición HTTP `GET` con esta estructura:

```text
https://v6.exchangerate-api.com/v6/TU_API_KEY/pair/MONEDA_ORIGEN/MONEDA_DESTINO/MONTO
```

Por ejemplo, una solicitud de `USD` a `COP` para un monto de `100` obtiene una respuesta JSON con los campos `base_code`, `target_code`, `conversion_rate` y `conversion_result`. La aplicación lee esa respuesta con **Gson** y muestra el resultado calculado por la API.

Los códigos de moneda siguen el formato ISO 4217, como `USD`, `COP` y `EUR`. La disponibilidad de monedas, las cuotas de consultas y la actualización de las tasas dependen del servicio y del plan contratado; el programa no garantiza cotizaciones en tiempo real.

- [Documentación de Pair Conversion](https://www.exchangerate-api.com/docs/pair-conversion-requests)
- [Monedas admitidas](https://www.exchangerate-api.com/docs/supported-currencies)

## Tecnologías y requisitos

| Componente | Uso |
| --- | --- |
| **JDK 17 o posterior, recomendado** | Compilar y ejecutar el programa con `javac` y `java` |
| **Java HttpClient** | Realizar las solicitudes HTTP a la API |
| **Gson 2.11.0** | Leer la respuesta JSON; es la versión referenciada por los archivos de IntelliJ |
| **Scanner** | Recibir las opciones y los montos introducidos por el usuario |
| **Cuenta y clave de ExchangeRate-API** | Autorizar las consultas de conversión |
| **Conexión a internet** | Acceder al servicio externo |

El código utiliza un `record` y bloques de texto multilínea. Para compilarlo sin funciones experimentales requiere al menos Java 16; esta guía recomienda JDK 17 o superior, y los `.class` incluidos en el repositorio corresponden a Java 17. No basta con un JRE para seguir los pasos de compilación.

El proyecto no incorpora Maven ni Gradle: la dependencia Gson debe añadirse al classpath o configurarse en el IDE.

## Plataformas

**El código no está limitado a Windows.** Utiliza Java y Gson sin llamadas específicas a ese sistema operativo.

| Plataforma | Cómo podría ejecutarse |
| --- | --- |
| Windows | Desde PowerShell o un IDE, con un JDK compatible |
| macOS, Intel o Apple Silicon | Desde Terminal o un IDE, con el JDK de la arquitectura correspondiente |
| Linux | Desde una terminal o un IDE, con un JDK compatible |
| Raspberry Pi con Linux ARM64 | Desde una terminal, con un JDK compatible para ARM64 |

Esta compatibilidad se deduce de las dependencias y del código fuente; no implica que esta versión haya sido probada en todos esos entornos. Puede consultarse la disponibilidad de distribuciones del JDK en [Eclipse Temurin](https://adoptium.net/supported-platforms).

## Preparación

### 1. Obtener el proyecto y comprobar Java

```sh
git clone https://github.com/alfredojvp/Money-Converter.git
cd Money-Converter
java -version
javac -version
```

Los comandos siguientes se ejecutan desde esta carpeta raíz del repositorio.

### 2. Añadir Gson

Descarga [gson-2.11.0.jar desde Maven Central](https://repo.maven.apache.org/maven2/com/google/code/gson/gson/2.11.0/gson-2.11.0.jar), crea una carpeta `lib` en la raíz y guarda el archivo allí:

```text
Money-Converter/
├── lib/
│   └── gson-2.11.0.jar
└── MoneyConverter/
    └── src/
```

La biblioteca no está incluida en el repositorio. Los archivos `.iml` conservan referencias del entorno original de IntelliJ; una de ellas apunta a una carpeta local de descargas que no estará disponible automáticamente en otro equipo.

### 3. Configurar una clave propia

Obtén tu clave desde tu cuenta de [ExchangeRate-API](https://www.exchangerate-api.com). La versión actual declara `apiKey` directamente en `MoneyConverter/src/Main.java`; no lee variables de entorno de forma automática.

Para usar una variable de entorno en tu copia local, sustituye **solo esa declaración** por:

```java
String apiKey = System.getenv("EXCHANGE_RATE_API_KEY");
```

Después, define `EXCHANGE_RATE_API_KEY` en la misma terminal o en la configuración de ejecución del IDE antes de iniciar el programa. No uses ni dependas de la clave incluida en la versión original, y no publiques tu clave personal. La modificación anterior es un paso de configuración local: el código fuente de esta versión todavía conserva la declaración original.

## Compilar y ejecutar

Usa los archivos de `MoneyConverter/src/`. Las carpetas `out/` contienen compilaciones anteriores; estos pasos generan una compilación nueva en `build/`.

### Linux y macOS

Después de configurar la lectura de la variable de entorno como se explica arriba, puedes introducir la clave sin mostrarla en pantalla ni escribirla como parte de un comando guardado en el historial. Este bloque usa Bash:

```bash
bash
read -r -s -p 'Clave de ExchangeRate-API: ' EXCHANGE_RATE_API_KEY
printf '\n'
export EXCHANGE_RATE_API_KEY
mkdir -p build
javac -encoding UTF-8 -cp "lib/gson-2.11.0.jar" -d build MoneyConverter/src/*.java
java -cp "build:lib/gson-2.11.0.jar" Main
```

### Windows — PowerShell

Configura la misma variable en la sesión de PowerShell y compila las cuatro clases:

```powershell
$apiKeyInput = Read-Host "Clave de ExchangeRate-API" -AsSecureString
$env:EXCHANGE_RATE_API_KEY = [System.Net.NetworkCredential]::new("", $apiKeyInput).Password
New-Item -ItemType Directory -Force build | Out-Null
$sources = (Get-ChildItem ".\MoneyConverter\src\*.java").FullName
javac -encoding UTF-8 -cp "lib\gson-2.11.0.jar" -d build $sources
java -cp "build;lib\gson-2.11.0.jar" Main
```

El separador del classpath cambia según el sistema: `:` en Linux/macOS y `;` en Windows.

### IntelliJ IDEA

1. Abre el proyecto y selecciona un JDK 17 o superior.
2. Configura `MoneyConverter/src` como carpeta de código fuente.
3. Añade `lib/gson-2.11.0.jar` a las dependencias del módulo y reemplaza cualquier referencia local antigua que no se resuelva.
4. Aplica la configuración de clave descrita arriba y define `EXCHANGE_RATE_API_KEY` en las variables de entorno de la configuración de ejecución.
5. Ejecuta el método `main` de `Main.java`.

## Uso

Al iniciar, se presenta este menú:

```text
=== CONVERSOR DE MONEDAS ===

1. Dólar a peso colombiano
2. Conversión personalizada
3. Salir

Seleccione una opción:
```

| Opción | Datos que solicita |
| --- | --- |
| `1` | El monto en dólares estadounidenses que se convertirá a pesos colombianos |
| `2` | Código de moneda de origen, código de destino y monto |
| `3` | Cierra el programa |

Ejemplo ilustrativo de la opción `1`, con un monto de `100` y una tasa hipotética de `4000` COP por USD:

```text
=== Resultado de la conversión ===
Monto original: 100.00 USD
Monto convertido: 400000.00 COP
Tasa de cambio: 4000.0000
```

La tasa del ejemplo es ficticia; una ejecución real muestra la respuesta recibida del proveedor. El separador decimal de entrada y salida depende de la configuración regional de Java, porque `Scanner` y `String.format` usan la configuración predeterminada del equipo.

## Organización del código

| Archivo | Responsabilidad |
| --- | --- |
| [`Main.java`](MoneyConverter/src/Main.java) | Punto de entrada, menú interactivo, lectura de datos y presentación de resultados o errores |
| [`ConvertMoney.java`](MoneyConverter/src/ConvertMoney.java) | Validación básica, normalización a mayúsculas, conversión USD/COP y formato de salida |
| [`ApiClient.java`](MoneyConverter/src/ApiClient.java) | Construcción de la URL, petición HTTP y lectura del JSON con Gson |
| [`Currency.java`](MoneyConverter/src/Currency.java) | `record` que contiene monedas, tasa, monto original y resultado; incluye un método auxiliar para recalcular un monto con la misma tasa |

El recorrido principal es: **menú → validación → consulta a la API → objeto `Currency` → resultado en consola**. En este recorrido, el resultado de la conversión procede del campo `conversion_result` de la API.

## Alcance de esta versión

Es un proyecto educativo con validaciones básicas. Utiliza `double`, muestra los montos con dos decimales y no añade comisiones bancarias. No guarda un historial de conversiones ni conserva tasas para trabajar sin internet.

Los fallos de conexión y las respuestas HTTP distintas de `200` se presentan como errores. El manejo de respuestas JSON inesperadas y de entradas no numéricas es general, y las peticiones no tienen un tiempo límite configurado explícitamente en la aplicación.

Esta documentación se elaboró revisando el código y la documentación del proveedor. No certifica la vigencia de la clave original ni una prueba de conversión en vivo.

## Autor y licencia

**Alfredo José Vélez Parra** — proyecto realizado durante el **Programa Oracle Next Education F2 T7 Back-end**, de Oracle y Alura.

Distribuido bajo la [licencia MIT](LICENSE).
