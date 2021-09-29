package com.ironhack.midtermproject.controller.impl.user;

import com.ironhack.midtermproject.controller.dto.TPartyTransactionDTO;
import com.ironhack.midtermproject.controller.dto.ThirdPartyDTO;
import com.ironhack.midtermproject.controller.interfaces.user.IThirdPartyController;
import com.ironhack.midtermproject.dao.users.ThirdParty;
import com.ironhack.midtermproject.repository.user.ThirdPartyRepository;
import com.ironhack.midtermproject.service.interfaces.user.IThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("")
public class ThirdPartyController implements IThirdPartyController {

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private IThirdPartyService thirdPartyService;

    @GetMapping("/thirdParty")
    @ResponseStatus(HttpStatus.OK)
    public List<ThirdParty> getAllThirdParties() {
        return thirdPartyRepository.findAll();
    }

    @GetMapping("/thirdParty/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<ThirdParty> getThirdParty(@PathVariable("id") Long id) {
        return thirdPartyRepository.findById(id);
    }

    @PostMapping("/thirdParty")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty addThirdParty(@RequestBody @Valid ThirdPartyDTO thirdPartyDTO) {
        return thirdPartyService.addThirdParty(thirdPartyDTO);
    }

    @PostMapping("/TP/transaction/{hashedKey}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TPartyTransactionDTO makeATransaction(@PathVariable("hashedKey") String hashedKey,
                                                 @RequestBody @Valid TPartyTransactionDTO transactionDTO) {
        return thirdPartyService.makeATransaction(transactionDTO);
    }

    @DeleteMapping("/thirdParty/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        thirdPartyRepository.deleteById(id);
    }

}
