package com.CGI.springBoot.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	List<Account> findAllByOrderByCodeAsc();

	Account findAccountByCode(Integer code);

	Account findAccountByMail(String Mail);

	Account findAccountByPassword(String password);

}
