package com.CGI.springBoot.group;

import com.CGI.springBoot.account.AccountRepository;
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
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GroupTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private GroupRepository groupRepository;

	//Test création d'un groupe normalement
	@Test
	public void creationGroup() {
		Group group = new Group("gfgfdgfdgfd", null);
		groupRepository.save(group);
		Assert.assertEquals(groupRepository.findGroupById(group.getId()), group);
		groupRepository.delete(group);
		groupRepository.flush();

	}


	//Test création d'un group avec le titre vide
	@Test
	public void creationGroupChampVide() {
		Group group = new Group();
		try {
			groupRepository.save(group);
		} catch (ConstraintViolationException c) {
			Assert.assertTrue(true);
		}

	}

	//Test création d'un groupe en vérifiant le titre
	@Test
	public void creationGroupAvecVerif() {
		Group group = new Group("titre", null);
		groupRepository.save(group);
		group = groupRepository.findGroupById(group.getId());
		Assert.assertEquals("titre", groupRepository.findGroupById(group.getId()).getTitle());
		groupRepository.delete(group);
		groupRepository.flush();

	}

	//Test suppression d'un groupe
	@Test
	public void suppressionGroup() {
		Group group = new Group("fdsfdsf", null);
		groupRepository.save(group);
		groupRepository.delete(group);
		Assert.assertNotEquals(groupRepository.findGroupById(group.getId()), group);
	}

	//Test modification d'un groupe
	@Test
	public void modifyGroup() {
		Group group = new Group("Title", null);
		groupRepository.save(group);

		Group groupAModifier = groupRepository.findGroupById(group.getId());
		groupAModifier.setTitle("Titre modifié");

		groupRepository.save(groupAModifier);
		Assert.assertEquals(groupRepository.findGroupById(group.getId()).getTitle(), "Titre modifié");

		groupRepository.delete(group);
		groupRepository.flush();
	}


	//Test de findByTitleWithQuery de UserRepository
	@Test
	public void testFindByTitleWithQuery() {
		Group group = new Group("titre", null);
		groupRepository.save(group);
		Assert.assertEquals(groupRepository.findByTitleWithQuery("titre"), group);
		groupRepository.delete(group);
		groupRepository.flush();
	}

	//Test de findAllByOrderByIdAsc de UserRepository
	@Test
	public void testFindAllByOrderByIdAsc() {
		Group group = new Group("titre", null);
		Group group2 = new Group("titre2", null);
		groupRepository.save(group2);
		groupRepository.save(group);

		List<Group> groupList = groupRepository.findAllByOrderById();

		int id = -1;
		for (Group groupATest : groupList) {
			Assert.assertTrue(id < groupATest.getId());
			id = groupATest.getId();
		}

		groupRepository.delete(group);
		groupRepository.delete(group2);
		groupRepository.flush();
	}


	//Test de l'ajout d'une liste d'utilisateur dans un groupe
	@Test
	public void ajoutUtilisateur() {
		//Given
		User user = new User("fdfsd", "dfdsfsd", "fdfd", null, null);
		userRepository.save(user);
		List<User> userList = new ArrayList<>();
		userList.add(user);
		Group group = new Group("titre", userList);
		groupRepository.save(group);

		//Then

		Assert.assertEquals(groupRepository.findGroupById(group.getId()).getUserList().get(0), userRepository.findUserById(user.getId()));

		groupRepository.delete(group);
		groupRepository.flush();
		userRepository.delete(user);
		userRepository.flush();
	}
}
