package ar.com.mercadolibre.socialmeli.service;

import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerListDTO;

public interface IUserService {

    UserFollowerListDTO getFollowerList(Integer userId);
}
