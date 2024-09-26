package ar.com.mercadolibre.socialmeli.repository;

import ar.com.mercadolibre.socialmeli.entity.User;

public interface IRepository {
    User findUserById(Integer userId);
}
