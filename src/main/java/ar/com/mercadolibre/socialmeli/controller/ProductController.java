package ar.com.mercadolibre.socialmeli.controller;

import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.service.IProductService;
import ar.com.mercadolibre.socialmeli.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("/products/test")
    public ResponseEntity<?> test(){

        List<User> users;
        users = Utils.createDefaultUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<?> registerANewPublication(@RequestBody PostDTO publicationDTO){
        return new ResponseEntity<>(productService.registerANewPublication(publicationDTO), HttpStatus.OK);
    }
}
