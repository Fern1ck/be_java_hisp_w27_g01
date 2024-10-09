package ar.com.mercadolibre.socialmeli.integration;

import ar.com.mercadolibre.socialmeli.dto.response.FollowersListResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostDetailsResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostOkResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    private static ObjectMapper OBJECT_MAPPER;

    @BeforeEach
    public void setUp() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        OBJECT_MAPPER  = new ObjectMapper();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));

        OBJECT_MAPPER.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("US-09: Get Recent Post From Followed Users - Order Ascendent")
    public void getRecentPostFromFollowedUsersDateAsc() throws Exception{

        Integer userId = 2;
        String order = "date_asc";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/products/followed/{userId}/list", userId).param("order", order)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String jsonResponse =  mvcResult.getResponse().getContentAsString();
        FollowersListResponseDTO followersListResponseDTO = OBJECT_MAPPER.readValue(jsonResponse, FollowersListResponseDTO.class);

        Assertions.assertEquals(followersListResponseDTO.getPosts(), followersListResponseDTO.getPosts().stream().sorted(Comparator.comparing(PostDetailsResponseDTO::getDate)).toList());
    }

    @Test
    @DisplayName("US-09: Get Recent Post From Followed Users - Order Descendet")
    public void getRecentPostFromFollowedUsersDateDesc() throws Exception{

        Integer userId = 2;
        String order = "date_desc";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/products/followed/{userId}/list", userId).param("order", order)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String jsonResponse =  mvcResult.getResponse().getContentAsString();
        FollowersListResponseDTO followersListResponseDTO = OBJECT_MAPPER.readValue(jsonResponse, FollowersListResponseDTO.class);

        Assertions.assertEquals(followersListResponseDTO.getPosts(), followersListResponseDTO.getPosts().stream().sorted(Comparator.comparing(PostDetailsResponseDTO::getDate)).toList());
    }

    @Test
    @DisplayName("US-16: Delete Post")
    public void deletePost() throws Exception{

        Integer userId = 2;
        Integer postId = 2;
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete("/products/post/{userId}/{postId}", userId, postId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String jsonResponse =  mvcResult.getResponse().getContentAsString();
        PostOkResponseDTO postOkResponseDTO = OBJECT_MAPPER.readValue(jsonResponse, PostOkResponseDTO.class);

        Assertions.assertEquals(postOkResponseDTO.getResponse(), "OK");
    }

}
