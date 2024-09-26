package ar.com.mercadolibre.socialmeli.dto;

import ar.com.mercadolibre.socialmeli.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class PostDTO {
    //PostDTO sin promo

    @JsonProperty("user_id")
    private Integer UserId;
    @JsonProperty("post_id")
    private Integer postId;

    private LocalDate date;
    private ProductDTO product;
    private Integer category;
    private Double price;




}
