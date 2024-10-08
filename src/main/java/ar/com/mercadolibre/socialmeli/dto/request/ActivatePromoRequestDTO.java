package ar.com.mercadolibre.socialmeli.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivatePromoRequestDTO {

    @NotNull(message = "El id no puede estar vacío.")
    @Positive(message = "El id debe ser mayor a cero")
    @JsonProperty("user_id")
    private Integer userId;

    @NotNull(message = "La id no puede estar vacía.")
    @Positive(message = "El id debe ser mayor a cero.")
    @JsonProperty("post_id")
    private Integer postId;

    @DecimalMax(value = "0.50", message = "El descuento no puede ser mayor al 50%.")
    @Positive(message = "El descuento debe ser mayor a 0.")
    @NotNull(message = "El descuento no puede estar vacio.")
    private Double discount;
}
