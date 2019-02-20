package com.CGI.springBoot;

import com.CGI.springBoot.service.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/services")
public class GlobalController {

	@Autowired
	GlobalService globalService;

	@PostMapping("post")
	@ResponseStatus(value = HttpStatus.OK)
	public void fillDB() {
		globalService.remplireDB();
	}

	@DeleteMapping("delete")
	@ResponseStatus(value = HttpStatus.OK)
	public void truncateDB() {
		globalService.truncateDB();
	}
}
