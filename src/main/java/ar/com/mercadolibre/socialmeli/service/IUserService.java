package ar.com.mercadolibre.socialmeli.service;

import ar.com.mercadolibre.socialmeli.dto.UserFollowedDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerCountDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerListDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserOkDTO;
import java.util.List;

public interface IUserService {

    List<UserFollowedDTO> findByFollowed(Integer userId, String order);

    UserFollowerCountDTO getFollowerCount(Integer userId);

    UserFollowerListDTO getFollowerList(Integer userId, String order);

    UserOkDTO followASpecificUserById(Integer userId, Integer userIdToFollow);

    UserOkDTO unfollowASpecificUserById(Integer userId, Integer userIdToUnfollow);

}
