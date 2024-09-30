package ar.com.mercadolibre.socialmeli.service;

import ar.com.mercadolibre.socialmeli.dto.PostFollowDTO;


import ar.com.mercadolibre.socialmeli.dto.request.*;
import ar.com.mercadolibre.socialmeli.dto.request.ActivatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.request.CreatePromoRequestDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostsFollowersListDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostsIdDTO;
import ar.com.mercadolibre.socialmeli.dto.response.CreatePromoResponseDTO;
import ar.com.mercadolibre.socialmeli.dto.response.PostOkDTO;
import ar.com.mercadolibre.socialmeli.dto.response.ProductPromoCountDTO;

import java.time.LocalDate;
import java.util.List;

import ar.com.mercadolibre.socialmeli.dto.response.SearchDTO;

import java.util.List;

import ar.com.mercadolibre.socialmeli.dto.response.ProductPostsHistoryDTO;



public interface IProductService {

    PostOkDTO registerANewPublication(PostDTO postDTO);

    CreatePromoResponseDTO createPromo(CreatePromoRequestDTO dto);

    PostsFollowersListDTO getRecentPostFromFollowedUsers(Integer userId, String order);

    ProductPromoCountDTO promoProductsCountBySeller(Integer userId);

    List<PostsIdDTO> searchProductsPostsByDate(LocalDate dateStart, LocalDate dateEnd);

    PostOkDTO deletePost(Integer userId, Integer postId);

    List<SearchDTO> search(String query, Integer userId);

    ProductPostsHistoryDTO getSellerPostListHistory(Integer userId, Boolean withPromo);

    PostOkDTO activatePromo(ActivatePromoRequestDTO promo);
}

