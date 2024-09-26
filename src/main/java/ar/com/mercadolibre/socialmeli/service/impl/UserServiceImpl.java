package ar.com.mercadolibre.socialmeli.service.impl;

import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerCountDTO;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.repository.IRepository;
import ar.com.mercadolibre.socialmeli.service.IProductService;
import ar.com.mercadolibre.socialmeli.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IRepository repository;

    public UserFollowerCountDTO getFollowerCount(Integer userId){

        if (userId == null || userId <= 0){
            throw new BadRequestException("Invalid ID");
        }

        if (!repository.existId(userId)){
            throw new BadRequestException("Invalid ID");
        }

        long followerCount = repository.getUsers().stream()
                .filter(user -> user.getFollowedIds() != null && user.getFollowedIds().contains(userId))
                .count();

        User user = repository.getUserById(userId);

        return new UserFollowerCountDTO(userId, user.getUserName(), (int) followerCount);
    }
}
