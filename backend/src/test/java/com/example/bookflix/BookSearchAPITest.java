package com.example.bookflix;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.bookflix.search.SearchController;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class BookSearchAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchController searchController;

    @Test
    public void searchBooks_ReturnsResults() throws Exception {
        // Mock the response from the Open Library search API
        String mockResponse = "{\"docs\": [ { \"title\": \"Book 1\" }, { \"title\": \"Book 2\" } ] }";
        when(searchController.getSearchResults(anyString(), null)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/search")
                .param("query", "java")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mockResponse));
    }

    @Test
    public void searchBooks_WithEmptyQuery_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/search")
                .param("query", "")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
