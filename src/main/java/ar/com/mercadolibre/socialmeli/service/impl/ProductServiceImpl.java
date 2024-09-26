package ar.com.mercadolibre.socialmeli.service.impl;


import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostsFollowersListDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostsIdDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostOkDTO;
import ar.com.mercadolibre.socialmeli.dto.CreatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.CreatePromoResponseDTO;
import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.Product;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.exception.NotFoundException;
import ar.com.mercadolibre.socialmeli.repository.IRepository;
import ar.com.mercadolibre.socialmeli.service.IProductService;
import ar.com.mercadolibre.socialmeli.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    private IRepository repository;

    public ProductServiceImpl(IRepository repository) {
        this.repository = repository;
    }
  
    @Override
    public PostOkDTO registerANewPublication(PostDTO postDTO) {

        if (postDTO == null) {
            throw new BadRequestException("PublicationDTO is null");
        }

        Post post = Utils.changePostDtoToEntity(postDTO);

        User userPost = repository.getUserById(postDTO.getUserId());

        if(userPost == null) {
            throw new BadRequestException("User not found");
        }

        boolean isOk = repository.addPostToUser(userPost, post);

        if (!isOk) {
            throw new BadRequestException("Post already exists");
        }
        return new PostOkDTO("OK");
    }

    @Override
    public CreatePromoResponseDTO createPromo(CreatePromoRequestDTO requestDto) {
      
        User user = repository.getUserById(requestDto.getUserId());
        if(user == null){
            throw new BadRequestException("User ID: " + requestDto.getUserId() + " doesn´t exist.");
        }

        Post post = new Post();
        post.setProduct(requestDto.getProduct());
        post.setCategory(requestDto.getCategory());
        post.setPrice(requestDto.getPrice());
        post.setHasPromo(requestDto.isHasPromo());
        post.setDiscount(requestDto.getDiscount());
      
        Integer createdId = repository.createPost(user, post);

        CreatePromoResponseDTO responseDto = new CreatePromoResponseDTO();
        responseDto.setCreatedId(createdId);
        return responseDto;

    }


    /**
     * Obtener un listado de las publicaciones realizadas por uno de los los vendedores que un usuario sigue
     */
    public List<PostsIdDTO> postByUser(int userId) {

        List<Post> posts=  repository.getUsers().stream().filter(u->u.getUserId().equals(userId)).findFirst().map(User::getPosts).orElseThrow(()->new BadRequestException("is bad request"));

  //     ObjectMapper mapper= new ObjectMapper();
       // return posts.stream().map(p->mapper.convertValue(p,PostsIdDTO.class)).toList();

     /*   List<PostDTO> recentPosts = repository.getUsers().stream()
                .filter(u -> followedIds.contains(u.getUserId()))
                .flatMap(u -> u.getPosts().stream())
                .filter(post -> post.getDate().isAfter(twoWeeksAgo))
                .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                .map(post -> new PostsIdDTO(userId, post.getPostId(), post.getProduct(), post.getDate(), post.getCategory(), post.getPrice()))
                .toList();

        return recentPosts;*/

    }


    /**
     * list of posts from users flolowed
     */
    @Override
    public PostsFollowersListDTO postsFromFolloweds(int userId){
        if(!repository.existId(userId)){
            throw new NotFoundException("Not found id:" + userId);
        }
        if(userId<=0){
            throw new BadRequestException("id Number incorrect");
        }

        PostsFollowersListDTO dto= new PostsFollowersListDTO();
        dto.setUserId(userId);
        User user= repository.getUserById(userId);
        List<Integer> listFollowedsIds= user.getFollowedIds();

        for(Integer i: listFollowedsIds) {
            dto.setPosts(postByUser(i));
        }

        return dto;
    }


}
