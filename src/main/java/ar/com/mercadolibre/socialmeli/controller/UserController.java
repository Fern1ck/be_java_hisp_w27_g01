package ar.com.mercadolibre.socialmeli.controller;

import ar.com.mercadolibre.socialmeli.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/users/test")
    public ResponseEntity<?> test(){
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<?> getFollowersCount(@PathVariable Integer userId){
        return new ResponseEntity<>(userService.getFollowerCount(userId), HttpStatus.OK);
    }
    
    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<?> followASpecificUserById(@PathVariable Integer userId, @PathVariable Integer userIdToFollow){
        return new ResponseEntity<>(userService.followASpecificUserById(userId, userIdToFollow), HttpStatus.OK);
    }
}
