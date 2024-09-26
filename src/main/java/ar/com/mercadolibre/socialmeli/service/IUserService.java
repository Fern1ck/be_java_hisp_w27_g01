package ar.com.mercadolibre.socialmeli.service;

import ar.com.mercadolibre.socialmeli.dto.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.PostsDTO;
import ar.com.mercadolibre.socialmeli.entity.Post;

import java.util.List;

public interface IUserService {

    List<List<PostDTO>> postsOfFolloweds(int userId);
}
