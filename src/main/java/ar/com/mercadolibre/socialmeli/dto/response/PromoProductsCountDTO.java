package ar.com.mercadolibre.socialmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"user_id", "user_name", "promo_products_count"})
public class PromoProductsCountDTO {
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("user_name")
    private String name;

    @JsonProperty("promo_products_count")
    private Integer promoProductsCount;


}
