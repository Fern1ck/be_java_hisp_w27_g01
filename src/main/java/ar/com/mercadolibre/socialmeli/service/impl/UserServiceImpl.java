package ar.com.mercadolibre.socialmeli.service.impl;

import ar.com.mercadolibre.socialmeli.dto.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.PostsDTO;
import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.NotFoundException;
import ar.com.mercadolibre.socialmeli.repository.IRepository;
import ar.com.mercadolibre.socialmeli.service.IUserService;
import ar.com.mercadolibre.socialmeli.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IRepository repository;



    /**
     * Obtener un listado de las publicaciones realizadas por los vendedores
     */
    public List<Post> postsByUser(int userId){
        List<User> users= Utils.createDefaultUsers().stream().toList();

        User user= users.stream()
                .filter(u->u.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(()->new NullPointerException("No existe el usuario"));


        return user.getPosts();
    }


    /**
     * lista de ids que un usuario sigue (vendedores)
     */
    public List<Integer> followedByUser(int userId){
        User user= Utils.createDefaultUsers().stream().filter(u->u.getUserId().equals(userId)).findFirst().orElseThrow(()->new NotFoundException("No se encontro usuario"));
        return user.getFollowedIds();
    }


    /**
     * Obtener un listado de las publicaciones realizadas por uno de los los vendedores que un usuario sigue
     */

    public List<Post> postByUser(int userId) {
        List<Integer> idsFolloweds= followedByUser(userId);
        Integer idSeller= idsFolloweds.stream().filter(u->u.equals(userId)).findFirst().orElseThrow(()->new NotFoundException("No se encontro id: "+ userId + "de usuario"));
        User userSeller= Utils.createDefaultUsers().stream().filter(u->u.getUserId().equals(idSeller)).findFirst().orElseThrow(()->new NotFoundException("No se encontro usuario"));
        List<Post> post= userSeller.getPosts();

       // ObjectMapper mapper= new ObjectMapper();
        return userSeller.getPosts();
    }

    //listado de los posteos de los usuarios que sigue un usario
    @Override
    public List<List<PostDTO>> postsOfFolloweds(int userId){
        User user= Utils.createDefaultUsers().stream().filter(u->u.getUserId().equals(userId)).findFirst().orElseThrow(()->new NotFoundException("No se encontro usuario"));
        List<List<Post>> listsPostsFollowed= new ArrayList<>();
        for(Integer i: user.getFollowedIds()){
            listsPostsFollowed.add(postsByUser(i));
        }
        ObjectMapper mapper=new ObjectMapper();

        return listsPostsFollowed.stream()
                .map(posts -> mapper.convertValue(posts, new TypeReference<List<PostDTO>>() {}))
                .collect(Collectors.toList());
    }


}
