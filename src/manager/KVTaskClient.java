package manager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private HttpClient httpClient;
    private String apiToken;
    private static String urlString;

    public KVTaskClient(String urlString) {
        this.urlString = urlString;
        httpClient = HttpClient.newHttpClient();
        URI uriRegister = URI.create(urlString + "/register");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(uriRegister)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        System.out.println(uriRegister);
        try {
            HttpResponse<String> response = httpClient.send(request, handler);
            apiToken = response.body();
        } catch (InterruptedException | IOException exception) {
            System.out.println("Ошибка регистрации на сервере");;
        }
    }

    public void put(String key, String json) {
        httpClient = HttpClient.newHttpClient();
        URI uriSave = URI.create(urlString + "/save/" + key + "?API_TOKEN=" +apiToken);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .POST(body)
                .uri(uriSave)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = httpClient.send(request, handler);
            System.out.println("Код ответа: " + response.statusCode());
        } catch (InterruptedException | IOException exception) {
            System.out.println("Ошибка сохранения данных на сервер");
        }
    }

    public String load(String key) {
        httpClient = HttpClient.newHttpClient();
        URI uriLoad = URI.create(urlString + "/load/" + key + "?API_TOKEN=" +apiToken);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(uriLoad)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = httpClient.send(request, handler);
            if (response.statusCode() == 404) {
                return null;
            }
            return response.body();
        } catch (InterruptedException | IOException exception) {
            exception.printStackTrace();
            System.out.println("Ошибка чтения данных на сервере");
        }
        return null;
    }
}
