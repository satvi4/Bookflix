package com.example.bookflix.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.bookflix.entity.Book;
import com.example.bookflix.entity.BooksByUser;
import com.example.bookflix.entity.UserBooks;
import com.example.bookflix.entity.UserBooksPrimaryKey;
import com.example.bookflix.repository.BookRepository;
import com.example.bookflix.repository.BooksByUserRepository;
import com.example.bookflix.repository.UserBooksRepository;

@Controller
public class UserBooksController {

    @Autowired
    UserBooksRepository userBooksRepository;

    @Autowired
    BooksByUserRepository booksByUserRepository;

    @Autowired
    BookRepository bookRepository;

    private static final Logger logger = LogManager.getLogger(UserBooksController.class);

    @PostMapping("/addUserBook")
    public ModelAndView addBookForUser(
            @RequestBody MultiValueMap<String, String> formData,
            @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null || principal.getAttribute("login") == null) {
            logger.info("User not logged in...");
            return null;
        }

        String bookId = formData.getFirst("bookId");
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            logger.info("Book Data not available for book id ="+bookId);
            return new ModelAndView("redirect:/");
        }
        Book book = optionalBook.get();

        UserBooks userBooks = new UserBooks();
        UserBooksPrimaryKey key = new UserBooksPrimaryKey();
        String userId = principal.getAttribute("login");
        key.setUserId(userId);
        key.setBookId(bookId);

        userBooks.setKey(key);

        int rating = Integer.parseInt(formData.getFirst("rating"));

        userBooks.setStartedDate(LocalDate.parse(formData.getFirst("startDate")));
        userBooks.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
        userBooks.setRating(rating);
        userBooks.setReadingStatus(formData.getFirst("readingStatus"));

        userBooksRepository.save(userBooks);

        BooksByUser booksByUser = new BooksByUser();
        booksByUser.setId(userId);
        booksByUser.setBookId(bookId);
        booksByUser.setBookName(book.getName());
        booksByUser.setCoverIds(book.getCoverIds());
        booksByUser.setAuthorNames(book.getAuthorNames());
        booksByUser.setReadingStatus(formData.getFirst("readingStatus"));
        booksByUser.setRating(rating);
        booksByUserRepository.save(booksByUser);
        logger.info("Book status updated by the user");
        return new ModelAndView("redirect:/");

    }
}