package com.CGI.springBoot.user;

import com.CGI.springBoot.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserRepository userRepository;

	//Get un user

	@GetMapping("{id}")
	public User getUser(@PathVariable(value = "id") int userId) {
		Optional<User> user = userRepository.findById(userId);
		return user.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
	}

	//Post un user

	@PostMapping()
	@ResponseStatus(value = HttpStatus.OK)
	public User postUser(@Valid @RequestBody User user) {
		return userRepository.save(user);
	}

}
