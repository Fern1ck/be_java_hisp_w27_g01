package ar.com.mercadolibre.socialmeli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowedDTO {
    private Integer userId;
    private String userName;
    private List<UserFollowedListDTO> userFollowedListDTO;
}
