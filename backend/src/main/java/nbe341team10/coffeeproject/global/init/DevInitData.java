package nbe341team10.coffeeproject.global.init;


import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Configuration
public class DevInitData {

    @Bean
    public ApplicationRunner devApplicationRunner() {
        return args -> {
            genApiJsonFile("http://localhost:8080/v3/api-docs/apiV1", "../apiV1.json");
            };
    }

    public void genApiJsonFile(String url, String filename) {
        Path filePath = Path.of(filename);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Files.writeString(filePath, response.body(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("JSON data has been saved to " + filePath.toAbsolutePath());
            } else {
                System.err.println("Error: HTTP status code " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}