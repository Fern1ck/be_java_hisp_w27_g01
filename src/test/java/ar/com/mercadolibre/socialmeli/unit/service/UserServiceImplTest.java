package ar.com.mercadolibre.socialmeli.unit.service;

import ar.com.mercadolibre.socialmeli.dto.response.UserOkResponseDTO;
import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.Product;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.repository.impl.RepositoryImpl;
import ar.com.mercadolibre.socialmeli.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    RepositoryImpl repository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("T-0002 - Exist")
    public void checkUserExistsBeforeUnfollow() {
        // Arrange
        List<Integer> followed = new ArrayList<>(); //para que sea dinamico
        followed.add(5);
        User user = new User(2, "Maria", followed, null);
        Integer userToUnfollow= 5;

        String expectedResponse = "OK";

        // Act
        Mockito.when(repository.existId(user.getUserId())).thenReturn(true);
        Mockito.when(repository.existId(userToUnfollow)).thenReturn(true);
        Mockito.when(repository.getUserById(user.getUserId())).thenReturn(user); //necesario, tira error que user es null

        UserOkResponseDTO responseDTO = userService.unfollowASpecificUserById(user.getUserId(), userToUnfollow);

        verify(repository, atLeastOnce()).existId(user.getUserId());
        verify(repository, atLeastOnce()).existId(userToUnfollow);
        verify(repository, atLeastOnce()).getUserById(user.getUserId());

        // Assert
        Assertions.assertEquals(expectedResponse, responseDTO.getResponse());
    }

    @Test
    @DisplayName("T-0002 - NotExist")
    public void checkUserBeforeUnfollowNotExist() {
        // Arrange
        int userId = 2;
        int userToUnfollow = 10; // ID del usuario que no existe para hacer unfollow

        //Act
        Mockito.when(repository.existId(userId)).thenReturn(true);
        Mockito.when(repository.existId(userToUnfollow)).thenReturn(false);

        BadRequestException thrown = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.unfollowASpecificUserById(userId, userToUnfollow));

        //Assert
        Assertions.assertEquals("Invalid User to Unfollow ID: " + userToUnfollow, thrown.getMessage());
    }

    



}

