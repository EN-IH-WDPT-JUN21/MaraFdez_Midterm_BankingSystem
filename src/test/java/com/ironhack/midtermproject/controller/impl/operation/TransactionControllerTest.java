package com.ironhack.midtermproject.controller.impl.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.midtermproject.controller.dto.TransactionDTO;
import com.ironhack.midtermproject.dao.operation.Transaction;
import com.ironhack.midtermproject.dao.account.Checking;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.dao.users.Admin;
import com.ironhack.midtermproject.dao.users.Role;
import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.repository.operation.TransactionRepository;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.account.CheckingRepository;
import com.ironhack.midtermproject.repository.account.RoleRepository;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TransactionControllerTest {

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

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private TransactionRepository transactionRepository;

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

        Checking checking = new Checking();
        checking.setBalance(new Money(new BigDecimal(1200)));
        checking.setPrimaryOwner(accountHolder1);
        checking.setStatus(Status.ACTIVE);
        checking.setSecretKey("$2a$10$DkwjOXnfy6u2CFEy5f5jJOxsmPXN6SegIEHWQ94uWN0aV4iSwzBzq"); //secretkey
        checkingRepository.save(checking);

        Checking checking2 = new Checking();
        checking2.setBalance(new Money(new BigDecimal(10000)));
        checking2.setPrimaryOwner(accountHolder2);
        checking.setStatus(Status.ACTIVE);
        checking2.setSecretKey("$2a$10$DkwjOXnfy6u2CFEy5f5jJOxsmPXN6SegIEHWQ94uWN0aV4iSwzBzq"); //secretkey
        checkingRepository.save(checking2);

        Transaction transaction = new Transaction(checking2.getId(), checking.getId(), checking2.getPrimaryOwner().getName(),
                new Money(new BigDecimal(50)));
        Transaction transaction2 = new Transaction(checking.getId(), checking2.getId(), checking.getPrimaryOwner().getName(),
                new Money(new BigDecimal(150)));
        transaction.setTransactionDate(LocalDateTime.of(2021, 8, 30, 4,30,00,00000));
        transactionRepository.saveAll(List.of(transaction, transaction2));

    }

    @AfterEach
    void tearDown() {
        checkingRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        accountHolderRepository.deleteAll();
        adminRepository.deleteAll();
        roleRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    void getAllTransactions_ReturnAllTransactions() throws Exception {
        MvcResult result = mockMvc.perform(get("/transaction")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("50"));
        assertTrue(result.getResponse().getContentAsString().contains("Alyssa Thompson"));
    }

    @Test
    void getAllTransactions_NotAuthorisedUser_ThrowsException() throws Exception {
        MvcResult result = mockMvc.perform(get("/transaction")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("ilarpin", "123")))
                .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }

    @Test
    void getTransaction_ReturnsTransaction() throws Exception {
        MvcResult result = mockMvc.perform(get("/transaction/" + transactionRepository.findAll().get(0).getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("50"));
    }

    // Unfortunately, H2 does not support the interval data type, so INTERVAL 1 DAY is not understood
    // and throws an error. This makes createTransaction() untestable.
 /*   @Test
    void createTransaction_SuccessfullyProcessed() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(
                accountRepository.findByPrimaryOwnerId(accountHolderRepository
                        .findByName("Ilaria Pino").get().getId()).get().getId(),
                accountRepository.findByPrimaryOwnerId(accountHolderRepository
                        .findByName("Alyssa Thompson").get().getId()).get().getId(),
                "Alyssa Thompson", new Money(new BigDecimal(350)));

        // Enable the use of LocalDate in Jackson annotations
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String body = objectMapper.writeValueAsString(transactionDTO);

        List<Transaction> transactionList = transactionRepository.findAll();
        BigDecimal balanceAmountProvenanceAccountBefore = accountRepository.findByPrimaryOwnerId(accountHolderRepository
                .findByName("Ilaria Pino").get().getId()).get().getBalance().getAmount();
        BigDecimal balanceAmountDestinationAccountBefore = accountRepository.findByPrimaryOwnerId(accountHolderRepository
                .findByName("Alyssa Thompson").get().getId()).get().getBalance().getAmount();

        MvcResult result = mockMvc.perform(post("/newTransaction")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("ilarpin", "123")))
                .andExpect(status().isAccepted())
                .andReturn();


        assertEquals(transactionList.size() + 1, transactionRepository.findAll().size());
        assertEquals(accountRepository.findByPrimaryOwnerId(accountHolderRepository
                        .findByName("Ilaria Pino").get().getId()).get().getBalance().getAmount(),
                balanceAmountProvenanceAccountBefore.subtract(transactionDTO.getTransaction().getAmount()));
        assertEquals(accountRepository.findByPrimaryOwnerId(accountHolderRepository
                        .findByName("Alyssa Thompson").get().getId()).get().getBalance().getAmount(),
                balanceAmountDestinationAccountBefore.add(transactionDTO.getTransaction().getAmount()));
    }

    @Test
    void createTransaction_InsufficientFunds_ThrowsException() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(
                accountRepository.findByPrimaryOwnerId(accountHolderRepository
                        .findByName("Ilaria Pino").get().getId()).get().getId(),
                accountRepository.findByPrimaryOwnerId(accountHolderRepository
                        .findByName("Alyssa Thompson").get().getId()).get().getId(),
                "Alyssa Thompson", new Money(new BigDecimal(35000)));

        // Enable the use of LocalDate in Jackson annotations
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String body = objectMapper.writeValueAsString(transactionDTO);

        MvcResult result = mockMvc.perform(post("/newTransaction")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("ilarpin", "123")))
                .andReturn();


        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
        assertEquals("The provenance account has insufficient funds", result.getResponse().getErrorMessage());
    }*/

    @Test
    void createTransaction_NotAuthorisedUser_ThrowsException() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(
                accountRepository.findByPrimaryOwnerId(accountHolderRepository
                        .findByName("Ilaria Pino").get().getId()).get().getId(),
                accountRepository.findByPrimaryOwnerId(accountHolderRepository
                        .findByName("Alyssa Thompson").get().getId()).get().getId(),
                "Alyssa Thompson", new Money(new BigDecimal(350)));

        // Enable the use of LocalDate in Jackson annotations
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String body = objectMapper.writeValueAsString(transactionDTO);

        MvcResult result = mockMvc.perform(post("/newTransaction")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }

}