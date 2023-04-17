package com.example.bookflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.example.bookflix.connection.DataStaxAstraProperties;
import java.nio.file.Path;;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BookflixApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookflixApplication.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

	// @Bean
	// public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
    //     Path bundle = astraProperties.getSecureConnectBundle().toPath();
    //     return builder -> builder.withCloudSecureConnectBundle(bundle).withAuthCredentials("clientId","clientSecret")

	// 			.withKeyspace("main")

	// 			.build();
    // }

	public void CassandraDataAutoConfiguration(com.datastax.oss.driver.api.core.CqlSession session){}

}
