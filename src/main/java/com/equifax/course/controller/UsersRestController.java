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
	public ResponseEntity<?> password(@RequestBody Map<String, Object> password)
	{
		PasswordEncoder encoder = passwordEncoder;
		Map<String, String> response = new HashMap<String, String>();
		response.put("your_encrypted_password", encoder.encode(password.get("password").toString()));
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.OK);
	}
}
