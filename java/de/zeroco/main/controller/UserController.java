package de.zeroco.main.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import de.zeroco.main.service.UserService;
import de.zeroco.main.util.Util;


@RestController
public class UserController {
	
	@Autowired
	UserService service;
	
	@GetMapping("/list")
	public List<Map<String, Object>> list() {
		return service.list();
	}
	
	@DeleteMapping("/delete")
	public String delete(@RequestParam("pk_id") int pk_id) {
		if (Util.hasData(pk_id)) {
			int result = service.delete(pk_id);
			return result > 0 ? "Succesfully deleted" : "Failed deletion";
		}
		return "Please provide valid input";
	}
	
	@PostMapping("/save")
	public String save(@RequestParam("name") String name, @RequestParam("email") String  email, @RequestParam("phone") String phone, @RequestParam("password") String password) {
		if (Util.hasData(name) && Util.hasData(password) && Util.hasData(email) && Util.hasData(phone)) {
			int result = service.save(name, email, phone, password);
			return result > 0 ? "Succesfully inserted" : "Failed insertion";
		}
		return "Please provide valid inputs";
	}
	
	@PutMapping("/update")
	public String update(@RequestParam("pk_id") int pk_id, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("phone") String phone, @RequestParam("password") String password) {
		if (Util.hasData(pk_id) && Util.hasData(name) && Util.hasData(password) && Util.hasData(email) && Util.hasData(phone)) {
			int result = service.update(pk_id, name, email, phone, password);
			return result > 0 ? "Succesfully updated" : "Failed updation";
		}
		return "Please provide valid inputs";
	}
	
	@GetMapping("/get")
	public Map<String, Object> getUser(@RequestParam("pk_id") int pk_id) {
	    return service.getUser(pk_id);
	}
}
