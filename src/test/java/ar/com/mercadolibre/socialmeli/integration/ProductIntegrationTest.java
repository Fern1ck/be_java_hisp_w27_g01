package ar.com.mercadolibre.socialmeli.integration;

import ar.com.mercadolibre.socialmeli.dto.response.*;
import ar.com.mercadolibre.socialmeli.dto.request.CreatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.request.ProductRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.response.CreatePromoResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.FollowersListResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostDetailsResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostOkResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.exception.ValidationResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.request.ActivatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.ArrayList;
import java.util.Comparator;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    @DisplayName("INTEGRATION - US - 06 - happyPath")
    public void getRecentPostFromFollowedUsers() throws Exception {
        //arrange
        Integer userId = 2;

        ProductResponseDTO productDto = new ProductResponseDTO();
        productDto.setType("Periférico");
        productDto.setBrand("Logitech");
        productDto.setColor("Negro");
        productDto.setNotes("RGB");
        productDto.setProductId(2);
        productDto.setProductName("Teclado mecánico");

        PostDetailsResponseDTO postDto = new PostDetailsResponseDTO();
        postDto.setUserId(4);
        postDto.setPostId(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        postDto.setDate(LocalDate.parse("29-09-2024", formatter));
        postDto.setProduct(productDto);
        postDto.setCategory(200);
        postDto.setPrice(5000.0);

        List<PostDetailsResponseDTO> posts = new ArrayList<>();
        posts.add(postDto);


        FollowersListResponseDTO expectedDto = new FollowersListResponseDTO(2, posts);

        //act
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/products/followed/{userId}/list", userId)
                        .accept(MediaType.APPLICATION_JSON_UTF8))  // Especificar UTF-8
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))  // Verificar el contentType UTF-8
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        FollowersListResponseDTO actualDto = OBJECT_MAPPER.readValue(jsonResponse, FollowersListResponseDTO.class);

        //assert
        Assertions.assertEquals(expectedDto, actualDto);
    }


    @Test
    @DisplayName("INTEGRATION - US - 06 - sadPath - There aren't posts of minus two weeks.")
    public void getRecentPostFromFollowedUsersS1() throws Exception {
        //arrange
        Integer userId = 1;
        String expectedJson = "{\"message\":\"There aren't posts of minus two weeks.\"}";

        //act
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/products/followed/{userId}/list", userId)
                        .accept(MediaType.APPLICATION_JSON_UTF8))  // Especificar UTF-8
                .andDo(print())
                .andExpect(status().isBadRequest())  // Verifica que es 400 BadRequest
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))  // Verifica el contentType UTF-8
                .andReturn();


        String jsonResponse = mvcResult.getResponse().getContentAsString();

        //assert
        Assertions.assertEquals(expectedJson, jsonResponse);
    }




    @Test
    @DisplayName("INTEGRATION - US - 09 - Get Recent Post From Followed Users Order Ascendent")
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
    @DisplayName("INTEGRATION - US - 09 - Get Recent Post From Followed Users - Order Descent")
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

    @DisplayName("INTEGRATION - US - 010 - Success")
    @Test
    void integrationTestCreatePromoPostSuccess() throws Exception {
        // Arrange
        var productdto = new ProductRequestDTO();
        productdto.setProductId(1);
        productdto.setProductName("Silla Gamer");
        productdto.setType("Gamer");
        productdto.setBrand("Racer");
        productdto.setColor("Red");
        productdto.setNotes("Special Edition");

        var requestDto = new CreatePromoRequestDTO();
        requestDto.setProduct(productdto);
        requestDto.setDate(LocalDate.now());
        requestDto.setUserId(1);
        requestDto.setPrice(1500.5);
        requestDto.setHasPromo(true);
        requestDto.setCategory(100);
        requestDto.setDiscount(0.4);

        String requestJson = OBJECT_MAPPER.writeValueAsString(requestDto);

        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/products/promo-post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // Deserialize
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CreatePromoResponseDTO responseDto = OBJECT_MAPPER.readValue(jsonResponse, new TypeReference<>() {});

        // Assert
        assertEquals(1, responseDto.getCreatedId());
    }

    @Test
    @DisplayName("INTEGRATION - US - 16 -  Delete Post")
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

    @DisplayName("INTEGRATION - US - 015 - User Not Found")
    @Test
    void integrationTestActivatePromoUserNotFound() throws Exception {
        // Arrange
        ActivatePromoRequestDTO requestDTO = new ActivatePromoRequestDTO(999, 1, 0.20);
        String requestJson = OBJECT_MAPPER.writeValueAsString(requestDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/products/posts/activate-promo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User ID: 999 doesn´t exist."));
    }

    @DisplayName("INTEGRATION - US - 015 - Post Not Found")
    @Test
    void integrationTestActivatePromoPostNotFound() throws Exception {
        // Arrange
        ActivatePromoRequestDTO requestDTO = new ActivatePromoRequestDTO(1, 999, 0.2);
        String requestJson = OBJECT_MAPPER.writeValueAsString(requestDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/products/posts/activate-promo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Post ID: 999 doesn´t exist."));
    }

    @DisplayName("INTEGRATION - US - 015 - All Values Negative")
    @Test
    void integrationTestActivatePromoAllValuesNegative() throws Exception {
        // Arrange
        ActivatePromoRequestDTO requestDTO = new ActivatePromoRequestDTO(-1, -999, -0.2);
        String requestJson = OBJECT_MAPPER.writeValueAsString(requestDTO);

        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/products/posts/activate-promo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        // Deserialize
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<ValidationResponseDTO> validationErrors = OBJECT_MAPPER.readValue(jsonResponse, new TypeReference<List<ValidationResponseDTO>>() {});

        // Assert
        assertNotNull(validationErrors);
        assertEquals(3, validationErrors.size());

        assertTrue(validationErrors.stream().anyMatch(error ->
                "El id debe ser mayor a cero".equals(error.getMessage())
        ));
        assertTrue(validationErrors.stream().anyMatch(error ->
                "El id debe ser mayor a cero.".equals(error.getMessage())
        ));
        assertTrue(validationErrors.stream().anyMatch(error ->
                "El descuento debe ser mayor a 0.".equals(error.getMessage())
        ));
    }

    @DisplayName("INTEGRATION - US - 015 - All Values Null")
    @Test
    void integrationTestActivatePromoAllValuesNull() throws Exception {
        // Arrange
        ActivatePromoRequestDTO requestDTO = new ActivatePromoRequestDTO(null, null, null);
        String requestJson = OBJECT_MAPPER.writeValueAsString(requestDTO);

        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/products/posts/activate-promo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        // Deserialize
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<ValidationResponseDTO> validationErrors = OBJECT_MAPPER.readValue(jsonResponse, new TypeReference<List<ValidationResponseDTO>>() {});

        // Assert
        assertNotNull(validationErrors);
        assertEquals(3, validationErrors.size());


        assertTrue(validationErrors.stream().anyMatch(error ->
                "El descuento no puede estar vacio.".equals(error.getMessage())
        ));
        assertTrue(validationErrors.stream().anyMatch(error ->
                "La id no puede estar vacÃ­a.".equals(error.getMessage())
        ));
        assertTrue(validationErrors.stream().anyMatch(error ->
                "El id no puede estar vacÃ\u00ADo.".equals(error.getMessage())
        ));
    }


    @Test
    @DisplayName("INTEGRATION - US - 13 - Should search by query")
    @Order(1)
    public void testSearchPostByBrandAndName1Output() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/products/search")
                                .param("query", "me")
                ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].product.product_name").value("Silla gamer"))
                .andExpect(jsonPath("$[1].product.product_name").value("Teclado mecánico"));
    }

    @Test
    @DisplayName("INTEGRATION - US - 13 - Should search by query and user_id")
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
    @DisplayName("INTEGRATION - US - 13 - Should not find anything")
    public void testSearchPostByBrandAndName3Output() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/products/search")
                                .param("query", "kflehgjiorhgrogjprkgperjg")
                ).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("INTEGRATION - US - 17 - Happy Path - Get promo posts history")
    public void getPromoPostHistory() throws Exception {
        Integer userId = 2;

        this.mockMvc.perform(MockMvcRequestBuilders.get("/products/promo-post/{userId}/history", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.posts.length()").value(2))
                .andExpect(jsonPath("$.posts[0].post_id").value(1))
                .andExpect(jsonPath("$.posts[1].post_id").value(2))
                .andExpect(jsonPath("$.posts[0].product.product_name").value("Silla gamer"))
                .andExpect(jsonPath("$.posts[1].product.product_name").value("Monitor 4K"))
                .andExpect(jsonPath("$.posts[0].has_promo").value(false))
                .andExpect(jsonPath("$.posts[1].has_promo").value(true));

    }

    @Test
    @DisplayName("INTEGRATION - US - 17 - Sad Path - no have posts")
    public void getPromoPostHistoryS1() throws Exception {
        //arrange
        Integer userId = 6;
        String expectedJson = "{\"message\":\"User ID: 6 doesn't have posts.\"}";

        //act
        MvcResult response= this.mockMvc.perform(MockMvcRequestBuilders.get("/products/promo-post/{userId}/history", userId))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();

        //assert
        Assertions.assertEquals(expectedJson, jsonResponse);
    }


    @Test
    @DisplayName("INTEGRATION - US - 17 - Sad Path - User ID doesn't exist. ")
    public void getPromoPostHistoryS2() throws Exception {
        //arrange
        Integer userId = 999;
        String expectedJson = "{\"message\":\"User ID: 999 doesn't exist.\"}";

        //act
        MvcResult response= this.mockMvc.perform(MockMvcRequestBuilders.get("/products/promo-post/{userId}/history", userId))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();

        //assert
        Assertions.assertEquals(expectedJson, jsonResponse);
    }

}
