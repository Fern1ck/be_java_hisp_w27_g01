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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private List<User> users;

    @BeforeEach
    void setUp() {
        users = UtilTest.createUsers();
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



}
