package ar.com.mercadolibre.socialmeli.service.impl;


import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
