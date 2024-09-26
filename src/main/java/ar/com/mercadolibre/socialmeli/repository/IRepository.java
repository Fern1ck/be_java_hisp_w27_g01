package ar.com.mercadolibre.socialmeli.repository;

import ar.com.mercadolibre.socialmeli.entity.User;

import java.util.List;

public interface IRepository {
    User findByUserId(Integer userId);
    List<User> getAllUsers();
}
