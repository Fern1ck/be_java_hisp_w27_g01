package ar.com.mercadolibre.socialmeli.repository;

import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.User;

import java.util.List;

public interface IRepository {
  
    Integer createPost(User user, Post post);
  
    User getUserById(Integer id);

    Boolean idExist(Integer userId);

    User getUserById(Integer userId);
  
    List<User> getUsers();

    Boolean addPostToUser(User user, Post post);

    Boolean existId(Integer userId);

}
