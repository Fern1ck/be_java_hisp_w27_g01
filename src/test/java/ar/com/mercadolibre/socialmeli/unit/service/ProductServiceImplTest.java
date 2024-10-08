package ar.com.mercadolibre.socialmeli.unit.service;


import ar.com.mercadolibre.socialmeli.dto.response.FollowersListResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostDetailsResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.ProductResponseDTO;
import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.Product;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.repository.impl.RepositoryImpl;
import ar.com.mercadolibre.socialmeli.service.impl.ProductServiceImpl;
import ar.com.mercadolibre.socialmeli.util.TestUtils;
import ar.com.mercadolibre.socialmeli.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    RepositoryImpl repository;

    @InjectMocks
    ProductServiceImpl productService;

    private List<User> users;

    @BeforeEach
    void setUp() {
        users = TestUtils.createUsersWithPosts();
    }

    @DisplayName("T-0005 - Success")
    @Test
    void testGetRecentPostFromFollowedUsers_Success() {

        //Arrange
        PostDetailsResponseDTO postDetails = new PostDetailsResponseDTO(1,2, LocalDate.of(2024, 9, 27),
                new ProductResponseDTO(3, "Monitor 4K", "Monitor", "Samsung", "Negro", "Ultra HD"),300,
                30000.0
        );

        LocalDate dateExpected = LocalDate.of(2024, 9, 27);

        when(repository.existId(3)).thenReturn(true);
        when(repository.getUserById(3)).thenReturn(users.get(2));
        when(repository.getUsers()).thenReturn(users);

        //Act
        FollowersListResponseDTO response = productService.getRecentPostFromFollowedUsers(3, "date_asc");

        System.out.println(response);
        //Assert
        assertEquals(2, response.getPosts().size());
        assertTrue(response.getPosts().contains(postDetails));
        assertEquals(dateExpected, response.getPosts().getFirst().getDate());
        verify(repository, times(1)).existId(3);
        verify(repository, times(1)).getUserById(3);
        verify(repository, times(1)).getUsers();
    }

    @DisplayName("T-0005 - User not found")
    @Test
    void testGetRecentPostFromFollowedUsers_UserNotFound() {
        //Arrange
        Integer id = 999;
        when(repository.existId(id)).thenReturn(false);

        //Act
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            productService.getRecentPostFromFollowedUsers(999, null);
        });

        //Assert
        assertEquals("User ID: " + id + " doesn´t exist.", exception.getMessage());
        verify(repository, times(1)).existId(999);
        verify(repository, never()).getUserById(anyInt());
        verify(repository, never()).getUsers();
    }

    @DisplayName("T-0005 - No followed users")
    @Test
    void testGetRecentPostFromFollowedUsers_NoFollowedUsers() {
        //Arrange
        users.getFirst().setFollowedIds(Collections.emptyList());

        when(repository.existId(1)).thenReturn(true);
        when(repository.getUserById(1)).thenReturn(users.getFirst());

        //Act
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            productService.getRecentPostFromFollowedUsers(1, "date_asc");
        });

        //Assert
        assertEquals("User ID: 1 is not following anyone.", exception.getMessage());
        verify(repository, times(1)).existId(1);
        verify(repository, times(1)).getUserById(1);
        verify(repository, never()).getUsers();
    }

    @DisplayName("T-0005 - Invalid Order")
    @Test
    void testGetRecentPostFromFollowedUsers_InvalidOrderParameter() {
        //Arrange
        when(repository.existId(3)).thenReturn(true);
        when(repository.getUserById(3)).thenReturn(users.get(2));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            productService.getRecentPostFromFollowedUsers(3, "invalid_order");
        });

        assertEquals("Invalid order parameter: invalid_order", exception.getMessage());
        verify(repository, times(1)).existId(3);
        verify(repository, times(1)).getUserById(3);
        verify(repository, never()).getUsers();
    }

    @DisplayName("T-0005 - No recent post")
    @Test
    void testGetRecentPostFromFollowedUsers_NoRecentPost(){
        //Arrange
        when(repository.existId(2)).thenReturn(true);
        when(repository.getUserById(2)).thenReturn(users.get(1));
        when(repository.getUsers()).thenReturn(users);

        //Act
        BadRequestException exception = assertThrows(BadRequestException.class, ()-> {
            productService.getRecentPostFromFollowedUsers(2, null);
        });

        //Assert
        assertEquals("There aren't posts of minus two weeks.", exception.getMessage());
        verify(repository, times(1)).existId(2);
        verify(repository, times(1)).getUserById(2);
        verify(repository, times(1)).getUsers();

    }
}
