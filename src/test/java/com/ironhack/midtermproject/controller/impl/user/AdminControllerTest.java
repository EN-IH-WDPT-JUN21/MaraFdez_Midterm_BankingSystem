package com.ironhack.midtermproject.controller.impl.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.dao.users.Admin;
import com.ironhack.midtermproject.dao.users.Role;
import com.ironhack.midtermproject.dao.users.User;
import com.ironhack.midtermproject.repository.account.RoleRepository;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
import com.ironhack.midtermproject.repository.user.AdminRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.utils.Address;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AdminControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        AccountHolder accountHolder1 = new AccountHolder();
        accountHolder1.setName("Ilaria Pino");
        accountHolder1.setBirthDate(LocalDate.of(1987, 1, 1));
        accountHolder1.setUsername("ilarpin");
        accountHolder1.setPassword("$2a$10$uo.9jL9T3DTtC3egkQsDbezCqWb2pke8Py/JUh810DKEyCw1iPC.O"); //123
        accountHolder1.setPrimaryAddress(new Address("Calle Alegría", 8, "Alicante", "Spain", 03003));
        accountHolderRepository.save(accountHolder1);

        Admin admin = new Admin("Leire Beckham", "admin", "$2a$10$oCf3zdqkKcO1njLFm.yvu.n2qiwII5ghF77xhfO8zJ4UMJEaTax1a"); //admin
        Admin admin2 = new Admin("Rosa García", "admin2", "$2a$10$oCf3zdqkKcO1njLFm.yvu.n2qiwII5ghF77xhfO8zJ4UMJEaTax1a"); //admin
        adminRepository.save(admin);
        adminRepository.save(admin2);

        userRepository.saveAll(List.of(accountHolder1, admin, admin2));

        Role role = new Role("ACCOUNTHOLDER", accountHolder1);
        Role role1 = new Role("ADMIN", admin);
        Role role2 = new Role("ADMIN", admin2);
        roleRepository.saveAll(List.of(role, role1, role2));
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getAllAdmins_ReturnAllAdmins() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Leire Beckham"));
        assertTrue(result.getResponse().getContentAsString().contains("admin2"));
    }

    @Test
    void getAllAdmins_NotAuthorisedUser_ThrowsException() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("ilarpin", "123")))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
    }

    @Test
    void getAdmin_ReturnCorrectUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/" + adminRepository.findAll().get(0).getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Leire Beckham"));
        assertFalse(result.getResponse().getContentAsString().contains("Rosa García"));
    }

    @Test
    void getUser_AdminDoesNotExist_ReturnNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/10000")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals("User not found", result.getResponse().getErrorMessage());
    }

}