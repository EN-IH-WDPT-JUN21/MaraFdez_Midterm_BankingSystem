package com.ironhack.midtermproject.controller.impl.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.dao.account.StudentChecking;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.dao.users.Admin;
import com.ironhack.midtermproject.dao.users.Role;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.account.RoleRepository;
import com.ironhack.midtermproject.repository.account.StudentCheckingRepository;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
import com.ironhack.midtermproject.repository.user.AdminRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.utils.Address;
import com.ironhack.midtermproject.utils.Money;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class StudentCheckingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

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

        AccountHolder primaryOwner = new AccountHolder();
        primaryOwner.setName("Úrsula Corveró");
        primaryOwner.setBirthDate(LocalDate.of(1987, 1, 1));
        primaryOwner.setUsername("ursulacorv");
        primaryOwner.setPassword("$2a$10$uo.9jL9T3DTtC3egkQsDbezCqWb2pke8Py/JUh810DKEyCw1iPC.O"); //123
        primaryOwner.setPrimaryAddress(new Address("Calle Alegría", 8, "Alicante", "Spain", 03003));
        accountHolderRepository.save(primaryOwner);

        AccountHolder secondaryOwner = new AccountHolder();
        secondaryOwner.setName("Alyssa Thompson");
        secondaryOwner.setBirthDate(LocalDate.of(1999, 10, 13));
        secondaryOwner.setUsername("alysth");
        secondaryOwner.setPassword("$2a$10$uo.9jL9T3DTtC3egkQsDbezCqWb2pke8Py/JUh810DKEyCw1iPC.O"); //123
        secondaryOwner.setPrimaryAddress(new Address("This Street", 1, "New Scotland", "Canada", 3145));
        accountHolderRepository.save(secondaryOwner);

        userRepository.saveAll(List.of(primaryOwner, secondaryOwner));

        Admin admin = new Admin("Leire Beckham", "admin", "$2a$10$oCf3zdqkKcO1njLFm.yvu.n2qiwII5ghF77xhfO8zJ4UMJEaTax1a"); //admin
        adminRepository.save(admin);

        Role role1 = new Role("ACCOUNTHOLDER", primaryOwner);
        Role role2 = new Role("ACCOUNTHOLDER", secondaryOwner);
        Role role3 = new Role("ADMIN", admin);
        roleRepository.saveAll(List.of(role1, role2, role3));

        StudentChecking studentChecking = new StudentChecking(new Money(new BigDecimal(1200)), primaryOwner, secondaryOwner,
                "$2a$10$DkwjOXnfy6u2CFEy5f5jJOxsmPXN6SegIEHWQ94uWN0aV4iSwzBzq"); //secretkey
        studentCheckingRepository.save(studentChecking);

        StudentChecking studentChecking2 = new StudentChecking(new Money(new BigDecimal(10000)), secondaryOwner, primaryOwner,
                "$2a$10$DkwjOXnfy6u2CFEy5f5jJOxsmPXN6SegIEHWQ94uWN0aV4iSwzBzq"); //secretkey
        studentCheckingRepository.save(studentChecking2);

    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        adminRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getAllStudentCheckingAccounts_ReturnAllAccounts() throws Exception {
        MvcResult result = mockMvc.perform(get("/student")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("1200"));
        assertTrue(result.getResponse().getContentAsString().contains("10000"));
    }

    @Test
    void getStudentCheckingAccount_ReturnCorrectAccount() throws Exception {
        MvcResult result = mockMvc.perform(get("/student/" +
                accountRepository.findByPrimaryOwnerId(
                        accountHolderRepository.findByName("Úrsula Corveró").get().getId()
                ).get().getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("1200"));
        assertFalse(result.getResponse().getContentAsString().contains("10000"));
    }

    @Test
    void getStudentCheckingAccount_AccountDoesNotExist_ThrowsException() throws Exception {
        MvcResult result = mockMvc.perform(get("/student/1000")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals("Account not found", result.getResponse().getErrorMessage());
    }

    @Test
    void deleteStudentCheckingAccount_SuccessfulRemoval() throws Exception {
        List<StudentChecking> savingsBefore = studentCheckingRepository.findAll();

        mockMvc.perform(delete("/student/" + studentCheckingRepository.findAll().get(0).getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(savingsBefore.size() -1, studentCheckingRepository.findAll().size());
    }
}