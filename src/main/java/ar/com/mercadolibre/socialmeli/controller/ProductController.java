package ar.com.mercadolibre.socialmeli.controller;

import ar.com.mercadolibre.socialmeli.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("/products/test")
    public ResponseEntity<?> test(){
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
