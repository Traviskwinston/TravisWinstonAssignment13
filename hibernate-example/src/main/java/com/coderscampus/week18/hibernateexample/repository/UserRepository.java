package com.coderscampus.week18.hibernateexample.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coderscampus.week18.hibernateexample.domain.User;

//Change T to Type (User) and ID to the PK type (Long). Import the related Table (User)
@Repository //Should find this even without annotation
public interface UserRepository extends JpaRepository<User, Long> {

	// select * from users where username = :username
	List<User> findByUsername(String username);
	
	// select * from users where name = :name
	List<User> findByName(String name);
	
	// select * from users where name = :name and username = :username
	List<User> findByUsernameAndName(String username, String name);
	
	List<User> findByCreatedDateBetween(LocalDate date1, LocalDate date2);
	
	@Query("select u from User u where username = :username") //equivalent to "select * from Users where username = :username"  Use capital User becuase you use class name
	List<User>findExactlyOneUserByUsername(String username);

	@Query("select u from User u"
			+ " left join fetch u.accounts"
			+ " left join fetch u.address")
	Set<User> findaAllUsersWithAccountsAndAddresses();
}
