package my.project.web;

import my.project.entity.User;
import my.project.entity.dtos.UpdateUserRequest;
import my.project.entity.dtos.UserResponse;
import my.project.util.exception.exceptions.DuplicateUsernameException;
import my.project.util.exception.exceptions.UserNotFoundException;

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

        if (foundUser.isEmpty())
            throw new UserNotFoundException();

        return new UserResponse(foundUser.get());
    }

    public boolean checkUsernameAvailability(String username) {
        return checkNameAvail.test(username);
    }

    public UserResponse addUser(String username) {

        if (checkNameAvail.test(username))
            throw new DuplicateUsernameException();

        return new UserResponse(
                userRepo.save(
                        new User(username)
                )
        );
    }

    public UserResponse updateUser(UpdateUserRequest req) {

        if (userRepo.findById(req.getUserID()).isEmpty())
            throw new UserNotFoundException();

        if (checkNameAvail.test(req.getUsername()))
            throw new DuplicateUsernameException();

        return new UserResponse(
                userRepo.save(req.get())
        );

    }

    public void deleteUser(Long userID) {

        if (userRepo.findById(userID).isEmpty())
            throw new UserNotFoundException();

        userRepo.deleteById(userID);

    }

}
