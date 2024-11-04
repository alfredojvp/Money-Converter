public record Currency(
        String baseCode,
        String targetCode,
        double conversionRate,
        double amount,
        double result
) {
    // Constructor simplificado para cuando solo se necesitan los datos básicos
    public Currency(String baseCode, String targetCode, double conversionRate) {
        this(baseCode, targetCode, conversionRate, 0.0, 0.0);
    }

    // Método para crear una nueva Currency con un monto específico
    public Currency withAmount(double newAmount) {
        return new Currency(
                baseCode,
                targetCode,
                conversionRate,
                newAmount,
                newAmount * conversionRate
        );
    }
}