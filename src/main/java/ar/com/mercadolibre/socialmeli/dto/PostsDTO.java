package ar.com.mercadolibre.socialmeli.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostsDTO {
    @JsonProperty("user_id")
    private Integer userId;

    private List<PostDTO> postsList;
}
