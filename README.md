# Money-Converter

A **Java** currency converter with an interactive terminal menu. It converts US dollars to Colombian pesos or lets you choose a currency pair and retrieve the conversion through **ExchangeRate-API**.

I developed this project during my **intensive seven-month Oracle and Alura bootcamp**, as part of **ONE: Oracle Next Education — Programa Oracle Next Education F2 T7 Back-end**. The challenge put object-oriented programming, API integration, JSON processing, and console input handling into practice. The first version recorded in this repository dates to November 4, 2024.

## Features

- Convert **USD to COP** through a dedicated menu option.
- Perform a **custom conversion** by entering source and target currency codes, such as `EUR` and `USD`.
- Display the original amount, converted amount, and exchange rate.
- Reject negative amounts and empty currency codes; the API determines whether the supplied codes are supported.
- Keep the menu available for further conversions until the user chooses to exit.

This is a console application that runs in a terminal or an IDE. It does not include a graphical interface, web server, or money transfer functionality. Conversions require an internet connection and a valid API key.

## API

The project uses [ExchangeRate-API](https://www.exchangerate-api.com), specifically its version 6 **Pair Conversion** endpoint. For each conversion, `ApiClient` sends an HTTP `GET` request with this structure:

```text
https://v6.exchangerate-api.com/v6/YOUR_API_KEY/pair/SOURCE_CURRENCY/TARGET_CURRENCY/AMOUNT
```

For example, converting `100` from `USD` to `COP` returns JSON containing `base_code`, `target_code`, `conversion_rate`, and `conversion_result`. The application parses that response with **Gson** and displays the result calculated by the API.

Currencies use ISO 4217 codes such as `USD`, `COP`, and `EUR`. Supported currencies, request quotas, and rate refresh intervals depend on the provider and account plan; the application does not guarantee real-time quotes.

- [Pair Conversion documentation](https://www.exchangerate-api.com/docs/pair-conversion-requests)
- [Supported currencies](https://www.exchangerate-api.com/docs/supported-currencies)

## Technologies and requirements

| Component | Purpose |
| --- | --- |
| **JDK 17 or later, recommended** | Compile and run the application with `javac` and `java` |
| **Java HttpClient** | Make HTTP requests to the API |
| **Gson 2.11.0** | Parse JSON; this is the version referenced by the IntelliJ module files |
| **Scanner** | Read menu choices and amounts from the console |
| **ExchangeRate-API account and key** | Authorize conversion requests |
| **Internet connection** | Reach the external service |

The source uses a `record` and multiline text blocks, requiring at least Java 16 without preview features. This guide recommends JDK 17 or later. A JRE alone is not enough to follow the compilation steps.

The project does not use Maven or Gradle. Gson must be added to the classpath or configured as an IDE dependency.

## Platforms

**The application is not limited to Windows.** Its source uses Java and Gson without Windows-specific calls.

| Platform | Expected way to run it |
| --- | --- |
| Windows | PowerShell or an IDE with a compatible JDK |
| macOS, Intel or Apple Silicon | Terminal or an IDE with a JDK matching the processor architecture |
| Linux | A terminal or IDE with a compatible JDK |
| Raspberry Pi running ARM64 Linux | A terminal with a compatible ARM64 JDK |

This portability assessment is based on the source and dependencies; it does not mean the application has been tested on every platform listed. See [Eclipse Temurin's supported platforms](https://adoptium.net/supported-platforms) for available JDK distributions.

## Setup

### 1. Get the project and check Java

```sh
git clone https://github.com/alfredojvp/Money-Converter.git
cd Money-Converter
java -version
javac -version
```

Run the following commands from this repository's root directory.

### 2. Add Gson

Download [gson-2.11.0.jar from Maven Central](https://repo.maven.apache.org/maven2/com/google/code/gson/gson/2.11.0/gson-2.11.0.jar), create a `lib` folder in the repository root, and place the file there:

```text
Money-Converter/
├── lib/
│   └── gson-2.11.0.jar
└── MoneyConverter/
    └── src/
```

The library is not bundled with the project. The `.iml` files retain references from the original IntelliJ environment; one points to a local downloads folder that will not automatically exist on another computer.

### 3. Configure your API key

Get your own key from your [ExchangeRate-API account](https://www.exchangerate-api.com). The application reads it from the **`EXCHANGE_RATE_API_KEY`** environment variable. No source edit is needed.

Set the variable in the terminal session or IDE run configuration used to launch the application. If it is missing, empty, or whitespace-only, the program prints a configuration message and exits before making a request. The program does not load `.env` files automatically.

Keep your key private. The commands below prompt for it without displaying it or including its value in a command saved to shell history.

## Compile and run

Compile the files in `MoneyConverter/src/`. The following commands create fresh output in `build/`. Generated classes, build directories, local dependency downloads, and `.env` files are excluded from Git.

### Linux and macOS

Start Bash with `bash` if your current shell is different, then run:

```bash
read -r -s -p 'ExchangeRate-API key: ' EXCHANGE_RATE_API_KEY
printf '\n'
export EXCHANGE_RATE_API_KEY
mkdir -p build
javac -encoding UTF-8 -cp "lib/gson-2.11.0.jar" -d build MoneyConverter/src/*.java
java -cp "build:lib/gson-2.11.0.jar" Main
```

### Windows — PowerShell

Set the variable in the current PowerShell session and compile the four source files:

```powershell
$apiKeyInput = Read-Host "ExchangeRate-API key" -AsSecureString
$env:EXCHANGE_RATE_API_KEY = [System.Net.NetworkCredential]::new("", $apiKeyInput).Password
New-Item -ItemType Directory -Force build | Out-Null
$sources = (Get-ChildItem ".\MoneyConverter\src\*.java").FullName
javac -encoding UTF-8 -cp "lib\gson-2.11.0.jar" -d build $sources
java -cp "build;lib\gson-2.11.0.jar" Main
```

The classpath separator is `:` on Linux/macOS and `;` on Windows.

### IntelliJ IDEA

1. Open the project and select JDK 17 or later.
2. Mark `MoneyConverter/src` as a source directory.
3. Add `lib/gson-2.11.0.jar` to the module dependencies and replace any unresolved dependency references from the original environment.
4. Add `EXCHANGE_RATE_API_KEY` to the environment variables in the run configuration.
5. Run the `main` method in `Main.java`.

## Usage

The application's prompts remain in Spanish. On startup, it displays:

```text
=== CONVERSOR DE MONEDAS ===

1. Dólar a peso colombiano
2. Conversión personalizada
3. Salir

Seleccione una opción:
```

| Option | Meaning and requested input |
| --- | --- |
| `1` | US dollars to Colombian pesos: enter the amount in USD |
| `2` | Custom conversion: enter the source code, target code, and amount |
| `3` | Exit the program |

Illustrative output for option `1`, using an amount of `100` and a hypothetical rate of `4000` COP per USD:

```text
=== Resultado de la conversión ===
Monto original: 100.00 USD
Monto convertido: 400000.00 COP
Tasa de cambio: 4000.0000
```

These labels mean “conversion result,” “original amount,” “converted amount,” and “exchange rate.” The rate above is fictional; an actual conversion displays the provider's response. Input and output decimal separators depend on Java's default locale because both `Scanner` and `String.format` use the computer's regional settings.

## Code structure

| File | Responsibility |
| --- | --- |
| [`Main.java`](MoneyConverter/src/Main.java) | Entry point, API key configuration, interactive menu, input, and result/error display |
| [`ConvertMoney.java`](MoneyConverter/src/ConvertMoney.java) | Basic validation, uppercase currency codes, USD/COP conversion, and output formatting |
| [`ApiClient.java`](MoneyConverter/src/ApiClient.java) | URL construction, HTTP request, and JSON parsing with Gson |
| [`Currency.java`](MoneyConverter/src/Currency.java) | A `record` containing the currencies, rate, original amount, and result; also provides a helper to recalculate an amount using the same rate |

The main flow is: **menu → validation → API request → `Currency` object → console output**. In that flow, the converted amount comes from the API's `conversion_result` field.

## Scope of this version

This is an educational project with basic validation. It uses `double`, displays amounts with two decimal places, and does not add bank fees. It does not save conversion history or cache exchange rates for offline use.

Connection failures and HTTP responses other than `200` are reported as errors. Unexpected JSON responses and nonnumeric input receive general exception handling. The application does not explicitly configure request timeouts.

A successful conversion also depends on a valid API key, the provider's availability, and the account's request quota.

## Author and license

**Alfredo José Vélez Parra** — developed during Oracle and Alura's **Programa Oracle Next Education F2 T7 Back-end**, part of ONE: Oracle Next Education.

Distributed under the [MIT license](LICENSE).
