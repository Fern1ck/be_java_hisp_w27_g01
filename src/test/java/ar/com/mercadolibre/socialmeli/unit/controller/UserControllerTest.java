package ar.com.mercadolibre.socialmeli.unit.controller;

import ar.com.mercadolibre.socialmeli.controller.ProductController;
import ar.com.mercadolibre.socialmeli.controller.UserController;

import ar.com.mercadolibre.socialmeli.dto.response.FollowersListResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostDetailsResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.ProductResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserOkResponseDTO;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.service.impl.ProductServiceImpl;
import ar.com.mercadolibre.socialmeli.service.impl.UserServiceImpl;
import ar.com.mercadolibre.socialmeli.util.UtilTest;
import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerListResponseDTO;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.service.impl.UserServiceImpl;
import ar.com.mercadolibre.socialmeli.util.TestUtils;

import jakarta.validation.constraints.AssertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.util.Assert;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserServiceImpl userService;

    @InjectMocks
    UserController userController;

    private List<User> users;

    @BeforeEach
    void setUp() {
        users = UtilTest.createUsersWithPosts();
    }

    @Test
    @DisplayName("T-0001 - Success")
    void followASpecificUserByIdTest() {
        //Arrange
        UserOkResponseDTO userOk = userService.followASpecificUserById(1,2);
        when(userService.followASpecificUserById(1, 2)).thenReturn(userOk);

        // Act
        ResponseEntity<?> response = userController.followASpecificUserById(1, 2);
        UserOkResponseDTO responseBody = (UserOkResponseDTO) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("US-003 - Happy Path No ordering")
    public void getFollowerListTest(){
        //arrange
        Integer userId = 1;
        when(userService.getFollowerList(userId, null)).thenReturn(new UserFollowerListResponseDTO());

        //act
        ResponseEntity<?> response = userController.getFollowerList(userId, null);

        //assert
        verify(userService, atLeastOnce()).getFollowerList(userId, null);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }
}
