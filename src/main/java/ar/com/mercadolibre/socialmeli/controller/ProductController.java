package ar.com.mercadolibre.socialmeli.controller;

import ar.com.mercadolibre.socialmeli.dto.request.CreatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.response.CreatePromoResponseDTO;
import ar.com.mercadolibre.socialmeli.service.IProductService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping("/search/date")
    public ResponseEntity<?> searchProductsPostsByDate(@RequestParam(name = "date_start", required = true)
                                                           @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                           LocalDate dateStart,
                                                       @RequestParam(name = "date_end", required = false)
                                                           @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                       LocalDate dateEnd) {
        return new ResponseEntity<>(productService.searchProductsPostsByDate(dateStart, dateEnd), HttpStatus.OK);
    }

}
