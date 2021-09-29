package com.ironhack.midtermproject.controller.impl.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.controller.dto.BalanceDTO;
import com.ironhack.midtermproject.controller.dto.StatusDTO;
import com.ironhack.midtermproject.dao.account.Checking;
import com.ironhack.midtermproject.dao.users.AccountHolder;
import com.ironhack.midtermproject.dao.users.Admin;
import com.ironhack.midtermproject.dao.users.Role;
import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.repository.account.AccountRepository;
import com.ironhack.midtermproject.repository.account.CheckingRepository;
import com.ironhack.midtermproject.repository.account.RoleRepository;
import com.ironhack.midtermproject.repository.user.AccountHolderRepository;
import com.ironhack.midtermproject.repository.user.AdminRepository;
import com.ironhack.midtermproject.repository.user.UserRepository;
import com.ironhack.midtermproject.utils.Address;
import com.ironhack.midtermproject.utils.Constants;
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
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CheckingRepository checkingRepository;

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

        Checking checking = new Checking();
        checking.setBalance(new Money(new BigDecimal(1200)));
        checking.setPrimaryOwner(primaryOwner);
        checking.setSecondaryOwner(secondaryOwner);
        checking.setStatus(Status.ACTIVE);
        checking.setSecretKey("$2a$10$DkwjOXnfy6u2CFEy5f5jJOxsmPXN6SegIEHWQ94uWN0aV4iSwzBzq"); //secretkey
        checkingRepository.save(checking);

        Checking checking2 = new Checking();
        checking2.setBalance(new Money(new BigDecimal(10000)));
        checking2.setPrimaryOwner(secondaryOwner);
        checking2.setSecondaryOwner(primaryOwner);
        checking.setStatus(Status.ACTIVE);
        checking2.setSecretKey("$2a$10$DkwjOXnfy6u2CFEy5f5jJOxsmPXN6SegIEHWQ94uWN0aV4iSwzBzq"); //secretkey
        checkingRepository.save(checking2);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        checkingRepository.deleteAll();
        userRepository.deleteAll();
        adminRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getAllAccounts_ReturnAllAccounts() throws Exception {
        MvcResult result = mockMvc.perform(get("/accounts")
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                    .andExpect(status().isOk())
                    .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("1200"));
        assertTrue(result.getResponse().getContentAsString().contains("10000"));
    }

    @Test
    void getAccount_returnCorrectAccount() throws Exception {
        MvcResult result = mockMvc.perform(get("/accounts/" +
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
    void getAccount_AccountDoesNotExist_ThrowsException() throws Exception {
        MvcResult result = mockMvc.perform(get("/accounts/1000")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals("Account not found", result.getResponse().getErrorMessage());
    }

    @Test
    @WithMockUser(username="ursulacorv", roles={"ACCOUNTHOLDER"})
    void getAccountHolderAccounts_ReturnAccounts() throws Exception {
        MvcResult result = mockMvc.perform(get("/myAccount"))
                .andExpect(status().isOk())
                .andReturn();

        // As a primaryHolder
        assertTrue(result.getResponse().getContentAsString().contains("1200"));
        // As a secondaryHolder
        assertTrue(result.getResponse().getContentAsString().contains("10000"));
        assertFalse(result.getResponse().getContentAsString().contains("7000"));
    }

    @Test
    void updateBalance_PenaltyFeeAutomaticallyDeductedWhenBalanceGoBelowMinimumBalance() throws Exception {
        BalanceDTO balanceDTO = new BalanceDTO(new BigDecimal(200), Currency.getInstance("EUR"));

        String body = objectMapper.writeValueAsString(balanceDTO);

        MvcResult result = mockMvc.perform(patch("/accounts/balance/" +
                    accountRepository.findByPrimaryOwnerId(
                            accountHolderRepository.findByName("Úrsula Corveró").get().getId()
                    ).get().getId())
                    .content(body).contentType(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                    .andExpect(status().isAccepted())
                    .andReturn();

        assertEquals(balanceDTO.getBalance().getAmount().subtract(Constants.PENALTY_FEE),
                accountRepository.findByPrimaryOwnerId(accountHolderRepository.findByName("Úrsula Corveró").get().getId())
                        .get().getBalance().getAmount());
    }

    @Test
    void updateBalance_NoPenaltyFeeDeduction() throws Exception {
        BalanceDTO balanceDTO = new BalanceDTO(new BigDecimal(1000), Currency.getInstance("EUR"));

        String body = objectMapper.writeValueAsString(balanceDTO);

        MvcResult result = mockMvc.perform(patch("/accounts/balance/" +
                    accountRepository.findByPrimaryOwnerId(
                            accountHolderRepository.findByName("Úrsula Corveró").get().getId()
                    ).get().getId())
                    .content(body).contentType(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                    .andExpect(status().isAccepted())
                    .andReturn();

        assertEquals(balanceDTO.getBalance().getAmount(), accountRepository.findByPrimaryOwnerId(
                accountHolderRepository.findByName("Úrsula Corveró").get().getId()).get().getBalance().getAmount());
    }

    @Test
    void changeStatus_SuccessfulChange() throws Exception {
        StatusDTO statusDTO = new StatusDTO(Status.FROZEN);

        String body = objectMapper.writeValueAsString(statusDTO);

        MvcResult result = mockMvc.perform(patch("/accounts/status/" +
                accountRepository.findByPrimaryOwnerId(
                        accountHolderRepository.findByName("Úrsula Corveró").get().getId()
                ).get().getId())
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isAccepted())
                .andReturn();

        assertEquals(statusDTO.getStatus(), accountRepository.findByPrimaryOwnerId(
                accountHolderRepository.findByName("Úrsula Corveró").get().getId()).get().getStatus());
    }

    @Test
    void deleteAccount_SuccessfulRemoval() throws Exception {
        mockMvc.perform(delete("/accounts/" +
                accountRepository.findByPrimaryOwnerId(
                        accountHolderRepository.findByName("Úrsula Corveró").get().getId()
                ).get().getId())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin")))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThrows(NoSuchElementException.class, () -> accountRepository.findByPrimaryOwnerId(
                accountHolderRepository.findByName("Úrsula Corveró").get().getId()).get().getId());
    }

}
