package com.equifax.course.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersRestController
{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/password")
	public ResponseEntity<?> password(@RequestBody String password)
	{
		PasswordEncoder encoder = passwordEncoder;
		Map<String, String> errors = new HashMap<String, String>();
		errors.put("your_encrypted_password", encoder.encode("password"));
		return new ResponseEntity<Map<String, String>>(errors, HttpStatus.OK);
	}
}
