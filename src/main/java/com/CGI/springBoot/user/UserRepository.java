package com.CGI.springBoot.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	List<User> findAllByOrderByIdAsc();

	User findUserById(Integer id);

	User findUserByFirstName(String name);

	User findUserByLastName(String name);

}
