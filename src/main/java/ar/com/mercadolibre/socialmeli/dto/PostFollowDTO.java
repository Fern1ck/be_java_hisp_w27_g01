package ar.com.mercadolibre.socialmeli.dto;

import ar.com.mercadolibre.socialmeli.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostFollowDTO {
    private Integer userId;
    private List<Post> posts;
}
