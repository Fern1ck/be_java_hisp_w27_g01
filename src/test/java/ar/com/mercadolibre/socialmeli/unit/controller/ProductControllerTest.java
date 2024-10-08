package ar.com.mercadolibre.socialmeli.unit.controller;

import ar.com.mercadolibre.socialmeli.controller.ProductController;
import ar.com.mercadolibre.socialmeli.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    ProductServiceImpl productService;

    @InjectMocks
    ProductController productController;

}
