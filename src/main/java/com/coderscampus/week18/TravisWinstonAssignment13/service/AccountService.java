package com.coderscampus.week18.TravisWinstonAssignment13.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.week18.TravisWinstonAssignment13.domain.Account;
import com.coderscampus.week18.TravisWinstonAssignment13.domain.User;
import com.coderscampus.week18.TravisWinstonAssignment13.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private UserService userService;

    public Account save(Account account, Long userId) {
        // Fetch the user
        User user = userService.findById(userId);

        // Link account to the user
        if (!user.getAccounts().contains(account)) {
            // Link the account to the user only if it's not already linked
            account.getUsers().add(user);  // Link account to user
            user.getAccounts().add(account); // Link user to account
        }

        // Save the account and user
        return accountRepo.save(account);
    }

	public Account findOne(Long accountId) {
		Optional<Account> accOpt = accountRepo.findById(accountId);
		return accOpt.orElse(new Account());
	}
}
