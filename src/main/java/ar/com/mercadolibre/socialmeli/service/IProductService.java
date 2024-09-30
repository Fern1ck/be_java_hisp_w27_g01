package ar.com.mercadolibre.socialmeli.service;

import ar.com.mercadolibre.socialmeli.dto.PostFollowDTO;
import ar.com.mercadolibre.socialmeli.dto.request.CreatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostsFollowersListDTO;
import ar.com.mercadolibre.socialmeli.dto.response.CreatePromoResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostOkDTO;
import ar.com.mercadolibre.socialmeli.dto.response.ProductPromoCountDTO;


public interface IProductService {

    PostOkDTO registerANewPublication(PostDTO postDTO);

    CreatePromoResponseDTO createPromo(CreatePromoRequestDTO dto);

    PostsFollowersListDTO getRecentPostFromFollowedUsers(Integer userId, String order);

    ProductPromoCountDTO promoProductsCountBySeller(Integer userId);

    PostFollowDTO deletePost(Integer userId, Integer postId);

}
