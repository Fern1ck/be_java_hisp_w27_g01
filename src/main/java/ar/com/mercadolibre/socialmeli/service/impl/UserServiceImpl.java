package ar.com.mercadolibre.socialmeli.service.impl;

import ar.com.mercadolibre.socialmeli.dto.response.UserOkDTO;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.repository.IRepository;
import ar.com.mercadolibre.socialmeli.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IRepository repository;

    @Override
    public UserOkDTO followASpecificUserById(Integer userId, Integer userIdToFollow) {
        if (userIdToFollow == null ||userId == null || userIdToFollow < 0 || userId < 0) {
            throw new BadRequestException("Status Code 400 (Bad Request)");
        }
        User user = repository.findUserById(userId);
        if (user == null) {
            throw new BadRequestException("Status Code 400 (Bad Request) userId does not exist");
        }
        user.setFollowedId(userIdToFollow);

        return new UserOkDTO("Status Code 200 (todo OK)");
    }
}
