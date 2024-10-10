package ar.com.mercadolibre.socialmeli.unit.service;

import ar.com.mercadolibre.socialmeli.dto.response.UserOkResponseDTO;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.repository.impl.RepositoryImpl;
import ar.com.mercadolibre.socialmeli.service.impl.UserServiceImpl;
import ar.com.mercadolibre.socialmeli.util.TestUtils;
import org.junit.jupiter.api.*;
import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerListResponseDTO;
import ar.com.mercadolibre.socialmeli.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ar.com.mercadolibre.socialmeli.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ar.com.mercadolibre.socialmeli.util.UtilTest.createUsersWithPosts;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static ar.com.mercadolibre.socialmeli.util.TestUtils.createUserWithFollowed;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    RepositoryImpl repository;

    @InjectMocks
    UserServiceImpl userService;

    private User user1;
  
    private List<User> users;

    @BeforeEach
    void setUp() {
        user1 = createUserWithFollowed();
        users = createUsersWithPosts();
     
    }

    @Test
    @DisplayName("T-0002 - Exist")
    public void checkUserExistsBeforeUnfollow() {

        // Arrange
        Integer userToUnfollow= 2;
        String expectedResponse = "OK";

        // Act
        Mockito.when(repository.existId(user1.getUserId())).thenReturn(true);
        Mockito.when(repository.existId(userToUnfollow)).thenReturn(true);
        Mockito.when(repository.getUserById(user1.getUserId())).thenReturn(user1);

        UserOkResponseDTO responseDTO = userService.unfollowASpecificUserById(user1.getUserId(), userToUnfollow);

        // Assert
        Assertions.assertEquals(expectedResponse, responseDTO.getResponse());
        verify(repository, atLeastOnce()).existId(user1.getUserId());
        verify(repository, atLeastOnce()).existId(userToUnfollow);
        verify(repository, atLeastOnce()).getUserById(user1.getUserId());
    }

    @Test
    @DisplayName("T-0002 - NotExist")
    public void checkUserBeforeUnfollowNotExist() {

        // Arrange
        int userToUnfollow = 10;

        //Act
        Mockito.when(repository.existId(user1.getUserId())).thenReturn(true);
        Mockito.when(repository.existId(userToUnfollow)).thenReturn(false);


        BadRequestException thrown = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.unfollowASpecificUserById(user1.getUserId(), userToUnfollow));

        //Assert
        Assertions.assertEquals("Invalid User to Unfollow ID: " + userToUnfollow, thrown.getMessage());
        verify(repository, atLeastOnce()).existId(user1.getUserId());
        verify(repository, atLeastOnce()).existId(userToUnfollow);
    }

    @Test
    @DisplayName("TB-0002 - userId does not follow seller")
    public void checkUserContainsUnfollowTB() {

        // Arrange
        Integer userToUnfollow = 3;

        // Mockea user1 para que puedas controlar su comportamiento



        // Mock del repository y el servicio
        Mockito.when(repository.existId(user1.getUserId())).thenReturn(true);
        Mockito.when(repository.existId(userToUnfollow)).thenReturn(true);  // Simula que user1 no sigue a userToUnfollow
        Mockito.when(repository.getUserById(user1.getUserId())).thenReturn(user1);
        Mockito.when(user1.getFollowedIds().contains(userToUnfollow)).thenReturn(false);

        // Act
        BadRequestException thrown = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.unfollowASpecificUserById(user1.getUserId(), userToUnfollow) // Asegúrate de que user1Mock no sea null
        );

        // Assert
        Assertions.assertEquals("User ID: " + user1.getUserId() + " does not follow User ID: " + userToUnfollow, thrown.getMessage());

        // Verificaciones
        verify(repository, atLeastOnce()).existId(user1.getUserId());
        verify(repository, atLeastOnce()).existId(userToUnfollow);
    }



    @Test
    @DisplayName("US-0003 - Happy Path No Ordering")
    public void getFollowerListHappyTest(){
        //arrange
        //User 3 follows 2
        User user = users.stream().filter(u -> u.getUserId().equals(3)).findFirst().orElseThrow(() -> new RuntimeException("Test user not found"));
        when(repository.existId(user.getUserId())).thenReturn(true);
        when(repository.getUserById(user.getUserId())).thenReturn(user);
        when(repository.getUsers()).thenReturn(users);

        //act
        UserFollowerListResponseDTO response = userService.getFollowerList(user.getUserId(), null);

        //assert
        verify(repository, atLeastOnce()).existId(user.getUserId());
        verify(repository, atLeastOnce()).getUserById(user.getUserId());
        verify(repository, atLeastOnce()).getUsers();
        assertEquals(response.getUserId(), user.getUserId());
        assertFalse(response.getFollowers().isEmpty());
        assertEquals(1, response.getFollowers().size());
        assertEquals(2, response.getFollowers().stream().findFirst().get().getUserId());
    }

    @Test
    @DisplayName("US-0003 - User ID is null")
    public void getFollowerListSadPath1Test(){
        //arrange
        Integer userId = null;

        //act
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> userService.getFollowerList(userId, null));

        //assert
        assertEquals(thrown.getMessage(), "User ID: " + userId + " is invalid.");
    }

    @Test
    @DisplayName("US0003 - User ID is negative")
    public void getFollowerListSadPath2Test(){
        //arrange
        Integer userId = -1;

        //act
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> userService.getFollowerList(userId, null));

        //assert
        assertEquals(thrown.getMessage(), "User ID: " + userId + " is invalid.");
    }

    @Test
    @DisplayName("US-0003 - User ID doesn't exist")
    public void getFollowerListSadPath3Test(){
        //arrange
        Integer userId = 50;
        when(repository.existId(userId)).thenReturn(false);

        //act
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> userService.getFollowerList(userId, null));

        //assert
        verify(repository, atLeastOnce()).existId(userId);
        assertEquals(thrown.getMessage(), "User ID: " + userId + " doesn't exist.");
    }
}

