package com.mycompany.app.controller;

import com.mycompany.app.dao.DataRepository;
import com.mycompany.app.domain.ShortenUrl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = {URLShortenerController.class})
public class URLShortenerControllerTest {

    // Used to simulate HTTP requests
    @Autowired
    private MockMvc mockMvc;

//    // 2. Mocks the dependency (DataRepository) required by the Controller
//    // Spring Boot adds this mock to the application context used for the test
    @Autowired
    private DataRepository dataRepository;

//    private final String BASE_URL = "/api/url";
//    private final String SHORT_CODE = "abcde123";
//    private final String LONG_URL = "https://www.google.com/very/long/path";
//
//    // --- Test for GET /{shortenUrl} (find) ---
//    @Test
//    void find_ShouldReturnShortenUrl_WhenFound() throws Exception {
//        // Arrange
//        ShortenUrl expectedUrl = ShortenUrl.builder()
//                .longUrl(LONG_URL)
//                .shortenUrl(SHORT_CODE)
//                .build();
//
//        // Mock the repository call
//        when(dataRepository.find(eq(SHORT_CODE))).thenReturn(expectedUrl);
//
//        // Act & Assert
//        mockMvc.perform(get(BASE_URL + "/{shortenUrl}", SHORT_CODE)
//                        .contentType(MediaType.APPLICATION_JSON))
//                // Expect HTTP 200 OK
//                .andExpect(status().isOk())
//                // Expect the response body to contain the correct values
//                .andExpect(jsonPath("$.shortenUrl").value(SHORT_CODE))
//                .andExpect(jsonPath("$.longUrl").value(LONG_URL));
//    }
//
//    // --- Test for POST / (shorten) ---
//    @Test
//    void shorten_ShouldReturnNewShortenUrl_WhenSuccessful() throws Exception {
//        // Arrange
//        String newShortCode = "fghij456";
//        ShortenUrl urlToReturn = ShortenUrl.builder()
//                .longUrl(LONG_URL)
//                .shortenUrl(newShortCode) // The repository is responsible for setting this
//                .build();
//
//        // Mock the repository call (save is expected to return the object with the short URL set)
//        when(dataRepository.save(any(ShortenUrl.class))).thenReturn(urlToReturn);
//
//        // Act & Assert
//        mockMvc.perform(post(BASE_URL)
//                        // Use @RequestParam 'longUrl'
//                        .param("longUrl", LONG_URL)
//                        .contentType(MediaType.APPLICATION_JSON))
//                // Expect HTTP 200 OK
//                .andExpect(status().isOk())
//                // Expect the returned object to have the new short code
//                .andExpect(jsonPath("$.longUrl").value(LONG_URL))
//                .andExpect(jsonPath("$.shortenUrl").value(newShortCode));
//    }

    @Test
    void test1() {

    }
}