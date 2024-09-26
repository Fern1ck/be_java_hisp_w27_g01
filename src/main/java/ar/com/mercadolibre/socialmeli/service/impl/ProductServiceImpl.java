package ar.com.mercadolibre.socialmeli.service.impl;

import ar.com.mercadolibre.socialmeli.dto.CreatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.CreatePromoResponseDTO;
import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.Product;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.NotFoundException;
import ar.com.mercadolibre.socialmeli.repository.IRepository;
import ar.com.mercadolibre.socialmeli.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private IRepository repository;

    @Override
    public CreatePromoResponseDTO createPromo(CreatePromoRequestDTO requestDto) {
        User user = repository.getUserById(requestDto.getUserId());
        if(user == null){
            throw new NotFoundException("El usuario no existe");
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
