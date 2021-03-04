package es.centic.taller1.taller1.users;

import java.io.IOException;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class AvatarServiceIntegrationTests {
    
    private static MockWebServer mockApiServer;
    private AvatarService avatarService;

    @BeforeAll
    static void setupTests() throws IOException {
        mockApiServer = new MockWebServer();
        mockApiServer.start();
    }

    @AfterAll
    static void clearTests() throws IOException {
        mockApiServer.shutdown();
    }

    @BeforeEach
    void initApiServer() {
        String url = String.format("http://localhost:%s", mockApiServer.getPort());
        avatarService = new AvatarService(url);
    }

    @Test
    void getImageFromAPIWhenAvatarIsNull() throws IOException {
        String avatarExpected = "http://avatar.io/myavatar";

        mockApiServer.enqueue(
            new MockResponse()
                .setBody(String.format("{\"image\":\"%s\"}", avatarExpected))
                .addHeader("Content-Type", "application/json")
        );

        assertThat(avatarService.cacheAvatar(null)).isEqualTo(avatarExpected);
    }
}
