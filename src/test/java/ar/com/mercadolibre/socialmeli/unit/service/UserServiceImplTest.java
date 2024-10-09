package ar.com.mercadolibre.socialmeli.unit.service;

import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerListResponseDTO;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.exception.NotFoundException;
import ar.com.mercadolibre.socialmeli.repository.impl.RepositoryImpl;
import ar.com.mercadolibre.socialmeli.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ar.com.mercadolibre.socialmeli.utils.Utils;
import java.util.ArrayList;
import java.util.List;

import static ar.com.mercadolibre.socialmeli.util.UtilTest.createUsersWithPosts;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    RepositoryImpl repository;

    @InjectMocks
    UserServiceImpl userService;

    private List<User> users;

    @BeforeEach
    void setUp() {
        users = createUsersWithPosts();
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
