package com.coderscampus.week18.TravisWinstonAssignment13.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.week18.TravisWinstonAssignment13.domain.Address;
import com.coderscampus.week18.TravisWinstonAssignment13.domain.User;
import com.coderscampus.week18.TravisWinstonAssignment13.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
//	@Autowired
//	private AccountRepository accountRepo;
	
	public List<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}
	
	public List<User> findByUsernameAndName(String username, String name) {
		return userRepo.findByUsernameAndName(username, name);
	}
	
	public User findExactlyOneUserByUsername(String username) {
		List<User> users = userRepo.findExactlyOneUserByUsername(username);
		if (users.size() > 0)
			return users.get(0);
		else
			return new User();
		//return userRepo.findExactlyOneUserByUsername(username);
	}
	
	public List<User> findByCreatedDateBetween(LocalDate date1, LocalDate date2) {
		return userRepo.findByCreatedDateBetween(date1, date2);
	}
	
	public Set<User> findAll () {
		return userRepo.findaAllUsersWithAccountsAndAddresses();
	}
	
	public User findById(Long userId) {
		Optional<User> userOpt = userRepo.findById(userId);
		return userOpt.orElse(new User());
	}

	public User saveUser(User user) {
		return userRepo.save(user);		
	}

	public void delete(Long userId) {
		userRepo.deleteById(userId);		
	}
}
