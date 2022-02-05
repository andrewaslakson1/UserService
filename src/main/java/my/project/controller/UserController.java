package my.project.controller;

import my.project.entity.dtos.requests.UpdateUserRequest;
import my.project.entity.dtos.responses.UserResponse;
import my.project.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{username}")
    public ResponseEntity<Void> checkNameAvailability(@PathVariable String username) {

        return userService.checkUsernameAvailability(username)
                ?
                ResponseEntity.status(HttpStatus.CONFLICT).build()
                :
                ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PostMapping("/create/{username}")
    public ResponseEntity<UserResponse> addUser(@PathVariable String username) {

        return null;
    }

    @GetMapping("/{userID}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userID) {

        return ResponseEntity.ok(
                userService.getUserById(Long.valueOf(userID))
        );

    }

    @PatchMapping("/edit")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest req) {

        return ResponseEntity.ok(userService.updateUser(req));

    }

    @DeleteMapping("/{userID}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userID) {

        userService.deleteUser(Long.valueOf(userID));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
