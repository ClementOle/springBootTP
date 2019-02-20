package com.CGI.springBoot.group;

import com.CGI.springBoot.exception.ResourceNotFoundException;
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
@RequestMapping("/groups")
public class GroupController {

	@Autowired
	GroupRepository groupRepository;

	//Get un group
	@GetMapping("{id}")
	public Group getGroup(@PathVariable(value = "id") int groupId) {
		return groupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group", "id", groupId));
	}

	//Get tout les groupes
	@GetMapping
	public List<Group> getAllGroup(@RequestParam(value = "sortDirection", defaultValue = "null") String sortProperty) {

		if (sortProperty.equals("ASC")) {
			return groupRepository.findAllByOrderById();
		} else if(sortProperty.equals("DSC")){
			return groupRepository.findAlldByOrderByIdDesc();
		} else {
			return groupRepository.findAll();
		}
	}

	//Get tout les groupes mais paginer
	@GetMapping("page")
	public Page<Group> getAllGroupPage(@RequestParam(value = "page") int page, @RequestParam(value = "size", defaultValue = "10") int size, @RequestParam(value = "sortProperty", defaultValue = "id") String sortProperty, @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection) {
		if (page < 0) {
			throw new IllegalArgumentException("Le numéro de page ne peut être négatif !");
		} else if (page > groupRepository.findAll().size() / size) {
			throw new IllegalArgumentException("Le numéro de page est trop grand ! ");
		} else if (size < 0) {
			throw new IllegalArgumentException("La taille ne peut être négative !");
		} else if (size >= groupRepository.findAll().size()) {
			throw new IllegalArgumentException("La taille ne peut être supérieur au nombre d'employé !");
		}

		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
		return groupRepository.findAll(pageRequest);
	}

	//Post un nouveau group

	@PostMapping()
	@ResponseStatus(value = HttpStatus.OK)
	public Group postGroup(@Valid @RequestBody Group group) {
		return groupRepository.save(group);
	}

	//Delete un group
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteGroup(@PathVariable(value = "id") int id) {
		Optional<Group> group = groupRepository.findById(id);
		if (!group.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			groupRepository.delete(group.orElseThrow(() -> new ResourceNotFoundException("Group", "id", id)));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	//Modifie un group

	@PutMapping("{id}")
	public ResponseEntity<?> updateGroup(@PathVariable(value = "id") int id, @RequestBody Group group) {
		Group groupTrouver = groupRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Group", "id", id));

		groupTrouver.setTitle(group.getTitle());
		groupTrouver.setUserList(group.getUserList());
		groupRepository.save(groupTrouver);
		return new ResponseEntity<>(groupRepository.save(groupTrouver), HttpStatus.OK);
	}
}
