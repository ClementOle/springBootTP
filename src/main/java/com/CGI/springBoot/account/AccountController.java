package com.CGI.springBoot.account;

import com.CGI.springBoot.exception.ResourceNotFoundException;
import com.CGI.springBoot.user.User;
import com.CGI.springBoot.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	UserRepository userRepository;

	//Get un account
	@GetMapping("{id}")
	public Account getAccount(@PathVariable(value = "id") int code) {
		return accountRepository.findById(code).orElseThrow(() -> new ResourceNotFoundException("Account", "id", code));
	}

	//Get tout les accountes
	@GetMapping("*")
	public List<Account> getAllAccount() {
		return accountRepository.findAll();
	}

	//Get tout les accountes mais paginer
	@GetMapping()
	public Page<Account> getAllAccountPage(@RequestParam(value = "page") int page, @RequestParam(value = "size", defaultValue = "10") int size, @RequestParam(value = "sortProperty", defaultValue = "id") String sortProperty, @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection) {
		if (page < 0) {
			throw new IllegalArgumentException("Le numéro de page ne peut être négatif");
		} else if (page > accountRepository.findAll().size() / size) {
			throw new IllegalArgumentException("Le numéro de page est trop grand");
		} else if (size < 0) {
			throw new IllegalArgumentException("La taille ne peut être négative");
		} else if (size >= accountRepository.findAll().size()) {
			throw new IllegalArgumentException("La taille ne peut être supérieur au nombre d'employé");
		}

		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
		return accountRepository.findAll(pageRequest);
	}

	@PostMapping()
	@ResponseStatus(value = HttpStatus.OK)
	public Account postAccount(@Valid @RequestBody Account account) {
		User user = userRepository.findById(account.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", account.getUser().getId()));
		account.setUser(user);
		return accountRepository.save(account);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteAccount(@PathVariable(value = "id") int id) {
		Optional<Account> account = accountRepository.findById(id);
		if (!account.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			accountRepository.delete(account.orElseThrow(() -> new ResourceNotFoundException("Account", "id", id)));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@PutMapping("{code}")
	public ResponseEntity<?> updateAccount(@PathVariable(value = "code") int code, @RequestBody Account account) {
		Account accountTrouver = accountRepository.findById(code).orElseThrow(() -> new ResourceNotFoundException("Account", "code", code));

		accountTrouver.setMail(account.getMail());
		accountTrouver.setUser(account.getUser());
		accountTrouver.setDescription(account.getDescription());
		accountTrouver.setPassword(account.getPassword());
		accountRepository.save(accountTrouver);
		return new ResponseEntity<>(accountRepository.save(accountTrouver), HttpStatus.OK);
	}
}
