package my.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerIntegrationTest {

    private MockMvc mockMvc;
    private final WebApplicationContext context;
    private final ObjectMapper mapper;

    @Autowired
    public UserControllerIntegrationTest(WebApplicationContext context, ObjectMapper mapper) {
        this.context = context;
        this.mapper = mapper;
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void test_checkNameAvailability_returnsNoContent_givenProvidedUsernameNotTaken() throws Exception {
        mockMvc.perform(get("/user/test6"))
                .andDo(print())
                .andExpect(status().is(204))
                .andReturn();
    }

    @Test
    public void test_checkNameAvailability_returnsConflict_givenProvidedUsernameTaken() throws Exception {
        mockMvc.perform(get("/user/test1"))
                .andDo(print())
                .andExpect(status().is(409))
                .andReturn();
    }

}
