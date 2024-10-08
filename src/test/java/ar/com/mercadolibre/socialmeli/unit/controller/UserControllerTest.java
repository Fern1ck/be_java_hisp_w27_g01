package ar.com.mercadolibre.socialmeli.unit.controller;

import ar.com.mercadolibre.socialmeli.controller.UserController;
import ar.com.mercadolibre.socialmeli.service.impl.UserServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserServiceImpl userService;

    @InjectMocks
    UserController userController;


}
