package ar.com.mercadolibre.socialmeli.service.impl;

import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerListDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserNameDTO;
import ar.com.mercadolibre.socialmeli.dto.UserFollowedDTO;
import ar.com.mercadolibre.socialmeli.dto.UserFollowedListDTO;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.exception.NotFoundException;
import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerCountDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserOkDTO;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.repository.IRepository;
import ar.com.mercadolibre.socialmeli.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IRepository repository;

    @Override
    public List<UserFollowedDTO> findByFollowed(Integer userId) {
        User user = repository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("El User Id: " + userId + ", no existe.");
        }
        List<Integer> follows = user.getFollowedIds();
        List<User> allUsers = repository.getUsers();

        if (follows == null) {
            throw new BadRequestException("El usuario con Id: " + user.getUserId() + ", no sigue a nadie.");
        }

        List<UserFollowedListDTO> followedList = follows.stream()
                .map(followedId -> allUsers.stream()
                        .filter(user1 -> user1.getUserId().equals(followedId))
                        .findFirst()
                        .map(user1 -> new UserFollowedListDTO(user1.getUserId(), user1.getUserName())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<UserFollowedDTO> userFollowedDTOList = new ArrayList<>();
        userFollowedDTOList.add(new UserFollowedDTO(user.getUserId(), user.getUserName(), followedList));
        return userFollowedDTOList;
    }

    public UserFollowerCountDTO getFollowerCount(Integer userId){

        if (userId == null || userId <= 0){
            throw new BadRequestException("Invalid ID");
        }

        if (!repository.idExist(userId)){
            throw new BadRequestException("Invalid ID");
        }

        List<UserNameDTO> followers = repository.getUsers().stream()
                .filter(user -> user.getFollowedIds() != null && user.getFollowedIds().contains(userId))
                .map(user -> new UserNameDTO(user.getUserId(), user.getUserName()))
                .toList();

        User user = repository.getUserById(userId);

        return new UserFollowerListDTO(userId, user.getUserName(), followers);
    }
   
    @Override
    public UserOkDTO followASpecificUserById(Integer userId, Integer userIdToFollow) {
        if (userIdToFollow == null ||userId == null || userIdToFollow < 0 || userId < 0) {
            throw new BadRequestException("Status Code 400 (Bad Request)");
        }
        User user = repository.findUserById(userId);
        if (user == null) {
            throw new BadRequestException("Status Code 400 (Bad Request) userId does not exist");
        }
        user.addFollowedId(userIdToFollow);

        return new UserOkDTO("Status Code 200 (todo OK)");

    }
}
