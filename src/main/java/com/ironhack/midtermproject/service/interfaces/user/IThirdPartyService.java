package com.ironhack.midtermproject.service.interfaces.user;

import com.ironhack.midtermproject.controller.dto.TPartyTransactionDTO;
import com.ironhack.midtermproject.controller.dto.ThirdPartyDTO;
import com.ironhack.midtermproject.dao.users.ThirdParty;

public interface IThirdPartyService {

    ThirdParty addThirdParty(ThirdPartyDTO thirdPartyDTO);
    TPartyTransactionDTO makeATransaction(TPartyTransactionDTO transactionDTO);

}
