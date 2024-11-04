import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String apiKey = "ab83855b560aac0a297d5eb9";
        ConvertMoney converter = new ConvertMoney(apiKey);
        Scanner read = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    === CONVERSOR DE MONEDAS ===
                    
                    1. Dólar a peso colombiano
                    2. Conversión personalizada
                    3. Salir
                    
                    Seleccione una opción:""");

            try {
                int option = read.nextInt();
                read.nextLine(); // Limpiar buffer

                switch (option) {
                    case 1:
                        System.out.println("Ingrese la cantidad en dólares: ");
                        double amount = read.nextDouble();
                        Currency result = converter.convertUsdToCop(amount);
                        System.out.println(converter.formatResult(result));
                        break;

                    case 2:
                        System.out.println("Ingrese el código de la moneda origen (ejemplo: USD): ");
                        String fromCurrency = read.nextLine().toUpperCase();

                        System.out.println("Ingrese el código de la moneda destino (ejemplo: EUR): ");
                        String toCurrency = read.nextLine().toUpperCase();

                        System.out.println("Ingrese el monto a convertir: ");
                        double customAmount = read.nextDouble();

                        Currency customResult = converter.convert(customAmount, fromCurrency, toCurrency);
                        System.out.println(converter.formatResult(customResult));
                        break;

                    case 3:
                        System.out.println("¡Gracias por usar el conversor!");
                        read.close();
                        return;

                    default:
                        System.out.println("Opción no válida");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println("\nPresione Enter para continuar...");
            read.nextLine();
        }
    }
}