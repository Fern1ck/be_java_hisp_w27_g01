package ar.com.mercadolibre.socialmeli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowedListDTO {
    private Integer userId;
    private String userName;
}
