package ar.com.mercadolibre.socialmeli.unit.controller;

import ar.com.mercadolibre.socialmeli.controller.UserController;
import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerListResponseDTO;
import ar.com.mercadolibre.socialmeli.service.impl.UserServiceImpl;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserServiceImpl userService;

    @InjectMocks
    UserController userController;

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
