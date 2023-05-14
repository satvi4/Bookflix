package com.example.bookflix;

import com.datastax.oss.driver.api.core.CqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

@SpringBootTest
class CassandraConnectionTest {

    @Value("${datastax.astra.secure-connect-bundle}")
    private Path secureConnectBundlePath;

    @Test
    void testCassandraConnection() {
        try (CqlSession session = CqlSession.builder()
                .withCloudSecureConnectBundle(secureConnectBundlePath)
                .build()) {
            assertNotNull(session);
            
        }
    }
}
