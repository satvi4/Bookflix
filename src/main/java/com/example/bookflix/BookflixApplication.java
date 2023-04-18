package com.example.bookflix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.*;

import com.example.bookflix.author.Author;
import com.example.bookflix.author.AuthorRepository;
import com.example.bookflix.book.Book;
import com.example.bookflix.book.BookRepository;
import com.example.bookflix.connection.DataStaxAstraProperties;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BookflixApplication {

	@Autowired
	AuthorRepository authorRepository;

	@Autowired
	BookRepository bookRepository;

	// @Value("${datadump.location.author}")
	// private String authorDumpLocation;

	// @Value("${datadump.location.works}")
	// private String worksDumpLocation;

	private String workFile = "src/main/resources/test-works.txt";

	private String authorFile = "src/main/resources/test-authors.txt";

	public static void main(String[] args) {

		SpringApplication.run(BookflixApplication.class, args);

	}

	// public void loadAuthors(int startline) throws IOException {
	// Path path = Paths.get(authorDumpLocation);
	// try(Stream<String> lines= Files.lines(path)){
	// lines.forEach(line -> {
	// String json = l.substring(l.indexOf("{"));
	// try{
	// JSONObject jsonObj = new JSONObject(json);
	// Author a = new Author();
	// a.setName(jsonObj.optString("name"));
	// a.setPersonalName(jsonObj.optString("personal_name"));
	// a.setId(jsonObj.optString("key").replaceAll("/authors/", ""));
	// authorRepository.save(a);
	// System.out.println(currentLine.get() + ": Saving author: " + a.getName());
	// } catch(JSONException e) {
	// e.printStackTrace();
	// }
	// });
	// } catch(IOException e) {
	// e.printStackTrace();
	// }

	// }

	public void loadAuthors(int startline) throws IOException {

		final AtomicInteger currentLine = new AtomicInteger(0);
		Files.lines(Paths.get(authorFile)).forEach(l -> {
			try {
				if (currentLine.incrementAndGet() > startline) {
					String json = l.substring(l.indexOf("{"));
					JSONObject jsonObj = new JSONObject(json);
					Author a = new Author();
					a.setName(jsonObj.optString("name"));
					a.setPersonalName(jsonObj.optString("personal_name"));
					a.setId(jsonObj.optString("key").replaceAll("/authors/", ""));
					authorRepository.save(a);
					System.out.println(currentLine.get() + ": Saving author: " + a.getName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void loadBooks() throws IOException {
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
		Files.lines(Paths.get(workFile)).forEach(l -> {
			try {
				String json = l.substring(l.indexOf("{"));
				JSONObject jsonObj = new JSONObject(json);
				Book b = new Book();
				b.setId(jsonObj.getString("key").replace("/works/", ""));
				b.setName(jsonObj.optString("title"));

				JSONObject jsonDesc = jsonObj.optJSONObject("decription");
				if (jsonDesc != null) {
					b.setDescription(jsonDesc.optString("value"));
				}

				JSONObject pubdate = jsonObj.optJSONObject("created");
				if (pubdate != null) {
					String srVal = pubdate.optString("value");
					b.setPublishedDate(LocalDate.parse(srVal, dt));
				}

				JSONArray coversArray = jsonObj.optJSONArray("covers");
				if (coversArray != null) {
					List<String> coverIds = new ArrayList<>();
					for (int i = 0; i < coversArray.length(); i++) {
						coverIds.add(String.valueOf(coversArray.getLong(i)));
					}
					b.setCoverIds(coverIds);
				}

				JSONArray authorsArray = jsonObj.optJSONArray("authors");
				if (authorsArray != null) {
					List<String> authorIds = new ArrayList<>();
					for (int i = 0; i < authorsArray.length(); i++) {
						String authorid = authorsArray.getJSONObject(i).getJSONObject("author").getString("key")
								.replace("/authors/", "");
						authorIds.add(authorid);
					}
					b.setAuthorIds(authorIds);
					List<String> authorNames = authorIds.stream().map(id -> authorRepository.findById(id))
							.map(optAuthor -> {
								if (!optAuthor.isPresent())
									return "Unknown";
								return optAuthor.get().getName();

							}).collect(Collectors.toList());
					b.setAuthorNames(authorNames);
				}

				System.out.println("Saving book..." + b.getName());
				bookRepository.save(b);

			} catch (Exception e) {
				System.out.println(e);
			}
		});
	}

	// @PostConstruct
	// public void start() throws IOException {
	// loadAuthors(0);
	// loadBooks();
	// }



	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	// @Bean
	// public CqlSessionBuilderCustomizer
	// sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
	// Path bundle = astraProperties.getSecureConnectBundle().toPath();
	// return builder ->
	// builder.withCloudSecureConnectBundle(bundle).withAuthCredentials("clientId","clientSecret")

	// .withKeyspace("main")

	// .build();
	// }

	// public void
	// CassandraDataAutoConfiguration(com.datastax.oss.driver.api.core.CqlSession
	// session){}

	// @RequestMapping(value = "/user")
	// public Principal user(Principal principal) {
	// return principal;
	// }



	// @RequestMapping("/user")
	// public String user(@AuthenticationPrincipal OAuth2User principal) {
	// 	System.out.println(principal);
	// 	return principal.getAttribute("name");
	// }
}