package my.project.controller;

import my.project.entity.dtos.requests.UpdateUserRequest;
import my.project.entity.dtos.responses.UserResponse;
import my.project.metrics.CollectMetrics;
import my.project.metrics.ControllerEndpoints;
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
    @CollectMetrics(endPoint = ControllerEndpoints.CHECK_USERNAME_AVAILABILITY)
    public ResponseEntity<Void> checkNameAvailability(@PathVariable String username) {

        return userService.checkUsernameAvailability(username)
                ?
                ResponseEntity.status(HttpStatus.CONFLICT).build()
                :
                ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PostMapping("/create/{username}")
    @CollectMetrics(endPoint = ControllerEndpoints.ADD_USER)
    public ResponseEntity<UserResponse> addUser(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(username));
    }

    @GetMapping("/{userID}")
    @CollectMetrics(endPoint = ControllerEndpoints.GET_USER)
    public ResponseEntity<UserResponse> getUser(@PathVariable String userID) {

        return ResponseEntity.ok(
                userService.getUserById(Long.valueOf(userID))
        );

    }

    @PatchMapping("/edit")
    @CollectMetrics(endPoint = ControllerEndpoints.UPDATE_USER)
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest req) {

        return ResponseEntity.ok(userService.updateUser(req));

    }

    @DeleteMapping("/{userID}")
    @CollectMetrics(endPoint = ControllerEndpoints.DELETE_USER)
    public ResponseEntity<Void> deleteUser(@PathVariable String userID) {

        userService.deleteUser(Long.valueOf(userID));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
