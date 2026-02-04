package com.synergybridge;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@Tag("container")
class SynergyBridgeContainerIT {

    @Container
    static final GenericContainer<?> container = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withDockerfile(Path.of("Dockerfile")))
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/").forPort(8080).forStatusCode(200));

    @Test
    void healthEndpointReturnsJavaVersion() throws Exception {
        String url = "http://" + container.getHost() + ":" + container.getMappedPort(8080) + "/";

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder(URI.create(url)).GET().build(),
                        HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"javaVersion\""));
    }

    @Test
    void mcpInitializeReturnsProtocolVersion() throws Exception {
        String url = "http://" + container.getHost() + ":" + container.getMappedPort(8080) + "/mcp";

        String body = """
                {
                  "jsonrpc": "2.0",
                  "id": 1,
                  "method": "initialize",
                  "params": {
                    "protocolVersion": "2025-03-26",
                    "capabilities": {},
                    "clientInfo": { "name": "container-test", "version": "1.0.0" }
                  }
                }
                """;

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder(URI.create(url))
                                .POST(HttpRequest.BodyPublishers.ofString(body))
                                .header("Content-Type", "application/json")
                                .build(),
                        HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"protocolVersion\""));
    }
}
