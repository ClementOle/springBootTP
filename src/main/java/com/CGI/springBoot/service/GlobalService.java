package com.CGI.springBoot.service;

import com.CGI.springBoot.account.Account;
import com.CGI.springBoot.account.AccountRepository;
import com.CGI.springBoot.group.Group;
import com.CGI.springBoot.group.GroupRepository;
import com.CGI.springBoot.user.User;
import com.CGI.springBoot.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GlobalService {

	@Autowired
	GroupRepository groupRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AccountRepository accountRepository;

	public void remplireDB() {
		Group group1 = new Group("fdfdsf", null);
		Group group2 = new Group("fdfdfdfdsfdsfdsf", null);
		ArrayList<User> users = new ArrayList<>();
		users.add(new User("fdfs", "fdsfdsf", "", null, group1));
		users.add(new User("fdfdfdsfdsfs", "fdsfdfdsfdsf", "fdsf", null, group1));
		users.add(new User("fdtretrefs", "fdstretfdsf", "fnjhhg", null, group1));
		users.add(new User("fttertredfs", "fdsretrefdsf", "hjgjh", null, group2));
		users.add(new User("fdtrtefs", "fdsfdtrtesf", "trtetret", null, group2));


		for (User user : users) {
			groupRepository.save(user.getGroup());
			userRepository.save(user);
			ArrayList<Account> accounts = new ArrayList<>();
			Account account = new Account("mail", "password", "description", user);
			accounts.add(account);
			user.setAccountList(accounts);
			accountRepository.save(account);
		}

	}

	public void truncateDB(){
		accountRepository.deleteAll();
		userRepository.deleteAll();
		groupRepository.deleteAll();
	}
}
