package ar.com.mercadolibre.socialmeli.service;


import ar.com.mercadolibre.socialmeli.dto.request.ActivatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.request.CreatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.response.*;

import java.time.LocalDate;
import java.util.List;



public interface IProductService {

    PostOkResponseDTO registerANewPublication(PostRequestDTO postRequestDTO);

    CreatePromoResponseDTO createPromo(CreatePromoRequestDTO dto);

    PostFollowersListResponseDTO getRecentPostFromFollowedUsers(Integer userId, String order);

    ProductPromoCountResponseDTO promoProductsCountBySeller(Integer userId);

    List<PostIdResponseDTO> searchProductsPostsByDate(LocalDate dateStart, LocalDate dateEnd);

    PostOkResponseDTO deletePost(Integer userId, Integer postId);

    List<SearchResponseDTO> search(String query, Integer userId);

    ProductPostsHistoryResponseDTO getSellerPostListHistory(Integer userId, Boolean withPromo);

    PostOkResponseDTO activatePromo(ActivatePromoRequestDTO promo);
}

