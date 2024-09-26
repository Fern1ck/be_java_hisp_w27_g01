package ar.com.mercadolibre.socialmeli.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class User {
    public User(Integer userId, String userName){
        this.userId = userId;
        this.userName = userName;
    }

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("followed_ids")
    private List<Integer> followedIds;
    private List<Post> posts;

    public void addToPosts(Post post){
        this.posts.add(post);
    }
}
