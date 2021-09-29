package com.ironhack.midtermproject.controller.impl.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.dao.account.Savings;
import com.ironhack.midtermproject.dao.users.Admin;
import com.ironhack.midtermproject.dao.users.Role;
import com.ironhack.midtermproject.dao.users.User;
import com.ironhack.midtermproject.repository.account.RoleRepository;
import com.ironhack.midtermproject.repository.user.AdminRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        User user = new User("Ilaria Pino", "ilarpin",
                "$2a$10$uo.9jL9T3DTtC3egkQsDbezCqWb2pke8Py/JUh810DKEyCw1iPC.O"); //123
        User user2 = new User("Elia Rizzoli", "rizzel",
                "$2a$10$uo.9jL9T3DTtC3egkQsDbezCqWb2pke8Py/JUh810DKEyCw1iPC.O"); //123

        userRepository.saveAll(List.of(user, user2));

        Admin admin = new Admin("Leire Beckham", "admin", "$2a$10$oCf3zdqkKcO1njLFm.yvu.n2qiwII5ghF77xhfO8zJ4UMJEaTax1a"); //admin
        adminRepository.save(admin);

        Role role = new Role("ACCOUNTHOLDER", user);
        Role role1 = new Role("ADMIN", admin);
        roleRepository.saveAll(List.of(role1));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        adminRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getAllUsers_ReturnAllUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/user")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Ilaria Pino"));
        assertTrue(result.getResponse().getContentAsString().contains("Elia Rizzoli"));
    }

    @Test
    void getAllUsers_NotAuthorisedUser_ThrowsException() throws Exception {
        MvcResult result = mockMvc.perform(get("/user")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("ilarpin", "123")))
                .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }

    @Test
    void getAllUsers_NoUserLogged_ThrowsException() throws Exception {
        MvcResult result = mockMvc.perform(get("/user"))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
    }

    @Test
    void getUser_ReturnCorrectUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/user/" + userRepository.findAll().get(0).getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Ilaria Pino"));
        assertFalse(result.getResponse().getContentAsString().contains("Elia Rizzoli"));
    }

    @Test
    void getUser_UserDoesNotExist_ReturnNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/user/10000")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals("User not found", result.getResponse().getErrorMessage());
    }

    @Test
    void deleteUser_SuccessfulRemoval() throws Exception {
        List<User> userListBefore = userRepository.findAll();

        mockMvc.perform(delete("/user/" + userRepository.findAll().get(0).getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(userListBefore.size() -1, userRepository.findAll().size());
    }

}