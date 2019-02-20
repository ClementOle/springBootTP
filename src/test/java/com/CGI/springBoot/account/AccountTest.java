package com.CGI.springBoot.account;

import com.CGI.springBoot.group.GroupRepository;
import com.CGI.springBoot.user.User;
import com.CGI.springBoot.user.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private GroupRepository groupRepository;


	//Test création d'un account
	@Test
	public void creationAccount() {
		User user = new User("fdfds", "fdsf", "", null, null);
		userRepository.save(user);
		Account account = new Account("mail@exemple.com", "password", "snjkfksdn,fjklsf", user);
		accountRepository.save(account);

		Assert.assertEquals(accountRepository.findById(account.getCode()).get(), account);
		accountRepository.delete(account);
		accountRepository.flush();

		userRepository.delete(user);
		userRepository.flush();
	}

	//Test création d'un account avec des attributs null
	@Test
	public void creationAccountChampVide() {
		Account account = new Account();
		try {
			accountRepository.save(account);
		} catch (AssertionError c) {
			Assert.assertTrue(true);
			c.getMessage();
		}

	}

	//Création d'un account avec la description null
	@Test
	public void creationAccountDescriptionVide() {
		User user = new User("fdfds", "fdsf", "", null, null);
		userRepository.save(user);
		Account account = new Account("fdnfds@fdsfdsf.com", "password", " ", user);
		try {
			accountRepository.save(account);
			Assert.assertTrue(true);
		} catch (ConstraintViolationException c) {
			Assert.fail();
			c.getMessage();
		}
		accountRepository.delete(account);
		accountRepository.flush();

		userRepository.delete(user);
		userRepository.flush();
	}


	//Test création d'un account avec vérification des attributs
	@Test
	public void creationAccountAvecVerif() {
		User user = new User("fdfds", "fdsf", "", null, null);
		userRepository.save(user);
		Account account = new Account("mailTest@exemple.com", "passwordTest", "snjkfksdn,fjklsf", user);
		accountRepository.save(account);
		account = accountRepository.findAccountByCode(account.getCode());
		Assert.assertEquals("mailTest@exemple.com", accountRepository.findAccountByCode(account.getCode()).getMail());
		Assert.assertEquals("passwordTest", accountRepository.findAccountByCode(account.getCode()).getPassword());
		Assert.assertEquals("snjkfksdn,fjklsf", accountRepository.findAccountByCode(account.getCode()).getDescription());

		accountRepository.delete(account);
		accountRepository.flush();

		userRepository.delete(user);
		userRepository.flush();

	}

	//Test suppression d'un account
	@Test
	public void suppressionAccount() {
		User user = new User("fdfdsAAAAAAAAAAAAAAa", "fdsf", "", null, null);
		userRepository.save(user);
		Account account = new Account("mailTest@exemple.comm", "passwordTest2", "snjkfksdn,fjklsffsfdsf", user);
		accountRepository.save(account);
		accountRepository.delete(account);
		accountRepository.flush();
		Assert.assertTrue(!accountRepository.findById(account.getCode()).isPresent());

		userRepository.delete(user);
		userRepository.flush();
	}


	//Test modification d'un account
	@Test
	public void modifyAccount() {
		User user = new User("fdfds", "fdsf", "", null, null);
		userRepository.save(user);
		Account account = new Account("email.exemple@fds.com", "motdepasse", "description", user);
		accountRepository.save(account);

		Account accountAModifier = accountRepository.findAccountByCode(account.getCode());
		accountAModifier.setMail("emailmodifier@gmail.com");

		accountRepository.save(accountAModifier);
		Assert.assertEquals(accountRepository.findAccountByCode(account.getCode()).getMail(), "emailmodifier@gmail.com");
		accountRepository.delete(account);
		accountRepository.flush();

		userRepository.delete(user);
		userRepository.flush();

	}

	//Test de modification d'un attribue définie en not null
	@Test
	public void modifyAccountProprieteNotNull() {
		User user = new User("fdfds", "fdsf", "", null, null);
		userRepository.save(user);
		Account account = new Account("mail@mail.mail", "1235", "fdsfdsf", user);
		accountRepository.save(account);

		Account accountAModifier = accountRepository.findAccountByCode(account.getCode());
		accountAModifier.setMail("");
		try {
			accountRepository.save(accountAModifier);
			Assert.fail();
		} catch (AssertionError e) {
			Assert.assertTrue(true);
		}
		accountRepository.delete(account);
		accountRepository.flush();

		userRepository.delete(user);
		userRepository.flush();
	}

	//Test du findAllByOrderByCodeAsc de AccountRepository
	@Test
	public void testFindAllByOrderByCodeAsc() {
		User user = new User("fdfds", "fdsf", "", null, null);
		userRepository.save(user);
		User user2 = new User("fdfdsfdsfd", "fdsffdsf", "", null, null);
		userRepository.save(user);
		Account account = new Account("mail1", "aaa", "", user);
		Account account2 = new Account("mail2", "bbb", "", user2);
		accountRepository.save(account2);
		accountRepository.save(account);

		List<Account> accountList = accountRepository.findAllByOrderByCodeAsc();

		int id = -1;
		for (Account accountATest : accountList) {
			Assert.assertTrue(id < accountATest.getCode());
			id = accountATest.getCode();
		}

		accountRepository.delete(account);
		accountRepository.delete(account2);
		accountRepository.flush();

		userRepository.delete(user);
		userRepository.flush();

		userRepository.delete(user2);
		userRepository.flush();
	}

	//Test de l'ajout d'un utilisateur à un account
	@Test
	public void ajoutUtilisateur() {
		//Given
		User user = new User("fef", "fdsfs", "", null, null);
		userRepository.save(user);

		Account account = new Account("mail", "password", "fdfdsf", user);
		accountRepository.save(account);
		//Then

		Assert.assertEquals(accountRepository.findAccountByCode(account.getCode()).getUser(), userRepository.findUserById(user.getId()));

		accountRepository.delete(account);
		accountRepository.flush();

		userRepository.delete(user);
		userRepository.flush();

	}
}
