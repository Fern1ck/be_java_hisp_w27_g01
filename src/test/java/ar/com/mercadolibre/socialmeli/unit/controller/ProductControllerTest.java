package ar.com.mercadolibre.socialmeli.unit.controller;

import ar.com.mercadolibre.socialmeli.controller.ProductController;
import ar.com.mercadolibre.socialmeli.dto.response.FollowersListResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostDetailsResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.ProductResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.SearchResponseDTO;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    ProductServiceImpl productService;

    @InjectMocks
    ProductController productController;

    @DisplayName("T-0005 - Success")
    @Test
    void testGetRecentPostFromFollowedUsers_Success() {
        //Arrange
        ProductResponseDTO product1 = new ProductResponseDTO(3, "Monitor 4K", "Monitor", "Samsung", "Negro", "Ultra HD");
        PostDetailsResponseDTO post1 = new PostDetailsResponseDTO(1, 2, LocalDate.of(2024, 9, 27), product1, 300, 30000.0);
        ProductResponseDTO product2 = new ProductResponseDTO(1, "Silla gamer", "Gamer", "Racer", "Red", "Special Edition");
        PostDetailsResponseDTO post2 = new PostDetailsResponseDTO(1, 1, LocalDate.of(2024, 9, 28), product2, 100, 15000.0);
        FollowersListResponseDTO followersListResponseDTO = new FollowersListResponseDTO(3, Arrays.asList(post1, post2));

        when(productService.getRecentPostFromFollowedUsers(3, null)).thenReturn(followersListResponseDTO);

        // Act
        ResponseEntity<?> response = productController.getRecentPostFromFollowedUsers(3, null);
        FollowersListResponseDTO responseBody = (FollowersListResponseDTO) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(2, responseBody.getPosts().size());
        assertTrue(responseBody.getPosts().contains(post1));
        assertTrue(responseBody.getPosts().contains(post2));
        verify(productService, times(1)).getRecentPostFromFollowedUsers(3, null);
    }

    @DisplayName("T-0005 - ID not found")
    @Test
    void testGetRecentPostFromFollowedUsers_UserNotFound() {
        // Arrange
        Integer id = 999;
        when(productService.getRecentPostFromFollowedUsers(id, null)).thenThrow(new BadRequestException("User ID: " + id + " doesn´t exist."));

        // Act
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            productController.getRecentPostFromFollowedUsers(id, null);
        });

        //Assert
        assertEquals("User ID: " + id + " doesn´t exist.", exception.getMessage());
        verify(productService, times(1)).getRecentPostFromFollowedUsers(id, null);
    }

    @DisplayName("T-0006 - Success date_asc")
    @Test
    void testGetRecentPostFromFollowedUsers_SuccessOrderDateAsc() {
        //Arrange
        ProductResponseDTO product1 = new ProductResponseDTO(3, "Monitor 4K", "Monitor", "Samsung", "Negro", "Ultra HD");
        PostDetailsResponseDTO post1 = new PostDetailsResponseDTO(1, 2, LocalDate.of(2024, 9, 27), product1, 300, 30000.0);
        ProductResponseDTO product2 = new ProductResponseDTO(1, "Silla gamer", "Gamer", "Racer", "Red", "Special Edition");
        PostDetailsResponseDTO post2 = new PostDetailsResponseDTO(1, 1, LocalDate.of(2024, 9, 28), product2, 100, 15000.0);
        FollowersListResponseDTO followersListResponseDTO = new FollowersListResponseDTO(3, Arrays.asList(post1, post2));

        when(productService.getRecentPostFromFollowedUsers(3, "date_asc")).thenReturn(followersListResponseDTO);

        LocalDate expectedFirst = LocalDate.of(2024, 9, 27);
        LocalDate expectedSecond = LocalDate.of(2024, 9, 28);

        // Act
        ResponseEntity<?> response = productController.getRecentPostFromFollowedUsers(3, "date_asc");
        FollowersListResponseDTO responseBody = (FollowersListResponseDTO) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(expectedFirst, ((FollowersListResponseDTO) Objects.requireNonNull(response.getBody())).getPosts().getFirst().getDate());
        assertEquals(expectedSecond, ((FollowersListResponseDTO) Objects.requireNonNull(response.getBody())).getPosts().get(1).getDate());
        assertTrue(responseBody.getPosts().contains(post1));
        assertTrue(responseBody.getPosts().contains(post2));
        verify(productService, times(1)).getRecentPostFromFollowedUsers(3, "date_asc");
    }

    @Test
    @DisplayName("TB - 013 - Success Only query")
    public void searchPostByBrandAndNameOnlyQueryTest(){
        //Arrange
        when(productService.searchPostByBrandAndName("query", null)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<?> response = productController.searchPostByBrandAndName("query", null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService, times(1)).searchPostByBrandAndName("query", null);
    }

    @Test
    @DisplayName("TB - 013 - Success Query and User ID")
    public void searchPostByBrandAndNameQueryAndUserIDTest(){
        //Arrange
        when(productService.searchPostByBrandAndName("query", 2)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<?> response = productController.searchPostByBrandAndName("query", 2);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService, times(1)).searchPostByBrandAndName("query", 2);
    }
}
