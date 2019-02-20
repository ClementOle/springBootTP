package com.CGI.springBoot.user;

import com.CGI.springBoot.account.Account;
import com.CGI.springBoot.account.AccountRepository;
import com.CGI.springBoot.group.Group;
import com.CGI.springBoot.group.GroupRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private GroupRepository groupRepository;


	//Test création d'un utilisateur normalement
	@Test
	public void creationUser() {
		User user = new User("Toto", "tata", "Choupi", null, null);
		userRepository.save(user);
		Assert.assertEquals(userRepository.findUserById(user.getId()), user);
		userRepository.delete(user);
		userRepository.flush();

	}

	//Test de création d'un utilisateur avec les attributs non renseignés
	@Test
	public void creationUserChampVide() {
		User user = new User();
		try {
			userRepository.save(user);
			Assert.fail();
		} catch (ConstraintViolationException c) {
			Assert.assertTrue(true);
		}
	}

	//Test de créaztion d'un utilisateur avec nickname vide
	@Test
	public void creationUserNickNameVide() {
		User user = new User("John", "Doe", " ", null, null);
		try {
			userRepository.save(user);
			Assert.assertTrue(true);
		} catch (AssertionError c) {
			Assert.fail();
			c.getMessage();
		}
		userRepository.delete(user);
		userRepository.flush();

	}

	//Test création d'un user avec verification des attributs
	@Test
	public void creationUserAvecVerif() {
		User user = new User("Toto", "Tata", "Titi", null, null);
		userRepository.save(user);
		user = userRepository.findUserById(user.getId());
		Assert.assertEquals("Toto", userRepository.findUserById(user.getId()).getFirstName());
		Assert.assertEquals("Tata", userRepository.findUserById(user.getId()).getLastName());
		Assert.assertEquals("Titi", userRepository.findUserById(user.getId()).getNickName());
		userRepository.delete(user);
		userRepository.flush();

	}

	//Test suppression d'un user
	@Test
	public void suppressionUser() {
		User user = new User("fdfds", "fdsfdsfs", "fdfds", null, null);
		userRepository.save(user);
		userRepository.delete(user);
		Assert.assertNotEquals(userRepository.findUserById(user.getId()), user);
	}

	//Test modification d'un user
	@Test
	public void modifyUser() {
		User user = new User("Toto", "tata", "Choupi", null, null);
		userRepository.save(user);

		User userAModifier = userRepository.findUserById(user.getId());
		userAModifier.setFirstName("Lapinoux");

		userRepository.save(userAModifier);
		Assert.assertEquals(userRepository.findUserById(user.getId()).getFirstName(), "Lapinoux");
		userRepository.delete(user);
		userRepository.flush();

	}

	//Test de modification d'un attribue définie en not null
	@Test
	public void modifyUserProprieteNotNull() {
		User user = new User("Toto", "tata", "Choupi", null, null);
		userRepository.save(user);

		User userAModifier = userRepository.findUserById(user.getId());
		userAModifier.setFirstName("");
		try {
			userRepository.save(userAModifier);
			Assert.fail();
		} catch (AssertionError e) {
			Assert.assertTrue(true);
		}
		userRepository.delete(user);
		userRepository.flush();
	}

	//Test du findAllByOrderByIdAsc de UserRepository
	@Test
	public void testFindAllByOrderByIdAsc() {
		//Given
		User user = new User("prenom", "nom", "", null, null);
		User user2 = new User("prenom2", "nom2", "", null, null);
		userRepository.save(user2);
		userRepository.save(user);

		List<User> userList = userRepository.findAllByOrderByIdAsc();

		int id = -1;
		for (User userATest : userList) {
			Assert.assertTrue(id < userATest.getId());
			id = userATest.getId();
		}

		userRepository.delete(user);
		userRepository.delete(user2);
		userRepository.flush();
	}

	//Test de l'ajout d'une liste d'account à un user
	@Test
	public void testAjoutListAccount() {
		User user = new User("firstName", "LastName", "", new ArrayList<>(), null);

		userRepository.save(user);

		Account account = new Account("mail", "password", "description", user);
		Account account2 = new Account("mail3", "password", "description", user);

		accountRepository.save(account);
		accountRepository.save(account2);
		accountRepository.flush();

		List<Account> accounts = new ArrayList<>();
		accounts.add(account);
		accounts.add(account2);

		userRepository.flush();

		System.err.println(userRepository.findUserById(user.getId()).toString());
		Assert.assertEquals(accounts, userRepository.findUserById(user.getId()).getAccountList());
		accountRepository.delete(account);
		accountRepository.delete(account2);
		accountRepository.flush();
		userRepository.delete(user);
		userRepository.flush();

	}

	//Test de l'ajout d'un group à un user
	@Test
	public void testAjoutGroup() {
		//Given
		Group group = new Group("title", null);
		groupRepository.save(group);
		User user = new User("fdf", "fdsfds", "", null, group);
		userRepository.save(user);

		//Then
		Assert.assertEquals(userRepository.findUserById(user.getId()).getGroup(), groupRepository.findGroupById(group.getId()));

		userRepository.delete(user);
		userRepository.flush();
		groupRepository.delete(group);
		groupRepository.flush();
	}
}
