public class ConvertMoney {
    private final String apiKey;

    public ConvertMoney(String apiKey) {
        this.apiKey = apiKey;
    }

    public Currency convert(double amount, String fromCurrency, String toCurrency) {
        validateInput(amount, fromCurrency, toCurrency);
        return ApiClient.convertCurrency(
                fromCurrency.toUpperCase(),
                toCurrency.toUpperCase(),
                amount,
                apiKey
        );
    }

    public Currency convertUsdToCop(double amount) {
        return convert(amount, "USD", "COP");
    }

    public String formatResult(Currency conversion) {
        return String.format("""
            === Resultado de la conversión ===
            Monto original: %.2f %s
            Monto convertido: %.2f %s
            Tasa de cambio: %.4f""",
                conversion.amount(),
                conversion.baseCode(),
                conversion.result(),
                conversion.targetCode(),
                conversion.conversionRate()
        );
    }

    private void validateInput(double amount, String fromCurrency, String toCurrency) {
        if (amount < 0) {
            throw new IllegalArgumentException("El monto debe ser mayor o igual a cero");
        }
        if (fromCurrency == null || fromCurrency.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda de origen no puede estar vacía");
        }
        if (toCurrency == null || toCurrency.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda de destino no puede estar vacía");
        }
    }
}