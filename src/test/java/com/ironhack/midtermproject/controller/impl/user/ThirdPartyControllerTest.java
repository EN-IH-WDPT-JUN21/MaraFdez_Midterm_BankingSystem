package com.ironhack.midtermproject.controller.impl.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.midtermproject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermproject.controller.dto.TPartyTransactionDTO;
import com.ironhack.midtermproject.controller.dto.ThirdPartyDTO;
import com.ironhack.midtermproject.dao.account.Checking;
import com.ironhack.midtermproject.dao.users.*;
import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.enums.TransactionType;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.account.CheckingRepository;
import com.ironhack.midtermproject.repository.account.RoleRepository;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
import com.ironhack.midtermproject.repository.user.AdminRepository;
import com.ironhack.midtermproject.repository.user.ThirdPartyRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.utils.Address;
import com.ironhack.midtermproject.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ThirdPartyControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CheckingRepository checkingRepository;

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

        ThirdParty thirdParty = new ThirdParty("Anna Wintour",
                "$2a$10$VewYqx2ufcKYXkx/lOljF.KNV0r3K4Ol0dpsHmlQb3k.Jb6vylTnu"); //hashedkey
        ThirdParty thirdParty2 = new ThirdParty("Linda Evangelista",
                "$2a$10$VewYqx2ufcKYXkx/lOljF.KNV0r3K4Ol0dpsHmlQb3k.Jb6vylTnu"); //hashedkey
        thirdPartyRepository.saveAll(List.of(thirdParty, thirdParty2));

        Admin admin = new Admin("Leire Beckham", "admin",
                "$2a$10$oCf3zdqkKcO1njLFm.yvu.n2qiwII5ghF77xhfO8zJ4UMJEaTax1a"); //admin
        adminRepository.save(admin);

        userRepository.saveAll(List.of(accountHolder1, admin));

        Role role = new Role("ACCOUNTHOLDER", accountHolder1);
        Role role1 = new Role("ADMIN", admin);
        roleRepository.saveAll(List.of(role, role1));

        Checking checking = new Checking();
        checking.setBalance(new Money(new BigDecimal(1200)));
        checking.setPrimaryOwner(accountHolder1);
        checking.setStatus(Status.ACTIVE);
        checking.setSecretKey("$2a$10$DkwjOXnfy6u2CFEy5f5jJOxsmPXN6SegIEHWQ94uWN0aV4iSwzBzq"); //secretkey
        checkingRepository.save(checking);

    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
        accountHolderRepository.deleteAll();
        roleRepository.deleteAll();
        thirdPartyRepository.deleteAll();
    }

    @Test
    void getAllThirdParties() throws Exception {
        MvcResult result = mockMvc.perform(get("/thirdParty")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Anna Wintour"));
        assertTrue(result.getResponse().getContentAsString().contains("Linda Evangelista"));
    }

    @Test
    void getThirdParty_ReturnCorrectUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/thirdParty/" + thirdPartyRepository.findAll().get(0).getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Anna Wintour"));
        assertFalse(result.getResponse().getContentAsString().contains("Linda Evangelista"));
    }

    @Test
    void addThirdParty_ValidThirdParty_Created() throws Exception {
        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("Oliver Twist", "$2a$10$VewYqx2ufcKYXkx/lOljF.KNV0r3K4Ol0dpsHmlQb3k.Jb6vylTnu");

        String body = objectMapper.writeValueAsString(thirdPartyDTO);

        List<ThirdParty> thirdPartiesListBefore = thirdPartyRepository.findAll();

        MvcResult result = mockMvc.perform(post("/thirdParty")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(thirdPartyRepository.findAll().size(), thirdPartiesListBefore.size() + 1);
    }

    @Test
    void addThirdParty_NotAuthorisedUser_ThrowsException() throws Exception {
        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("Oliver Twist", "$2a$10$VewYqx2ufcKYXkx/lOljF.KNV0r3K4Ol0dpsHmlQb3k.Jb6vylTnu");

        // Enable the use of LocalDate in Jackson annotations
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String body = objectMapper.writeValueAsString(thirdPartyDTO);

        MvcResult result = mockMvc.perform(post("/thirdParty")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("ilarpin", "123")))
                .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }

    @Test
    void makeATransaction_Send_SuccessfullyProcessed() throws Exception {
        TPartyTransactionDTO transactionDTO = new TPartyTransactionDTO(new BigDecimal(300),
                checkingRepository.findAll().get(0).getId(), "$2a$10$DkwjOXnfy6u2CFEy5f5jJOxsmPXN6SegIEHWQ94uWN0aV4iSwzBzq",
                TransactionType.SEND);

        String body = objectMapper.writeValueAsString(transactionDTO);

        BigDecimal checkingAccountBalance = checkingRepository.findAll().get(0).getBalance().getAmount();

        MvcResult result = mockMvc.perform(post("/TP/transaction/hashedkey")
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();

        assertEquals(checkingRepository.findAll().get(0).getBalance().getAmount(),
                checkingAccountBalance.add(transactionDTO.getAmount()));
    }

    @Test
    void makeATransaction_Receive_SuccessfullyProcessed() throws Exception {
        TPartyTransactionDTO transactionDTO = new TPartyTransactionDTO(new BigDecimal(300),
                checkingRepository.findAll().get(0).getId(), "$2a$10$DkwjOXnfy6u2CFEy5f5jJOxsmPXN6SegIEHWQ94uWN0aV4iSwzBzq",
                TransactionType.RECEIVE);

        String body = objectMapper.writeValueAsString(transactionDTO);

        BigDecimal checkingAccountBalance = checkingRepository.findAll().get(0).getBalance().getAmount();

        MvcResult result = mockMvc.perform(post("/TP/transaction/hashedkey")
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();

        assertEquals(checkingRepository.findAll().get(0).getBalance().getAmount(),
                checkingAccountBalance.subtract(transactionDTO.getAmount()));
    }

    @Test
    void delete_SuccessfulRemoval() throws Exception {
        List<ThirdParty> thirdPartiesListBefore =thirdPartyRepository.findAll();

        mockMvc.perform(delete("/thirdParty/" + thirdPartyRepository.findAll().get(0).getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(thirdPartiesListBefore.size() -1, thirdPartyRepository.findAll().size());
    }

}