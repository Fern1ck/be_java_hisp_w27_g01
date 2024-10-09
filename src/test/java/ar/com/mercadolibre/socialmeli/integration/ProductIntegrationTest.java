package ar.com.mercadolibre.socialmeli.integration;

import ar.com.mercadolibre.socialmeli.dto.request.PostRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.request.ProductRequestDTO;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));

        OBJECT_MAPPER.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("INTEGRATION - US - 05 -  Create Post")
    public void integratioCreatePost() throws Exception {
        //Arrange
        PostRequestDTO postRequestDTO = new PostRequestDTO();
        postRequestDTO.setUserId(1);
        postRequestDTO.setProduct(new ProductRequestDTO());
        postRequestDTO.setCategory(200);
        postRequestDTO.setDate(LocalDate.now());
        postRequestDTO.setPrice(3000.0);
        String jsonString = "{\"response\":\"OK\"}";
        String requestJson = OBJECT_MAPPER.writeValueAsString(postRequestDTO);

        //Act<
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/products/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        //Deserialize
        String jsonResponse =  mvcResult.getResponse().getContentAsString();
        PostOkResponseDTO postOkResponseDTO = OBJECT_MAPPER.readValue(jsonResponse, PostOkResponseDTO.class);
        //Assert
        assertNotNull(jsonResponse);
        Assertions.assertEquals(jsonResponse, jsonString);
    }
}
