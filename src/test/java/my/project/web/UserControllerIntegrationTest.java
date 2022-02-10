package my.project.web;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.project.entity.User;
import my.project.entity.dtos.UpdateUserRequest;
import my.project.entity.dtos.UserResponse;
import my.project.util.exception.ExceptionResponse;
import my.project.web.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerIntegrationTest {

    private MockMvc mockMvc;
    private UserRepository userRepo;

    private final WebApplicationContext context;
    private final ObjectMapper mapper;

    @Autowired
    public UserControllerIntegrationTest(WebApplicationContext context, ObjectMapper mapper, UserRepository userRepo) {
        this.context = context;
        this.mapper = mapper;
        this.userRepo = userRepo;
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void cleanUp() {
        this.mockMvc = null;
    }

    @Test
    public void test_checkNameAvailability_returnsNoContent_givenProvidedUsernameNotTaken() throws Exception {
        mockMvc
                .perform(get("/user/test6"))
                .andDo(print())
                .andExpect(status().is(204))
                .andReturn();
    }

    @Test
    public void test_checkNameAvailability_returnsConflict_givenProvidedUsernameTaken() throws Exception {
        mockMvc
                .perform(get("/user/test1"))
                .andDo(print())
                .andExpect(status().is(409))
                .andReturn();
    }

    @Test
    public void test_getUser_returns200_givenValidId() throws Exception {
        // Arrange
        String validId = "1";

        // Act
        MvcResult result = mockMvc
                .perform(get("/user/id/" + validId))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        UserResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponse.class
        );

        // Assert
        Assertions.assertEquals("application/json", result.getResponse().getContentType(), "Expected return to have JSON body");

        Assertions.assertEquals(response.getUserID(), Long.valueOf(validId), "expected id of return to match id of request");
        Assertions.assertEquals(response.getUsername(), "test1", "expected name of test1");

    }

    @Test
    public void test_getUser_returns404_givenInvalidId() throws Exception {
        // Arrange
        String invalidId = "365";

        // Act
        MvcResult result = mockMvc
                .perform(get("/user/id/" + invalidId))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();

        ExceptionResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                ExceptionResponse.class
        );

        // Assert
        Assertions.assertEquals("application/json", result.getResponse().getContentType(), "Expected return to have JSON body");

        Assertions.assertEquals(response.getStatusCode(), 404, "expected status code of 404");
        Assertions.assertEquals(response.getException(), "UserNotFoundException", "expected reason for error to be UserNotFoundException");

    }

    @Test
    public void test_addUser_returns201_givenValidName() throws Exception {
        // Arrange
        String newUsername = "test_create_1";

        // Act
        MvcResult result = mockMvc
                .perform(post("/user/create/" + newUsername))
                .andDo(print())
                .andExpect(status().is(201))
                .andReturn();

        UserResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponse.class
        );

        Optional<User> newUser = userRepo.findByUsername(newUsername);

        // Assert
        Assertions.assertEquals("application/json", result.getResponse().getContentType(), "Expected return to have JSON body");

        Assertions.assertTrue(newUser.isPresent(), "Expected new user to be in database");

        Assertions.assertEquals(newUsername, response.getUsername(), "Expected response to contain new username");

    }

    @Test
    public void test_addUser_returns409_givenDuplicateUsername() throws Exception {
        // Arrange
        String newUsername = "test1";

        // Act
        MvcResult result = mockMvc
                .perform(post("/user/create/" + newUsername))
                .andDo(print())
                .andExpect(status().is(409))
                .andReturn();

        ExceptionResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                ExceptionResponse.class
        );

        // Assert
        Assertions.assertEquals("application/json", result.getResponse().getContentType(), "Expected return to have JSON body");

        Assertions.assertEquals(response.getStatusCode(), 409, "expected status code of 409");
        Assertions.assertEquals(response.getException(), "DuplicateUsernameException", "expected reason for error to be DuplicateUsernameException");

    }

    @Test
    public void test_updateUser_returns200_givenValidUpdateRequest() throws Exception {
        // Arrange
        Long userId = Long.valueOf(2l);
        String newUsername = "test_edit_1";

        UpdateUserRequest request = new UpdateUserRequest();
        request.setUserID(userId);
        request.setUsername(newUsername);

        // Act
        MvcResult result = mockMvc
                .perform(patch("/user/edit")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        UserResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponse.class
        );

        Optional<User> newUser = userRepo.findByUsername(newUsername);

        // Assert
        Assertions.assertEquals("application/json", result.getResponse().getContentType(), "Expected return to have JSON body");

        Assertions.assertTrue(newUser.isPresent(), "Expected new user to be in database with new name");

        Assertions.assertEquals(newUsername, response.getUsername(), "Expected response to contain new username");

    }

    @Test
    public void test_updateUser_returns404_givenInvalidId() throws Exception {
        // Arrange
        Long userId = Long.valueOf(365l);
        String newUsername = "test_edit_fail";

        UpdateUserRequest request = new UpdateUserRequest();
        request.setUserID(userId);
        request.setUsername(newUsername);

        // Act
        MvcResult result = mockMvc
                .perform(patch("/user/edit")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().is(404))
                .andReturn();

        ExceptionResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                ExceptionResponse.class
        );

        Optional<User> newUser = userRepo.findByUsername(newUsername);

        // Assert
        Assertions.assertEquals("application/json", result.getResponse().getContentType(), "Expected return to have JSON body");

        Assertions.assertFalse(newUser.isPresent(), "Expected new username to not be persisted to database");

        Assertions.assertEquals(response.getStatusCode(), 404, "expected status code of 404");
        Assertions.assertEquals(response.getException(), "UserNotFoundException", "expected reason for error to be UserNotFoundException");

    }

    @Test
    public void test_updateUser_returns409_givenDuplicateUsername() throws Exception {
        // Arrange
        Long userId = Long.valueOf(3l);
        String newUsername = "test1";

        UpdateUserRequest request = new UpdateUserRequest();
        request.setUserID(userId);
        request.setUsername(newUsername);

        // Act
        MvcResult result = mockMvc
                .perform(patch("/user/edit")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().is(409))
                .andReturn();

        ExceptionResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                ExceptionResponse.class
        );

        Optional<User> newUser = userRepo.findById(userId);

        // Assert
        Assertions.assertEquals("application/json", result.getResponse().getContentType(), "Expected return to have JSON body");

        Assertions.assertNotEquals(newUser.get().getUsername(), newUsername, "Expected new username to not be persisted to database");

        Assertions.assertEquals(response.getStatusCode(), 409, "expected status code of 409");
        Assertions.assertEquals(response.getException(), "DuplicateUsernameException", "expected reason for error to be DuplicateUsernameException");
    }

    @Test
    public void test_deleteUser_returns204_givenValidId() throws Exception {
        // Arrange
        String validId = "4";

        // Act
        MvcResult result = mockMvc
                .perform(delete("/user/" + validId))
                .andDo(print())
                .andExpect(status().is(204))
                .andReturn();

        Optional<User> deletedUser = userRepo.findById(Long.valueOf(validId));

        // Assert
        Assertions.assertFalse(deletedUser.isPresent(), "expected user to be deleted from database");

    }

    @Test
    public void test_deleteUser_returns404_givenInvalidId() throws Exception {
        // Arrange
        String invalidId = "365";

        // Act
        MvcResult result = mockMvc
                .perform(delete("/user/" + invalidId))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();

        ExceptionResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                ExceptionResponse.class
        );

        // Assert
        Assertions.assertEquals("application/json", result.getResponse().getContentType(), "Expected return to have JSON body");

        Assertions.assertEquals(response.getStatusCode(), 404, "expected status code of 404");
        Assertions.assertEquals(response.getException(), "UserNotFoundException", "expected reason for error to be UserNotFoundException");
    }

}
