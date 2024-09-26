package ar.com.mercadolibre.socialmeli.repository.impl;

import ar.com.mercadolibre.socialmeli.entity.Post;
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
    public Integer createPost(User user, Post post) {
        Integer createdId = user.getPosts().stream().mapToInt(Post::getPostId).max().orElse(0) + 1;
        post.setPostId(createdId);

        user.addToPosts(post);

        users.replaceAll(u -> u.getUserId().equals(user.getUserId()) ? user : u);
        return createdId;
    }

    @Override
    public User getUserById(Integer id) {
        return users.stream().filter(u -> u.getUserId().equals(id)).findFirst().orElse(null);
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

    @Override
    public User getUserById(Integer userId) {
        return users.stream().filter(u -> u.getUserId().equals(userId)).findFirst().orElse(null);
    }

    @Override
    public Boolean addPostToUser(User user, Post post) {
        return user.addToPosts(post);
    }
}
