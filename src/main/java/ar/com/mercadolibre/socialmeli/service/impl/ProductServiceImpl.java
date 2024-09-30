package ar.com.mercadolibre.socialmeli.service.impl;

import ar.com.mercadolibre.socialmeli.dto.request.CreatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostsFollowersListDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostsIdDTO;
import ar.com.mercadolibre.socialmeli.dto.response.CreatePromoResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostOkDTO;
import ar.com.mercadolibre.socialmeli.dto.response.ProductPromoCountDTO;
import ar.com.mercadolibre.socialmeli.dto.response.SearchDTO;
import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.exception.NotFoundException;
import ar.com.mercadolibre.socialmeli.repository.IRepository;
import ar.com.mercadolibre.socialmeli.service.IProductService;
import ar.com.mercadolibre.socialmeli.utils.Utils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        User userPost = repository.getUserById(postDTO.getUserId());
        Integer id =  userPost.getPosts().stream().mapToInt(Post::getPostId).max().orElse(0) + 1;
        Post post = Utils.changePostDtoToEntity(postDTO);
        post.setPostId(id);

        post.setHasPromo(false);
        post.setDiscount(0.0);
        
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
        post.setDate(requestDto.getDate());
        post.setHasPromo(requestDto.isHasPromo());
        post.setDiscount(requestDto.getDiscount());
      
        Integer createdId = repository.createPost(user, post);

        CreatePromoResponseDTO responseDto = new CreatePromoResponseDTO();
        responseDto.setCreatedId(createdId);

        return responseDto;
    }

    @Override
    public PostsFollowersListDTO getRecentPostFromFollowedUsers(Integer userId, String order){

        User user = repository.getUserById(userId);

        if (user == null){
            throw new BadRequestException("User ID: " + userId + " doesn´t exist.");
        }

        List<Integer> followedIds = user.getFollowedIds();

        if (followedIds == null || followedIds.isEmpty()){
            throw new BadRequestException("User ID: " + userId + " is not following anyone.");
        }

        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);

        List<PostsIdDTO> recentPost = repository.getUsers().stream()
                .filter(u -> followedIds.contains(u.getUserId()))
                .flatMap(u -> u.getPosts().stream().map(post -> new PostsIdDTO(u.getUserId(), post.getPostId() , post.getDate(), post.getProduct(), post.getCategory(), post.getPrice())))
                .filter(postDTO -> postDTO.getDate().isAfter(twoWeeksAgo))
                .toList();

        if(recentPost == null || recentPost.isEmpty()){
            throw new BadRequestException("There aren't posts of minus two weeks.");
        }

        if (order != null) {
            if (order.equalsIgnoreCase("date_asc")) {
                recentPost = recentPost.stream()
                        .sorted(Comparator.comparing(PostsIdDTO::getDate))
                        .collect(Collectors.toList());
            } else if (order.equalsIgnoreCase("date_desc")) {
                recentPost = recentPost.stream()
                        .sorted(Comparator.comparing(PostsIdDTO::getDate).reversed())
                        .collect(Collectors.toList());
            }
        }

        return new PostsFollowersListDTO(userId, recentPost);
    }

    @Override
    public ProductPromoCountDTO promoProductsCountBySeller(Integer userId) {
        if (!repository.existId(userId)){
            throw new NotFoundException("User ID: " + userId + " doesn´t exist.");
        }

        User user = repository.getUserById(userId);

        Integer promoCount = Integer.parseInt(String.valueOf(user.getPosts().stream()
                .filter(Post::getHasPromo)
                .count()));

        return new ProductPromoCountDTO(user.getUserId(), user.getUserName(), promoCount);
    }

    @Override
    public List<SearchDTO> search(String query, Integer userId) {
        if (userId != null && !repository.existId(userId)){
            throw new NotFoundException("User ID: " + userId + " doesn´t exist.");
        }

        //List<SearchDTO> dtosADevolver = new ArrayList<>();
        List<User> usuarios = repository.getUsers();

        if(userId != null){
            usuarios = usuarios.stream().filter(u -> u.getUserId().equals(userId)).findFirst().stream().toList();
        }

        //for(User user : usuarios){
        //    for(Post post : user.getPosts()){
        //        //Se normaliza quitando tildes, y la comparación se realiza en minúsculas
        //            if(compareQuery(post.getProduct().getProductName(), query) || compareQuery(post.getProduct().getBrand(), query)){
        //            SearchDTO dto = new SearchDTO(post.getPostId(), post.getProduct(), post.getDate(), post.getCategory(), post.getPrice(), post.getHasPromo(), post.getDiscount(), user.getUserId());
        //            dtosADevolver.add(dto);
        //        }
        //    }
        //}

        return usuarios.stream()
                .flatMap(user -> user.getPosts().stream()
                        .filter(post -> compareQuery(post.getProduct().getProductName(), query) || compareQuery(post.getProduct().getBrand(), query))
                        .map(post -> new SearchDTO(post.getPostId(), post.getProduct(), post.getDate(), post.getCategory(), post.getPrice(), post.getHasPromo(), post.getDiscount(), user.getUserId())))
                .toList();

        //return dtosADevolver;
    }

    private Boolean compareQuery(String str, String query){
        return Utils.limpiarTildes(str).toLowerCase().contains(Utils.limpiarTildes(query).toLowerCase());
    }
}


