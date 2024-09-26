package ar.com.mercadolibre.socialmeli.controller;

import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.CreatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.CreatePromoResponseDTO;
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
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/post")
    public ResponseEntity<?> registerANewPublication(@RequestBody PostDTO publicationDTO) {
        return new ResponseEntity<>(productService.registerANewPublication(publicationDTO), HttpStatus.OK);
    }
    @PostMapping("promo-post")
    public ResponseEntity<?> createPromo(@RequestBody() CreatePromoRequestDTO requestDto){
            CreatePromoResponseDTO dto = productService.createPromo(requestDto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

    @GetMapping("/followed/{userId}/list")
    public ResponseEntity<?> getRecentPostFromFollowedUsers(@PathVariable int userId){
        return new ResponseEntity<>(productService.getRecentPostFromFollowedUsers(userId), HttpStatus.OK);
    }

}
