package ar.com.mercadolibre.socialmeli.repository;

import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.User;

import java.util.List;

public interface IRepository {

    Boolean idExist(Integer userId);

    User getUserById(Integer userId);

    List<User> getUsers();
    
    Boolean existId(Integer userId);

    User findUserById(Integer userId);

    Boolean addPostToUser(User user, Post post);
}
