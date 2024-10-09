package ar.com.mercadolibre.socialmeli.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

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
    @DisplayName("Integration - US 13 - Should search by query")
    @Order(1)
    public void testSearchPostByBrandAndName1Output() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/products/search")
                                .param("query", "me")
                ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].product.product_name").value("Silla gamer"))
                .andExpect(jsonPath("$[1].product.product_name").value("Teclado mecánico"));
    }

    @Test
    @DisplayName("Integration - US 13 - Should search by query and user_id")
    public void testSearchPostByBrandAndName2Output() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/products/search")
                .param("query", "ams")
                .param("user_id", "2")
                ).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].post_id").value(2))
                .andExpect(jsonPath("$[0].product.product_id").value("3"));
    }

    @Test
    @DisplayName("Integration - US 13 - Should not find anything")
    public void testSearchPostByBrandAndName3Output() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/products/search")
                                .param("query", "kflehgjiorhgrogjprkgperjg")
                ).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
