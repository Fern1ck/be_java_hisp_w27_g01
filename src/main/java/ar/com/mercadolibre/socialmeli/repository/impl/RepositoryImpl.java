package ar.com.mercadolibre.socialmeli.repository.impl;

import ar.com.mercadolibre.socialmeli.entity.Product;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.repository.IRepository;
import ar.com.mercadolibre.socialmeli.utils.Utils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Repository
public class RepositoryImpl implements IRepository {
    private List<User> users;
    public RepositoryImpl(){
        users = Utils.createDefaultUsers();
    }

    @Override
    public User findByUserId(Integer userId){
        return users.stream().filter(user -> Objects.equals(user.getUserId(), userId)).findFirst().orElse(null);
    }

    @Override
    public List<User> getAllUsers(){
        return users;
    }
}
