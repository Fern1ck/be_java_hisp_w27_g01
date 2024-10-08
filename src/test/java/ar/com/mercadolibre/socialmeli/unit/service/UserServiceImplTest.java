package ar.com.mercadolibre.socialmeli.unit.service;

import ar.com.mercadolibre.socialmeli.repository.impl.RepositoryImpl;
import ar.com.mercadolibre.socialmeli.service.impl.UserServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    RepositoryImpl repository;

    @InjectMocks
    UserServiceImpl userService;
}
