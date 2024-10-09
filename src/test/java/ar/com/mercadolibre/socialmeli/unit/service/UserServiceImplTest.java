package ar.com.mercadolibre.socialmeli.unit.service;

import ar.com.mercadolibre.socialmeli.dto.response.UserOkResponseDTO;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.repository.impl.RepositoryImpl;
import ar.com.mercadolibre.socialmeli.service.impl.UserServiceImpl;
import ar.com.mercadolibre.socialmeli.util.TestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @BeforeEach
    void setUp() {
        user1 = createUserWithFollowed();
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





}

