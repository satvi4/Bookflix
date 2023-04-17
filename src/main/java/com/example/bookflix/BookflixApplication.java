package com.example.bookflix;

import org.apache.el.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.json.*;

import com.example.bookflix.author.Author;
import com.example.bookflix.author.AuthorRepository;
import com.example.bookflix.connection.DataStaxAstraProperties;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;

import javax.annotation.PostConstruct;;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BookflixApplication {


	@Autowired AuthorRepository authorRepository;

	// @Value("${datadump.location.author}")
	// private String authorDumpLocation;

	// @Value("${datadump.location.works}")
	// private String worksDumpLocation;

	private String workFile   = "src/main/resources/test-authors.txt";
    
    private String authorFile = "src/main/resources/test-authors.txt";

	public static void main(String[] args) {

		SpringApplication.run(BookflixApplication.class, args);
				
	}

	// public void loadAuthors(int startline) throws IOException {
	// 	Path path = Paths.get(authorDumpLocation);
	// 	try(Stream<String> lines= Files.lines(path)){
	// 		lines.forEach(line -> {
	// 			String json = l.substring(l.indexOf("{"));
	// 			try{
	// 				JSONObject jsonObj = new JSONObject(json);
	// 				Author a = new Author();
	// 				a.setName(jsonObj.optString("name"));
	// 				a.setPersonalName(jsonObj.optString("personal_name"));
	// 				a.setId(jsonObj.optString("key").replaceAll("/authors/", ""));
	// 				authorRepository.save(a);
	// 				System.out.println(currentLine.get() + ": Saving author: " + a.getName());
	// 			} catch(JSONException e) {
	// 				e.printStackTrace();
	// 			}
	// 		});
	// 	} catch(IOException e) {
	// 		e.printStackTrace();
	// 	}
		 
	//  }

	public void loadAuthors(int startline) throws IOException {

		final AtomicInteger currentLine = new AtomicInteger(0);
		Files.lines(Paths.get(authorFile)).forEach(l -> {
			try {
				if (currentLine.incrementAndGet()> startline) {
					String json = l.substring(l.indexOf("{"));
					JSONObject jsonObj = new JSONObject(json);
					Author a = new Author();
					a.setName(jsonObj.optString("name"));
					a.setPersonalName(jsonObj.optString("personal_name"));
					a.setId(jsonObj.optString("key").replaceAll("/authors/", ""));
					authorRepository.save(a);
					System.out.println(currentLine.get() + ": Saving author: " + a.getName());
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}); 
	 }

	@PostConstruct
	public void start() throws IOException{
		loadAuthors(0);
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

	// public void CassandraDataAutoConfiguration(com.datastax.oss.driver.api.core.CqlSession session){}

}
