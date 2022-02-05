package my.project.service;

import my.project.entity.User;
import my.project.entity.dtos.requests.UpdateUserRequest;
import my.project.entity.dtos.responses.UserResponse;
import my.project.exception.exceptions.DuplicateUsernameException;
import my.project.exception.exceptions.UserNotFoundException;
import my.project.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Predicate;

@Service
public class UserService {

    private final UserRepository userRepo;

    private final Predicate<String> checkNameAvail;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
        this.checkNameAvail = str -> userRepo.findByUsername(str).isPresent();
    }

    public UserResponse getUserById(Long userID) {

        Optional<User> foundUser = userRepo.findById(userID);

        if (foundUser.isEmpty()) throw new UserNotFoundException();

        return new UserResponse(foundUser.get());
    }

    public boolean checkUsernameAvailability(String username) {
        return checkNameAvail.test(username);
    }

    public UserResponse addUser(String username) {

        if (checkNameAvail.test(username)) throw new DuplicateUsernameException();

        userRepo.save(new User(username));

        return new UserResponse(userRepo.findByUsername(username).get());
    }

    public UserResponse updateUser(UpdateUserRequest req) {

        if (checkNameAvail.test(req.getUsername())) throw new DuplicateUsernameException();

        return new UserResponse(
                userRepo.save(req.get())
        );

    }

    public void deleteUser(Long userID) {

        if (userRepo.findById(userID).isEmpty()) throw new UserNotFoundException();

        userRepo.deleteById(userID);

    }

}
