package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String key;
    private final String source;
    private URI URL;
    private HttpClient httpClient;

    public KVTaskClient(String source) {
        this.source = source;
        this.key = register();
    }

    private String register() {
        httpClient = HttpClient.newHttpClient();
        URL = URI.create(source + "/register");
        HttpRequest request = HttpRequest.newBuilder().uri(URL).GET().build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void put(String value) {
        httpClient = HttpClient.newHttpClient();
        URL = URI.create(source + "/save/?API_TOKEN=" + key);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value);
        HttpRequest request = HttpRequest.newBuilder().uri(URL).POST(body).build();

        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load() {
        httpClient = HttpClient.newHttpClient();
        URL = URI.create(source + "/load/?API_TOKEN=" + key);
        HttpRequest request = HttpRequest.newBuilder().uri(URL).GET().build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
