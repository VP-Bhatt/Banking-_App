package net.javademo.bankingapp.Impl;

import net.javademo.bankingapp.Mapper.AccountMapper;
import net.javademo.bankingapp.dto.AccountDto;
import net.javademo.bankingapp.entity.Account;
import net.javademo.bankingapp.repository.AccountRepository;
import net.javademo.bankingapp.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        var savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        var getAccount = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exist"));
        return  AccountMapper.mapToAccountDto(getAccount);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        var account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exist"));
        var totalBalance = account.getBalance() + amount;
        account.setBalance(totalBalance);
        var savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        var account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exist"));
        var totalBalance = account.getBalance() - amount;
        if(totalBalance < 0){
            throw new RuntimeException("Insufficient Amount");
        }
        account.setBalance(totalBalance);
        var savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        var accountList = accountRepository.findAll();
        return accountList.stream().map(AccountMapper::mapToAccountDto).collect(Collectors.toList());
    }


}
