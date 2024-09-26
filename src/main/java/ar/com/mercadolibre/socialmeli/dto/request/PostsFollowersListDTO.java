package ar.com.mercadolibre.socialmeli.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"user_id", "post"})
public class PostsFollowersListDTO {
    @JsonProperty("user_id")
    private Integer userId;

    private List<PostsIdDTO> posts;

}
