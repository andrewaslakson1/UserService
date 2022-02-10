package my.project.service;

import my.project.entity.User;
import my.project.entity.dtos.requests.UpdateUserRequest;
import my.project.entity.dtos.responses.UserResponse;
import my.project.exception.exceptions.DuplicateUsernameException;
import my.project.exception.exceptions.UserNotFoundException;
import my.project.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService sut;
    private UserRepository mockUserRepo;

    @BeforeEach
    public void setUp() {
        mockUserRepo = mock(UserRepository.class);
        sut = new UserService(mockUserRepo);
    }

    @AfterEach
    public void tearDown() {
        sut = null;
        mockUserRepo = null;
    }

    @Test
    public void test_getUserById_returnsUserReponse_givenValidId() {
        // Arrange
        Long valid_userID = 1l;
        User foundUser = new User(1l, "valid");
        Optional<User> foundUserOptional = Optional.of(foundUser);

        when(mockUserRepo.findById(valid_userID)).thenReturn(foundUserOptional);

        // Act
        UserResponse actual_result = sut.getUserById(valid_userID);

        // Assert
        verify(mockUserRepo, times(1)).findById(valid_userID);

        Assertions.assertNotNull(actual_result, "Expected actual object returned");

        Assertions.assertEquals(foundUser.getUserID(), actual_result.getUserID(), "Should be the same values");
        Assertions.assertEquals(foundUser.getUsername(), actual_result.getUsername(), "Should be the same values");

    }

    @Test
    public void test_getUserById_throwsUserNotFoundException_givenInvalidId() {
        // Arrange
        Long invalid_userID = 5l;
        Optional<User> foundUserOptional = Optional.empty();

        when(mockUserRepo.findById(invalid_userID)).thenReturn(foundUserOptional);

        // Act
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> sut.getUserById(invalid_userID),
                "Expected Exception to be thrown when empty optional is recieved"
        );

        // Assert
        verify(mockUserRepo, times(1)).findById(invalid_userID);

    }

    @Test
    public void test_checkUsernameAvailability_returnsTrue_givenTakenUsername() {
        // Arrange
        String takenUsername = "taken";
        User foundUser = new User(1l, takenUsername);
        Optional<User> foundUserOptional = Optional.of(foundUser);

        when(mockUserRepo.findByUsername(takenUsername)).thenReturn(foundUserOptional);

        // Act
        boolean actual_result = sut.checkUsernameAvailability(takenUsername);

        // Assert
        Assertions.assertTrue(actual_result, "Expected true when username finds user in database");

    }

    @Test
    public void test_checkUsernameAvailability_returnsFalse_givenNotTakenUsername() {
        // Arrange
        String notTakenUsername = "notTaken";
        Optional<User> foundUserOptional = Optional.empty();

        when(mockUserRepo.findByUsername(notTakenUsername)).thenReturn(foundUserOptional);

        // Act
        boolean actual_result = sut.checkUsernameAvailability(notTakenUsername);

        // Assert
        Assertions.assertFalse(actual_result, "Expected false when username does not find user in database");

    }

    @Test
    public void test_addUser_returnsNewUser_givenValidRequest() {
        // Arrange
        String notTakenUsername = "notTaken";
        Optional<User> foundUserOptional = Optional.empty();

        User savedUser = new User(1l, "notTaken");

        when(mockUserRepo.findByUsername(notTakenUsername))
                .thenReturn(foundUserOptional);

        when(mockUserRepo.save(any()))
                .thenReturn(savedUser);

        // Act
        UserResponse actual_result = sut.addUser(notTakenUsername);

        // Assert
        Assertions.assertNotNull(actual_result, "Expected actual object returned");

        Assertions.assertEquals(savedUser.getUserID(), actual_result.getUserID(), "Should be the same values");
        Assertions.assertEquals(savedUser.getUsername(), actual_result.getUsername(), "Should be the same values");

    }

    @Test
    public void test_addUser_throwsDuplicateUsernameException_givenTakenName() {
        // Arrange
        String takenName = "taken";
        User foundUser = new User(1l, "taken");
        Optional<User> foundUserOptional = Optional.of(foundUser);

        when(mockUserRepo.findByUsername(takenName)).thenReturn(foundUserOptional);

        // Act
        Assertions.assertThrows(
                DuplicateUsernameException.class,
                () -> sut.addUser(takenName),
                "Expected Exception to be thrown when name is taken"
        );

        // Assert
        verify(mockUserRepo, times(1)).findByUsername(takenName);
        verify(mockUserRepo, times(0)).save(any());

    }

    @Test
    public void test_updateUser_returnsValidResponse_givenValidUpdateRequest() {
        // Arrange
        UpdateUserRequest req = new UpdateUserRequest();
        req.setUserID(1l);
        req.setUsername("newUsername");

        User foundUserByID = new User(1l, "oldUsername");
        Optional<User> foundUserByIDOptional = Optional.of(foundUserByID);

        Optional<User> foundUserByNameOptional = Optional.empty();

        User savedUser = req.get();

        when(mockUserRepo.findById(req.getUserID())).thenReturn(foundUserByIDOptional);
        when(mockUserRepo.findByUsername(req.getUsername())).thenReturn(foundUserByNameOptional);
        when(mockUserRepo.save(req.get())).thenReturn(savedUser);

        // Act
        UserResponse actual_result = sut.updateUser(req);

        //Assert
        Assertions.assertNotNull(actual_result, "Should have returned object of UserResponse");

        Assertions.assertEquals(req.getUsername(), actual_result.getUsername(), "Username should have carried through");

        verify(mockUserRepo, times(1)).findById(req.getUserID());
        verify(mockUserRepo, times(1)).findByUsername(req.getUsername());
        verify(mockUserRepo, times(1)).save(req.get());

    }

    @Test
    public void test_updateUser_throwsUserNotFoundException_givenInvalidID() {
        // Arrange
        UpdateUserRequest req = new UpdateUserRequest();
        req.setUserID(6l);
        req.setUsername("newUsername");

        Optional<User> foundUserByIDOptional = Optional.empty();

        Optional<User> foundUserByNameOptional = Optional.empty();

        User savedUser = req.get();

        when(mockUserRepo.findById(req.getUserID())).thenReturn(foundUserByIDOptional);
        when(mockUserRepo.findByUsername(any())).thenReturn(foundUserByNameOptional);
        when(mockUserRepo.save(any())).thenReturn(savedUser);

        // Act
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> sut.updateUser(req),
                "Invalid ID should cause User not found exception"
        );

        //Assert
        verify(mockUserRepo, times(1)).findById(req.getUserID());
        verify(mockUserRepo, times(0)).findByUsername(any());
        verify(mockUserRepo, times(0)).save(any());

    }

    @Test
    public void test_updateUser_throwsDuplicateUsernameException_givenTakenUsername() {
        // Arrange
        UpdateUserRequest req = new UpdateUserRequest();
        req.setUserID(1l);
        req.setUsername("newUsername");

        User foundUserByID = new User(1l, "oldUsername");
        Optional<User> foundUserByIDOptional = Optional.of(foundUserByID);

        User foundUserByName = new User(5l, "newUsername");
        Optional<User> foundUserByNameOptional = Optional.of(foundUserByName);

        User savedUser = req.get();

        when(mockUserRepo.findById(req.getUserID())).thenReturn(foundUserByIDOptional);
        when(mockUserRepo.findByUsername(req.getUsername())).thenReturn(foundUserByNameOptional);
        when(mockUserRepo.save(any())).thenReturn(savedUser);

        // Act
        Assertions.assertThrows(
                DuplicateUsernameException.class,
                () -> sut.updateUser(req),
                "Taken Username should cause DuplicateUsernameException"
        );

        //Assert
        verify(mockUserRepo, times(1)).findById(req.getUserID());
        verify(mockUserRepo, times(1)).findByUsername(req.getUsername());
        verify(mockUserRepo, times(0)).save(any());

    }

    @Test
    public void test_deleteUser_finishesSuccessfully_givenValidID() {
        // Arrange
        Long valid_userID = 1l;

        User foundUser = new User(1l, "valid");
        Optional<User> foundUserOptional = Optional.of(foundUser);

        when(mockUserRepo.findById(valid_userID)).thenReturn(foundUserOptional);
        doNothing().when(mockUserRepo).deleteById(valid_userID);

        // Act
        sut.deleteUser(valid_userID);

        //Assert
        verify(mockUserRepo, times(1)).findById(valid_userID);
        verify(mockUserRepo, times(1)).deleteById(valid_userID);

    }

    @Test
    public void test_deleteUser_throwsUserNotFoundException_givenInvalidID() {
        // Arrange
        Long invalid_userID = 6l;

        Optional<User> foundUserOptional = Optional.empty();

        when(mockUserRepo.findById(invalid_userID)).thenReturn(foundUserOptional);
        doNothing().when(mockUserRepo).deleteById(any());

        // Act
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> sut.deleteUser(invalid_userID),
                "Invalid ID should throw UserNotFoundException"
        );

        //Assert
        verify(mockUserRepo, times(1)).findById(invalid_userID);
        verify(mockUserRepo, times(0)).deleteById(any());

    }

}
