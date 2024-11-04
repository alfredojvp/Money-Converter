import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class ApiClient {
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static Currency convertCurrency(String baseCode, String targetCode, double amount, String apiKey) {
        try {
            String apiUrl = BASE_URL + apiKey + "/pair/" + baseCode + "/" + targetCode + "/" + amount;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

                return new Currency(
                        jsonResponse.get("base_code").getAsString(),
                        jsonResponse.get("target_code").getAsString(),
                        jsonResponse.get("conversion_rate").getAsDouble(),
                        amount,
                        jsonResponse.get("conversion_result").getAsDouble()
                );
            } else {
                throw new RuntimeException("Error en la API: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al realizar la petición: " + e.getMessage());
        }
    }
}