package ar.com.mercadolibre.socialmeli.integration;

import ar.com.mercadolibre.socialmeli.dto.response.UserFollowedResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserNameResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    private static ObjectMapper OBJECT_MAPPER;

    @BeforeEach
    public void setUp() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        OBJECT_MAPPER = new ObjectMapper();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));

        OBJECT_MAPPER.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("US-04: Find By Followed")
    public void findByFollowed() throws Exception{

        Integer userId = 2;
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/followed/list", userId).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String jsonResponse =  mvcResult.getResponse().getContentAsString();
        UserFollowedResponseDTO[] userFollowedResponseDTOS = OBJECT_MAPPER.readValue(jsonResponse, UserFollowedResponseDTO[].class);

        List<UserFollowedResponseDTO> userFollowed = Arrays.stream(userFollowedResponseDTOS).toList();
        Assertions.assertNotNull(userFollowed);
        Assertions.assertFalse(userFollowed.isEmpty());

        boolean userIdFound = userFollowed.stream()
                .flatMap(userFollowedList -> userFollowedList.getFollowed().stream())
                .anyMatch(followedUser -> followedUser.getUserId() == 4);

        Assertions.assertTrue(userIdFound);
    }

}
