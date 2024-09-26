package ar.com.mercadolibre.socialmeli.repository;

import ar.com.mercadolibre.socialmeli.entity.User;

import java.util.List;

public interface IRepository {

    Boolean existId(Integer userId);

    List<User> getUsers();

    User getUserById(Integer userId);

    User findUserById(Integer userId);
}
