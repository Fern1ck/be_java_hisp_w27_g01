package ar.com.mercadolibre.socialmeli.unit.service;


import ar.com.mercadolibre.socialmeli.dto.response.UserFollowedResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerListResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserNameResponseDTO;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.exception.NotFoundException;
import ar.com.mercadolibre.socialmeli.repository.impl.RepositoryImpl;
import ar.com.mercadolibre.socialmeli.service.impl.UserServiceImpl;
import ar.com.mercadolibre.socialmeli.util.UtilTest;

import ar.com.mercadolibre.socialmeli.dto.response.UserOkResponseDTO;
import ar.com.mercadolibre.socialmeli.util.TestUtils;
import org.junit.jupiter.api.*;

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
import java.util.List;

import static ar.com.mercadolibre.socialmeli.util.UtilTest.createUsersWithPosts;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static ar.com.mercadolibre.socialmeli.util.TestUtils.createUserWithFollowed;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    RepositoryImpl repository;

    @InjectMocks
    UserServiceImpl userService;

    private User user1;
    private List<User> users;
    private List<User> users1;


    @BeforeEach
    void setUp() {
        users = UtilTest.createUsers();
        user1 = createUserWithFollowed();
        users1 = createUsersWithPosts();

    }

    @Test
    @DisplayName("T-0004: Order Ascendent")
    public void orderByDateAscendentHappy() {
        // Arrange
        Integer id = 2;
        String order = "name_asc";

        when(repository.existId(id)).thenReturn(true);
        when(repository.getUserById(id)).thenReturn(users.get(1));
        when(repository.getUsers()).thenReturn(users);

        // Act
        List<UserFollowedResponseDTO> userFollowedResponseDTOS = userService.findByFollowed(id, order);
        UserFollowerListResponseDTO userFollowerListResponseDTO = userService.getFollowerList(id, order);

        List<String> userNamesAsc3 = userFollowedResponseDTOS.getFirst().getFollowed().stream()
                .map(UserNameResponseDTO::getUserName)
                .collect(Collectors.toList());

        List<String> sortedNamesAsc3 = userNamesAsc3.stream().sorted().collect(Collectors.toList());

        List<String> userNamesAsc4 = userFollowerListResponseDTO.getFollowers()
                .stream()
                .map(UserNameResponseDTO::getUserName)
                .collect(Collectors.toList());

        List<String> sortedNamesAsc4 = userNamesAsc4.stream().sorted().collect(Collectors.toList());

        System.out.println("Sorted followed: " + sortedNamesAsc3);
        System.out.println("Sorted followers: " + sortedNamesAsc4);

        // Assert

        assertEquals(sortedNamesAsc3, userNamesAsc3);
        assertEquals(sortedNamesAsc4, userNamesAsc4);
    }

    @Test
    @DisplayName("T-0004: Order Descendent")
    public void orderByDateDescendentHappy(){

        // Arrange
        Integer id = 2;
        String order = "name_desc";

        // Act
        when(repository.existId(id)).thenReturn(true);
        when(repository.getUserById(id)).thenReturn(users.get(1));
        when(repository.getUsers()).thenReturn(users);
        // US-0003
        List<UserFollowedResponseDTO> userFollowedResponseDTOS = userService.findByFollowed(id, order);
        // US-0004
        UserFollowerListResponseDTO userFollowerListResponseDTO = userService.getFollowerList(id, order);

        // Assert
        List<String> userNamesDesc3 = userFollowedResponseDTOS.getFirst().getFollowed().stream()
                .map(UserNameResponseDTO::getUserName)
                .collect(Collectors.toList());

        List<String> sortedNamesDesc3 = userNamesDesc3.stream()
                .sorted()
                .toList().reversed();

        List<String> userNamesDesc4 = userFollowerListResponseDTO.getFollowers()
                .stream()
                .map(UserNameResponseDTO::getUserName)
                .toList();

        List<String> sortedNamesDesc4 = userNamesDesc4.stream()
                .sorted()
                .toList().reversed();

        System.out.println("Sorted followed users: " + sortedNamesDesc3);
        System.out.println("Sorted followers: " + sortedNamesDesc4);

        assertEquals(sortedNamesDesc3, userNamesDesc3);
        assertEquals(sortedNamesDesc4, userNamesDesc4);
    }

    @Test
    @DisplayName("T-0004: User doesn't follow anyone")
    public void noOrderAsc() {

        // Arrange
        Integer id = 2;
        String order = "name_asc";

        User user1 = users.stream()
                .filter(user -> user.getUserId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User not found"));

        user1.setFollowedIds(Collections.emptyList());

        // Act
        when(repository.existId(id)).thenReturn(true);
        when(repository.getUserById(id)).thenReturn(user1);

        // Assert
        Exception exception = assertThrows(BadRequestException.class, () -> {
            userService.findByFollowed(id, order);
        });

        String expectedMessage = "User with the ID: 2 is not following anyone.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("T-0004: User doesn't have followers")
    public void noOrderDesc() {
        // Arrange
        Integer id = 1;
        String order = "name_desc";

        User user2 = users.stream()
                .filter(user -> user.getUserId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Act
        when(repository.existId(id)).thenReturn(true);
        when(repository.getUserById(id)).thenReturn(user2);

        List<User> allUsers = users.stream()
                .peek(user -> {
                    if (user.getFollowedIds() == null || !user.getFollowedIds().contains(id)) {
                        user.setFollowedIds(Collections.emptyList());
                    } else {
                        user.setFollowedIds(Collections.singletonList(id));
                    }
                })
                .collect(Collectors.toList());

        when(repository.getUsers()).thenReturn(allUsers);

        // Assert
        UserFollowerListResponseDTO userFollowerListResponseDTO = userService.getFollowerList(id, order);
        assertTrue(userFollowerListResponseDTO.getFollowers().isEmpty());
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
    @DisplayName("US-0003 - Happy Path No Ordering")
    public void getFollowerListHappyTest(){
        //arrange
        //User 3 follows 2
        User user = users1.stream().filter(u -> u.getUserId().equals(3)).findFirst().orElseThrow(() -> new RuntimeException("Test user not found"));
        when(repository.existId(user.getUserId())).thenReturn(true);
        when(repository.getUserById(user.getUserId())).thenReturn(user);
        when(repository.getUsers()).thenReturn(users1);

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

