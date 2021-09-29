package com.ironhack.midtermproject.controller.interfaces.user;

import com.ironhack.midtermproject.controller.dto.TPartyTransactionDTO;
import com.ironhack.midtermproject.controller.dto.ThirdPartyDTO;
import com.ironhack.midtermproject.dao.users.ThirdParty;

import java.util.List;
import java.util.Optional;

public interface IThirdPartyController {

    List<ThirdParty> getAllThirdParties();
    Optional<ThirdParty> getThirdParty(Long id);
    ThirdParty addThirdParty(ThirdPartyDTO thirdPartyDTO);
    TPartyTransactionDTO makeATransaction(String hashedKey, TPartyTransactionDTO transactionDTO);
    void delete(Long id);

}
