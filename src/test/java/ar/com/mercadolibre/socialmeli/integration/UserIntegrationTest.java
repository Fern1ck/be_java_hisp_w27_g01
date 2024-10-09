package ar.com.mercadolibre.socialmeli.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    private static ObjectMapper OBJECT_MAPPER;

    @BeforeEach
    public void setUp() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        OBJECT_MAPPER= new ObjectMapper();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));

        OBJECT_MAPPER.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("Integration - US 07 - happyPath")
    public void unfollowASpecificUserByIdTest() throws Exception {
        // Arrange
        Integer userId = 5;
        Integer userIdToUnfollow = 2;
        String jsonResponse = "{\"response\":\"OK\"}";

        // Act
        MvcResult response= mockMvc.perform(post("/users/{userId}/unfollow/{userIdToUnfollow}", userId, userIdToUnfollow)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse))
                .andReturn();

        String jResponse =  response.getResponse().getContentAsString();

        // Assert
        assertNotNull(jResponse);
        assertEquals(jsonResponse, jResponse);

    }


    @Test
    @DisplayName("Integration - US 07 - sadPath - UnfollowIdNotExist")
    public void unfollowASpecificUserByIdTestB1() throws Exception {
        // Arrange
        Integer userId = 2;
        Integer userIdToUnfollow = 2;
        String jsonResponse = "{\"message\":\"Invalid User and User ID to unfollow\"}";

        // Act
        MvcResult response= mockMvc.perform(post("/users/{userId}/unfollow/{userIdToUnfollow}", userId, userIdToUnfollow)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonResponse))
                .andReturn();

        String jResponse =  response.getResponse().getContentAsString();

        // Assert
        assertNotNull(jResponse);
        assertEquals(jsonResponse, jResponse);

    }

    @Test
    @DisplayName("Integration - US 07 - sadPath - userIdNotExist")
    public void unfollowASpecificUserByIdTestB2() throws Exception {
        // Arrange
        Integer userId = 10;
        Integer userIdToUnfollow = 2;
        String jsonResponse = "{\"message\":\"Invalid User ID: 10\"}";

        // Act
        MvcResult response= mockMvc.perform(post("/users/{userId}/unfollow/{userIdToUnfollow}", userId, userIdToUnfollow)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse))
                .andReturn();

        String jResponse =  response.getResponse().getContentAsString();

        // Assert
        assertNotNull(jResponse);
        assertEquals(jsonResponse, jResponse);

    }


}
