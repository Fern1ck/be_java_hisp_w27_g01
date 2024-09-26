package ar.com.mercadolibre.socialmeli.service;

import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerCountDTO;

public interface IUserService {

    UserFollowerCountDTO getFollowerCount(Integer userId);
}
