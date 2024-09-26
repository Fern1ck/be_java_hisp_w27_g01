package ar.com.mercadolibre.socialmeli.service;

import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerCountDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserOkDTO;

public interface IUserService {

    UserFollowerCountDTO getFollowerCount(Integer userId);

    UserOkDTO followASpecificUserById(Integer userId, Integer userIdToFollow);

}
