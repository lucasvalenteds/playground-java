package com.playground.java.selenium;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerStub {

    protected static String serverUrl;

    private static WireMockServer server;

    @BeforeAll
    public static void beforeAll() throws IOException {
        server = new WireMockServer(
            WireMockConfiguration.options()
                .notifier(new ConsoleNotifier(false))
                .dynamicPort()
        );

        server.stubFor(
            WireMock.get(WireMock.anyUrl())
                .willReturn(
                    new ResponseDefinitionBuilder()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBody(Files.readString(
                            Path.of(ClassLoader.getSystemResource("index.html").getPath())
                        ))
                )
        );

        server.start();

        serverUrl = server.baseUrl();
    }

    @AfterAll
    public static void afterAll() {
        server.verify(WireMock.moreThan(0), WireMock.getRequestedFor(WireMock.anyUrl()));

        server.stop();
    }
}
