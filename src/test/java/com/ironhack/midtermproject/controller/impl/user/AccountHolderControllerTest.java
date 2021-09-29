package com.ironhack.midtermproject.controller.impl.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.midtermproject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermproject.controller.dto.ThirdPartyDTO;
import com.ironhack.midtermproject.dao.users.*;
import com.ironhack.midtermproject.repository.account.AccountRepository;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountHolderControllerTest {

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

    @Autowired
    private AccountRepository accountRepository;

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

        AccountHolder accountHolder2 = new AccountHolder();
        accountHolder2.setName("Alyssa Thompson");
        accountHolder2.setBirthDate(LocalDate.of(1999, 10, 13));
        accountHolder2.setUsername("alysth");
        accountHolder2.setPassword("$2a$10$uo.9jL9T3DTtC3egkQsDbezCqWb2pke8Py/JUh810DKEyCw1iPC.O"); //123
        accountHolder2.setPrimaryAddress(new Address("This Street", 1, "New Scotland", "Canada", 3145));
        accountHolderRepository.save(accountHolder2);

        userRepository.saveAll(List.of(accountHolder1, accountHolder2));

        Admin admin = new Admin("Leire Beckham", "admin", "$2a$10$oCf3zdqkKcO1njLFm.yvu.n2qiwII5ghF77xhfO8zJ4UMJEaTax1a"); //admin
        adminRepository.save(admin);

        Role role1 = new Role("ACCOUNTHOLDER", accountHolder1);
        Role role2 = new Role("ACCOUNTHOLDER", accountHolder2);
        Role role3 = new Role("ADMIN", admin);
        roleRepository.saveAll(List.of(role1, role2, role3));

    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
        accountHolderRepository.deleteAll();
        adminRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getAllAccountHolders_ReturnAllUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/accountHolder")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Ilaria Pino"));
        assertTrue(result.getResponse().getContentAsString().contains("Alyssa Thompson"));
    }

    @Test
    void getAccountHolder_ReturnCorrectUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/accountHolder/" + userRepository.findAll().get(1).getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Alyssa Thompson"));
        assertFalse(result.getResponse().getContentAsString().contains("Ilaria Pino"));
    }

    @Test
    void getAccountHolder_UserDoesNotExist_ReturnNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/accountHolder/100000")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals("User not found", result.getResponse().getErrorMessage());
    }

    @Test
    void addAccountHolder_ValidAccountHolder_Created() throws Exception {
        Address address = new Address("Via Zara", 19, "Palermo", "Italy", 90133);
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("Elia Rizzoli", "rizzel",
                "$2a$10$uo.9jL9T3DTtC3egkQsDbezCqWb2pke8Py/JUh810DKEyCw1iPC.O",
                LocalDate.of(1987, 2, 21), address, null);

        // Enable the use of LocalDate in Jackson annotations
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String body = objectMapper.writeValueAsString(accountHolderDTO);

        List<AccountHolder> accountHolderListBefore = accountHolderRepository.findAll();

        MvcResult result = mockMvc.perform(post("/accountHolder")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(accountHolderRepository.findAll().size(), accountHolderListBefore.size() + 1);
    }

    @Test
    void addAccountHolder_NotValidBody_ThrowsException() throws Exception {
        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("Oliver Twist", "$2a$10$VewYqx2ufcKYXkx/lOljF.KNV0r3K4Ol0dpsHmlQb3k.Jb6vylTnu");

        String body = objectMapper.writeValueAsString(thirdPartyDTO);

        MvcResult result = mockMvc.perform(post("/accountHolder")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void addAccountHolder_NotAuthorisedUser_ThrowsException() throws Exception {
        Address address = new Address("Via Zara", 19, "Palermo", "Italy", 90133);
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("Elia Rizzoli", "rizzel",
                "$2a$10$uo.9jL9T3DTtC3egkQsDbezCqWb2pke8Py/JUh810DKEyCw1iPC.O",
                LocalDate.of(1987, 2, 21), address, null);

        // Enable the use of LocalDate in Jackson annotations
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String body = objectMapper.writeValueAsString(accountHolderDTO);

        MvcResult result = mockMvc.perform(post("/accountHolder")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("ilarpin", "123")))
                .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }

    @Test
    void deleteAccountHolder_SuccessfulRemoval() throws Exception {
        List<AccountHolder> accountHolderListBefore = accountHolderRepository.findAll();

        mockMvc.perform(delete("/accountHolder/" + accountHolderRepository.findAll().get(0).getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(accountHolderListBefore.size() -1, accountHolderRepository.findAll().size());
    }

}