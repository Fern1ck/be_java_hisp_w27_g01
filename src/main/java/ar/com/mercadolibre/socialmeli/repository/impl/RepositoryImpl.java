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
    private List<User> users = new ArrayList<>();
    public RepositoryImpl(){
        users = Utils.createDefaultUsers();
    }

    public List<User> getUsers(){
        return this.users;
    }

    public Boolean idExist(Integer userId){
        return this.users.stream()
                .anyMatch(user -> user.getUserId().equals(userId));
    }

    public Boolean existId(Integer userId){
        return this.users.stream()
                .anyMatch(user -> user.getUserId().equals(userId));
    }

    public User getUserById(Integer userId){
        return this.users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public User findUserById(Integer userId) {
        return users.stream().filter(u -> u.getUserId().equals(userId)).findFirst().orElse(null);
    }
}
