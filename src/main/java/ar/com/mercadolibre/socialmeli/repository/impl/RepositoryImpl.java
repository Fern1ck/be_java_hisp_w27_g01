package ar.com.mercadolibre.socialmeli.repository.impl;

import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.repository.IRepository;
import ar.com.mercadolibre.socialmeli.utils.Utils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class RepositoryImpl implements IRepository {
    private List<User> users;
    public RepositoryImpl(){
        users = Utils.createDefaultUsers();
    }

    @Override
    public Integer createPost(Integer userId, Post post) {
        User updatedUser = getUserById(userId);
        Integer createdId = updatedUser.getPosts().size() + 1;
        post.setPostId(createdId);
        List<Post> posts = updatedUser.getPosts();
        posts.add(post);
        updatedUser.setPosts(posts);
        users.replaceAll(user -> user.getUserId().equals(userId) ? updatedUser : user);
        return createdId;
    }

    @Override
    public User getUserById(Integer id) {
        return users.stream().filter(u -> u.getUserId().equals(id)).findFirst().orElse(null);
    }
}
