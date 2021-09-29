package com.ironhack.midtermproject.controller.impl.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.midtermproject.controller.dto.CheckingDTO;
import com.ironhack.midtermproject.controller.dto.SavingsDTO;
import com.ironhack.midtermproject.dao.account.CreditCard;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.dao.users.Admin;
import com.ironhack.midtermproject.dao.users.Role;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.account.RoleRepository;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
import com.ironhack.midtermproject.repository.user.AdminRepository;
import com.ironhack.midtermproject.repository.user.CreditCardRepository;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CreditCardControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

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

        CreditCard creditCard = new CreditCard(new Money(new BigDecimal(1200)), primaryOwner, secondaryOwner);
        creditCard.setCreationDate(LocalDate.of(2021, 4, 28));
        creditCard.setLastInterestApplicationDate(LocalDate.of(2021, 4, 28));
        creditCardRepository.save(creditCard);

        CreditCard creditCard2 = new CreditCard(new Money(new BigDecimal(10000)), secondaryOwner, primaryOwner);
        creditCard2.setLastInterestApplicationDate(LocalDate.of(2021, 9, 10));
        creditCardRepository.save(creditCard2);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        adminRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getAllCreditCardAccount_ReturnAllAccounts() throws Exception {
        MvcResult result = mockMvc.perform(get("/credit")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("1200"));
        assertTrue(result.getResponse().getContentAsString().contains("10000"));
    }

    @Test
    void getCreditCardAccount_ReturnCorrectAccount_AddInterestIfLastInterestApplicationDateIsGreaterThanAMonth() throws Exception {
        CreditCard creditCardBefore = creditCardRepository.findAll().get(0);
        BigDecimal balanceAmountBefore = creditCardBefore.getBalance().getAmount();
        LocalDate lastInterestApplicationDate = creditCardBefore.getLastInterestApplicationDate();

        MvcResult result = mockMvc.perform(get("/credit/" + creditCardBefore.getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        CreditCard creditCardAfter = creditCardRepository.findAll().get(0);

        assertTrue(result.getResponse().getContentAsString().contains("2021-04-28"));
        assertEquals(LocalDate.now(), creditCardAfter.getLastInterestApplicationDate());
        assertEquals(creditCardAfter.getBalance().getAmount(),
                balanceAmountBefore.add(balanceAmountBefore.multiply(creditCardAfter.getInterestRate())
                        .multiply(new BigDecimal(creditCardAfter.getLastInterestApplicationDate().getMonthValue()
                                - lastInterestApplicationDate.getMonthValue()))).setScale(2, RoundingMode.FLOOR));
    }

    @Test
    void getSavingsAccount_ReturnCorrectAccount_InterestNotAddedIfLastInterestApplicationDateIsLessThanAMonth() throws Exception {
        BigDecimal balanceAmountBefore = creditCardRepository.findAll().get(1).getBalance().getAmount();

        MvcResult result = mockMvc.perform(get("/credit/" + creditCardRepository.findAll().get(1).getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(balanceAmountBefore, creditCardRepository.findAll().get(1).getBalance().getAmount());
        assertEquals(LocalDate.of(2021, 9, 10),
                creditCardRepository.findAll().get(1).getLastInterestApplicationDate());

    }

    @Test
    void createNewAccount_ValidAccount_Created() throws Exception {
        CheckingDTO savingsDTO = new CheckingDTO(new Money(new BigDecimal(77000)),
                accountHolderRepository.findByName("Úrsula Corveró").get().getId(), null,
                "$2a$10$uo.9jL9T3DTtC3egkQsDbezCqWb2pke8Py/JUh810DKEyCw1iPC.O");

        String body = objectMapper.writeValueAsString(savingsDTO);

        int checkingRepositorySizeBefore = creditCardRepository.findAll().size();

        MvcResult result = mockMvc.perform(post("/credit/")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("77000"));
        assertEquals(checkingRepositorySizeBefore + 1, creditCardRepository.findAll().size());
    }

    @Test
    void createNewAccount_NotValidBody_ThrowsException() throws Exception {
        SavingsDTO savingsDTO = new SavingsDTO(new Money(new BigDecimal(77000)),
                accountHolderRepository.findByName("Úrsula Corveró").get().getId(), null,
                "$2a$10$uo.9jL9T3DTtC3egkQsDbezCqWb2pke8Py/JUh810DKEyCw1iPC.O");

        // Enable the use of LocalDate in Jackson annotations
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String body = objectMapper.writeValueAsString(savingsDTO);

        MvcResult result = mockMvc.perform(post("/credit/")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void createNewAccount_NotAuthorisedUser_ThrowsException() throws Exception {
        SavingsDTO savingsDTO = new SavingsDTO(new Money(new BigDecimal(77000)),
                accountHolderRepository.findByName("Úrsula Corveró").get().getId(), null,
                "$2a$10$uo.9jL9T3DTtC3egkQsDbezCqWb2pke8Py/JUh810DKEyCw1iPC.O");

        String body = objectMapper.writeValueAsString(savingsDTO);

        MvcResult result = mockMvc.perform(post("/credit/")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("ursulacorv", "123")))
                .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());

    }

    @Test
    void deleteCreditCardAccount_SuccessfulRemoval() throws Exception {
        List<CreditCard> creditCardsBefore = creditCardRepository.findAll();

        mockMvc.perform(delete("/credit/" + creditCardRepository.findAll().get(0).getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(creditCardsBefore.size() -1, creditCardRepository.findAll().size());
    }

}