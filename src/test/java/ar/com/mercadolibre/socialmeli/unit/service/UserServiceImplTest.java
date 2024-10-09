package ar.com.mercadolibre.socialmeli.unit.service;

import ar.com.mercadolibre.socialmeli.dto.response.UserOkResponseDTO;
import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.Product;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.exception.NotFoundException;
import ar.com.mercadolibre.socialmeli.repository.impl.RepositoryImpl;
import ar.com.mercadolibre.socialmeli.service.impl.UserServiceImpl;
import ar.com.mercadolibre.socialmeli.util.UtilTest;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
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
        users = UtilTest.createUsersWithPosts();
    }

    @Test
    @Description("US-0001: Poder realizar la acción de “Follow” (seguir) a un determinado vendedor")
    public void followASpecificUserByIdTest() {
        User user = users.stream().filter(u -> u.getUserId().equals(1)).findFirst().orElseThrow(() -> new RuntimeException("No se encontro el usuario"));
        User follow = users.stream().filter(u -> u.getUserId().equals(2)).findFirst().orElseThrow(() -> new RuntimeException("No se encontro el usuario"));

        when(repository.existId(2)).thenReturn(true);
        when(repository.getUserById(1)).thenReturn(user);
        when(repository.getUserById(2)).thenReturn(follow);
        when(repository.updateUser(user)).thenReturn(true);

        UserOkResponseDTO dto = userService.followASpecificUserById(1,2);

        verify(repository).updateUser(user);
        verify(repository).existId(2);
        verify(repository).getUserById(1);
        verify(repository).getUserById(2);
        assertEquals(dto.getResponse(),"OK");
    }

    @Test
    @Description("US-0001: un usuario quiere seguir a alguien que ya sigue.")
    public void userWantsToFollowSomeoneTheyAlreadyFollow() {

    }

    @Test
    @Description("US-0001: Poder realizar la acción de “Follow” (seguir) a un determinado vendedor que no existe")
    public void followNotExistASpecificUserByIdTest() {
        when(repository.existId(6)).thenReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.followASpecificUserById(1,6));

        verify(repository).existId(6);
        assertEquals(exception.getMessage(),"User to follow ID: " + 6 + " doesn't exist.");
    }

    @Test
    @Description("US-0001: Usuario quiere seguir a un usuario que no es vendedor.")
    public void userFollowToNotSellerTest() {
        User user1 = new User(1, "Fernando Baldrich");

        when(repository.existId(1)).thenReturn(true);
        when(repository.getUserById(1)).thenReturn(user1);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.followASpecificUserById(2,1));

        verify(repository).existId(1);
        verify(repository).getUserById(1);
        assertEquals(exception.getMessage(),"User to follow is not a seller");
    }

    @Test
    @Description("US-0001: Usuario quiere seguir a un usuario que no es vendedor.")
    public void userNotExistFollowTest() {

        Product product1 = new Product(1, "Silla gamer", "Gamer",  "Racer", "Red", "Special Edition");
        Post post1 = new Post(1, product1, LocalDate.of(2024, 9, 28), 100, 15000.00, false, 0.0 );

        Product product2 = new Product(3, "Monitor 4K", "Monitor", "Samsung", "Negro", "Ultra HD");
        Post post2 = new Post(2, product2, LocalDate.of(2024, 9, 27), 300, 30000.00, true, 0.3);

        User user1 = new User();
        user1.setUserId(1);
        user1.setPosts(Arrays.asList(post1, post2));

        when(repository.existId(1)).thenReturn(true);
        when(repository.getUserById(1)).thenReturn(user1);
        when(repository.getUserById(6)).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.followASpecificUserById(6,1));

        verify(repository).existId(1);
        verify(repository).getUserById(1);
        verify(repository).getUserById(6);
        assertEquals(exception.getMessage(),"User ID: 6 doesn't exist.");
    }

    @Test
    @Description("US-0001: No se actualiza la lista de seguidores.")
    public void sizeListFollowTest() {
        Product product1 = new Product(1, "Silla gamer", "Gamer", "Racer", "Red", "Special Edition");
        Post post1 = new Post(1, product1, LocalDate.of(2024, 9, 28), 100, 15000.00, false, 0.0);

        Product product2 = new Product(3, "Monitor 4K", "Monitor", "Samsung", "Negro", "Ultra HD");
        Post post2 = new Post(2, product2, LocalDate.of(2024, 9, 27), 300, 30000.00, true, 0.3);

        User user1 = new User();
        user1.setUserId(1);
        user1.setPosts(Arrays.asList(post1, post2));

        User user2 = new User();
        user2.setUserId(2);
        user2.setFollowedIds(new ArrayList<>());

        when(repository.existId(1)).thenReturn(true);
        when(repository.getUserById(1)).thenReturn(user1);
        when(repository.getUserById(2)).thenReturn(user2);
        when(repository.updateUser(user2)).thenReturn(false);


        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.followASpecificUserById(2, 1);
        });

        verify(repository).existId(1);
        verify(repository).getUserById(1);
        verify(repository).getUserById(2);
        verify(repository).updateUser(user2);

        assertEquals("Ocurio un error al actualizar el User ID: 2", exception.getMessage());
    }

    @Test
    @Description("US-0001: un usuario no puede seguirse a si mismo.")
    public void userCannotFollowHimselfTest() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.followASpecificUserById(1,1));

        assertEquals("Invalid IDs", exception.getMessage());
    }

    @Test
    @Description("US-0001: un usuario no puede tener ID 0.")
    public void AUserCannotHaveZeroIdTest() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.followASpecificUserById(0,1));

        assertEquals("Invalid IDs", exception.getMessage());
    }

    @Test
    @Description("US-0001: un seguido no puede tener ID 0.")
    public void AFollowCannotHaveZeroIdTest() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.followASpecificUserById(1,0));

        assertEquals("Invalid IDs", exception.getMessage());
    }
}

