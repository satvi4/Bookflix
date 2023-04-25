package com.example.bookflix.home;

import java.util.List;
// import java.util.logging.LogManager;
// import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.bookflix.BookflixApplication;
import com.example.bookflix.user.BooksByUser;
import com.example.bookflix.user.BooksByUserRepository;
import com.example.bookflix.userbooks.UserBooksRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// import ch.qos.logback.classic.Logger;

@Controller
public class HomeController {

    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired
    BooksByUserRepository booksByUserRepository;

    private static final Logger logger = LogManager.getLogger(HomeController.class);

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || principal.getAttribute("login") == null) {
            logger.info("No user logged in");
            System.out.println(principal);
            System.out.println(model);
            return "index";
        }

        String userId = principal.getAttribute("login");
        logger.info("User logged in with user id- " + userId);
        Slice<BooksByUser> booksSlice = booksByUserRepository.findAllById(userId, CassandraPageRequest.of(0, 100));
        List<BooksByUser> booksByUser = booksSlice.getContent();
        booksByUser = booksByUser.stream().distinct().map(book -> {
            String coverImageUrl = "/images/no-image.png";
            if (book.getCoverIds() != null & book.getCoverIds().size() > 0) {
                coverImageUrl = COVER_IMAGE_ROOT + book.getCoverIds().get(0) + "-M.jpg";
            }
            book.setCoverUrl(coverImageUrl);
            logger.info("Book fetched...");
            return book;
        }).collect(Collectors.toList());
        model.addAttribute("books", booksByUser);
        return "home";

    }

}
