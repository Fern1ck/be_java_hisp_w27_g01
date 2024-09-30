package ar.com.mercadolibre.socialmeli.controller;

import ar.com.mercadolibre.socialmeli.dto.request.CreatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.response.CreatePromoResponseDTO;
import ar.com.mercadolibre.socialmeli.service.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getRecentPostFromFollowedUsers(@PathVariable int userId, @RequestParam(required = false) String order){
        return new ResponseEntity<>(productService.getRecentPostFromFollowedUsers(userId, order), HttpStatus.OK);
    }

    @GetMapping("/promo-post/count")
    public ResponseEntity<?> promoProductsCountBySeller(@RequestParam (required = true, name = "user_id") int userId){
        return new ResponseEntity<>(productService.promoProductsCountBySeller(userId), HttpStatus.OK);
    }

    @DeleteMapping("/post/{userId}/{postId}")
    public ResponseEntity<?> getHidePost(@PathVariable Integer userId,@PathVariable Integer postId){
        return new ResponseEntity<>(productService.deletePost(userId, postId), HttpStatus.OK);
    }

}
