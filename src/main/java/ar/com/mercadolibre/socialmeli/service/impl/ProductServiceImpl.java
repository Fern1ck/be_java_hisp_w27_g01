package ar.com.mercadolibre.socialmeli.service.impl;

import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostsFollowersListDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostsIdDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostOkDTO;
import ar.com.mercadolibre.socialmeli.dto.CreatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.CreatePromoResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.ProductPromoCountDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PromoProductsCountDTO;
import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.exception.NotFoundException;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.repository.IRepository;
import ar.com.mercadolibre.socialmeli.service.IProductService;
import ar.com.mercadolibre.socialmeli.utils.Utils;

import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDate;

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

}


