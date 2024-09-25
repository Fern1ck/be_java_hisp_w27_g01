package ar.com.mercadolibre.socialmeli.controller;

import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.service.IProductService;
import ar.com.mercadolibre.socialmeli.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("/products/test")
    public ResponseEntity<?> test(){

        List<User> users;
        users = Utils.createDefaultUsers();
        
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
