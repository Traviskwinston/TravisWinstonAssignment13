package com.coderscampus.week18.hibernateexample.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.week18.hibernateexample.domain.Account;
import com.coderscampus.week18.hibernateexample.domain.Address;
import com.coderscampus.week18.hibernateexample.domain.User;
import com.coderscampus.week18.hibernateexample.service.AccountService;
import com.coderscampus.week18.hibernateexample.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired 
	private AccountService accountService;
	
	@GetMapping("/register")
	public String createUser (ModelMap model) {
		model.put("user", new User());
		return "register";//goes to register html
	}
	@PostMapping("/register")
	public String postCreateUser(User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}
	
	
	@GetMapping("/users")
	public String getAllUsers (ModelMap model) {
		//List<User> users = userService.findByUsername("username@generic");
		//List<User> users = userService.findByCreatedDateBetween(LocalDate.of(2024, 01, 02), LocalDate.of(2024, 01, 04));
		Set<User> users = userService.findAll();
		
		model.put("users",users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}
		return "users";//goes to users html
	}
	
	@GetMapping("/users/{userId}")
	public String getOneUser (ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		model.put("users", Arrays.asList(user));
		model.put("user", user);
		return "users";//goes to users html
	}
	
//	@PostMapping("/users/{userId}")
//	public String postOneUser (User user) {
//		userService.saveUser(user);
//		return "redirect:/users/"+user.getUserId();
//	}
	
	@PostMapping("/users/{userId}")
	public String updateUser(@PathVariable Long userId, @ModelAttribute User user) {
	    // Fetch the existing user to update
	    User existingUser = userService.findById(userId);

	    // Update user fields
	    existingUser.setUsername(user.getUsername());
	    existingUser.setPassword(user.getPassword());
	    existingUser.setName(user.getName());

	    // Handle the Address object
	    if (existingUser.getAddress() == null) {
	        existingUser.setAddress(new Address());
	    }
	    existingUser.getAddress().setAddressLine1(user.getAddress().getAddressLine1());
	    existingUser.getAddress().setAddressLine2(user.getAddress().getAddressLine2());
	    existingUser.getAddress().setCity(user.getAddress().getCity());
	    existingUser.getAddress().setRegion(user.getAddress().getRegion());
	    existingUser.getAddress().setCountry(user.getAddress().getCountry());
	    existingUser.getAddress().setZipCode(user.getAddress().getZipCode());

	    // Save the updated user
	    userService.saveUser(existingUser);

	    return "redirect:/users/" + userId;
	}
	
	
	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getAccountForm(ModelMap model, @PathVariable Long userId, @PathVariable Long accountId) {
	    User user = userService.findById(userId);
	    Account account;
	    
	    // If accountId is 0, it's a new account
	    if (accountId == 0) {
	        account = new Account();
	        account.setAccountName("Account #" + (user.getAccounts().size() + 1)); // Default name
	    } else {
	        account = accountService.findOne(accountId); // Retrieve existing account
	    }

	    model.put("user", user);
	    model.put("account", account);
	    return "account"; // Redirect to account.html
	}
	
	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String saveAccount(@PathVariable Long userId, @PathVariable Long accountId, Account account) {
	    User user = userService.findById(userId);
		
	    // If accountId is passed, we are updating the existing account
	    if (accountId !=0) {
	    	 Account existingAccount = accountService.findOne(accountId);
	         // Update only the account name
	    	 existingAccount.setAccountName(account.getAccountName());
	    	 
	    	 //Save the updated account
	         accountService.save(existingAccount, userId);
	    } else {
	        // Otherwise, create a new account
	        account.setAccountName("Account #" + (user.getAccounts().size() + 1)); // Default name
	        account.setUsers(new ArrayList<>(List.of(user)));  // Link account to user
	        accountService.save(account, userId);  // Save new account
	    }

	    // Redirect back to the user's accounts page
	    return "redirect:/users/" + userId;
	}
	
	@PostMapping("/users/{userId}/accounts/create")
	public String createNewAccount(@PathVariable Long userId) {
	    // Create a new Account for the user
	    User user = userService.findById(userId);

	    Account newAccount = new Account();
	    newAccount.setAccountName("Account #" + (user.getAccounts().size() + 1)); // Default account name
	    Account savedAccount = accountService.save(newAccount, userId);

	    // Redirect to the new account's edit page
	    return "redirect:/users/" + userId + "/accounts/" + savedAccount.getAccountId();
	}
	
	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser (@PathVariable Long userId) {
		System.out.println("deleting user with id " + userId);
		userService.delete(userId);
		return "redirect:/users";
	}
}
