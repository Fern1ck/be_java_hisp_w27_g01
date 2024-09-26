package ar.com.mercadolibre.socialmeli.repository;

import ar.com.mercadolibre.socialmeli.entity.User;

import java.util.List;

public interface IRepository {

    Boolean idExist(Integer userId);

    public User getUserById(Integer userId);

    List<User> getUsers();
}
