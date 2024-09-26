package ar.com.mercadolibre.socialmeli.service.impl;

import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostOkDTO;
import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.repository.IRepository;
import ar.com.mercadolibre.socialmeli.service.IProductService;
import ar.com.mercadolibre.socialmeli.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private IRepository repository;

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
}
