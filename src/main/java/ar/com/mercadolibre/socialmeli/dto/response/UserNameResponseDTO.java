package ar.com.mercadolibre.socialmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNameResponseDTO {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("user_name")
    private String userName;


}
