package ar.com.mercadolibre.socialmeli.repository;

import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.User;

public interface IRepository {
    Integer createPost(User user, Post post);
    User getUserById(Integer id);
}
