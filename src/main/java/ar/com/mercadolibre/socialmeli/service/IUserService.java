package ar.com.mercadolibre.socialmeli.service;

import ar.com.mercadolibre.socialmeli.dto.response.UserOkDTO;

public interface IUserService {
    UserOkDTO followASpecificUserById(Integer userId, Integer userIdToFollow);
}
