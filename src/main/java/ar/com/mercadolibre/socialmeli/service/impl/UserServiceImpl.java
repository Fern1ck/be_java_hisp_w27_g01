package ar.com.mercadolibre.socialmeli.service.impl;

import ar.com.mercadolibre.socialmeli.dto.UserFollowedDTO;
import ar.com.mercadolibre.socialmeli.dto.UserFollowedListDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostDTO;
import ar.com.mercadolibre.socialmeli.dto.request.PostsFollowersListDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerCountDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserFollowerListDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserNameDTO;
import ar.com.mercadolibre.socialmeli.dto.response.UserOkDTO;
import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.exception.BadRequestException;
import ar.com.mercadolibre.socialmeli.exception.NotFoundException;
import ar.com.mercadolibre.socialmeli.repository.IRepository;
import ar.com.mercadolibre.socialmeli.service.IUserService;
import ar.com.mercadolibre.socialmeli.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    private final IRepository repository;

    public UserServiceImpl(IRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserFollowedDTO> findByFollowed(Integer userId, String order) {
        User user = repository.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User ID: " + userId + " doesn't exist.");
        }
        List<Integer> follows = user.getFollowedIds();
        List<User> allUsers = repository.getUsers();

        if (follows == null) {
            throw new BadRequestException("User with the ID: "  + user.getUserId() + " is not following anyone.");
        }

        List<UserFollowedListDTO> followedList = follows.stream()
                .map(followedId -> allUsers.stream()
                        .filter(user1 -> user1.getUserId().equals(followedId))
                        .findFirst()
                        .map(user1 -> new UserFollowedListDTO(user1.getUserId(), user1.getUserName())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if(order != null){
            if(order.equals("name_asc")){
                followedList = followedList.stream().sorted(Comparator.comparing(UserFollowedListDTO::getUserName)).toList();
            }
            else if(order.equals("name_desc")){
                followedList = followedList.stream().sorted(Comparator.comparing(UserFollowedListDTO::getUserName).reversed()).toList();
            }
            else{
                throw new BadRequestException("Order " + order + " not recognized.");
            }
        }

        List<UserFollowedDTO> userFollowedDTOList = new ArrayList<>();
        userFollowedDTOList.add(new UserFollowedDTO(user.getUserId(), user.getUserName(), followedList));
        return userFollowedDTOList;
    }

    public UserFollowerListDTO getFollowerList(Integer userId, String order){

        if (userId == null || userId <= 0){
            throw new BadRequestException("User ID: " + userId + " is invalid.");
        }

        if (!repository.existId(userId)){
            throw new BadRequestException("User ID: " + userId + " doesn't exist.");
        }

        List<UserNameDTO> followers = repository.getUsers().stream()
                .filter(user -> user.getFollowedIds() != null && user.getFollowedIds().contains(userId))
                .map(user -> new UserNameDTO(user.getUserId(), user.getUserName()))
                .toList();

        if(order != null){
            if(order.equals("name_asc")){
                followers = followers.stream().sorted(Comparator.comparing(UserNameDTO::getUserName)).toList();
            }
            else if(order.equals("name_desc")){
                followers = followers.stream().sorted(Comparator.comparing(UserNameDTO::getUserName).reversed()).toList();
            }
            else{
                throw new BadRequestException("Order " + order + " not recognized.");
            }
        }
        User user = repository.getUserById(userId);

        return new UserFollowerListDTO(userId, user.getUserName(), followers);

    }


    public UserFollowerCountDTO getFollowerCount(Integer userId){

        if (userId == null || userId <= 0){
            throw new BadRequestException("User ID: " + userId + " is invalid.");
        }

        if (!repository.existId(userId)){
            throw new BadRequestException("User ID: " + userId + " doesn't exist.");
        }

        long followerCount = repository.getUsers().stream()
                .filter(user -> user.getFollowedIds() != null && user.getFollowedIds().contains(userId))
                .count();

        User user = repository.getUserById(userId);

        return new UserFollowerCountDTO(userId, user.getUserName(), (int) followerCount);
    }


    @Override
    public UserOkDTO followASpecificUserById(Integer userId, Integer userIdToFollow) {
        if (userIdToFollow == null ||userId == null || userIdToFollow < 0 || userId < 0 || userId.equals(userIdToFollow)) {
            throw new BadRequestException("Invalid IDs");
        }
        User user = repository.getUserById(userId);

        User userToFollow = repository.getUserById(userIdToFollow);

        if (userToFollow.getPosts() == null || userToFollow.getPosts().isEmpty()){
            throw new BadRequestException("User to follow is not a seller");
        }

        if (user.getFollowedIds().contains(userToFollow)){
            throw new BadRequestException("User ID: " + userId + " already follows User ID: " + userToFollow);
        }

        if (user == null) {
            throw new BadRequestException("User ID: " + userId + " doesn't exist.");
        }

        user.addFollowedId(userIdToFollow);

        repository.updateUser(user);

        return new UserOkDTO("OK");

    }

    @Override
    public UserOkDTO unfollowASpecificUserById(Integer userId, Integer userIdToUnfollow) {
        if (userId == null || userId <= 0 || !repository.existId(userId) ){
            throw new BadRequestException("Invalid User ID: " +userId);
        }

        if (userId.equals(userIdToUnfollow)){
            throw new BadRequestException("Invalid User and User ID to unfollow");
        }

        if (userIdToUnfollow == null || userIdToUnfollow <= 0 || !repository.existId(userIdToUnfollow)){
            throw new BadRequestException("Invalid User to Unfollow ID: " +  userIdToUnfollow);
        }

        User user = repository.getUserById(userId);

        if (!user.getFollowedIds().contains(userIdToUnfollow)){
            throw new BadRequestException("User ID: " + userId + " does not follow User ID: " + userIdToUnfollow);
        }

        user.removeFollowedId(userIdToUnfollow);

        repository.updateUser(user);

        return new UserOkDTO("OK");
    }

}
