package ar.com.mercadolibre.socialmeli.service;

import ar.com.mercadolibre.socialmeli.dto.UserFollowedDTO;

import java.util.List;

public interface IUserService {

    List<UserFollowedDTO> findByFollowed(Integer userId);

}
