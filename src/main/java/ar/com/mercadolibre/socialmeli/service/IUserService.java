package ar.com.mercadolibre.socialmeli.service;

import ar.com.mercadolibre.socialmeli.dto.UserFollowedDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerCountDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserOkDTO;
import java.util.List;

public interface IUserService {

    List<UserFollowedDTO> findByFollowed(Integer userId);

    UserFollowerCountDTO getFollowerCount(Integer userId);

    UserOkDTO followASpecificUserById(Integer userId, Integer userIdToFollow);

}
