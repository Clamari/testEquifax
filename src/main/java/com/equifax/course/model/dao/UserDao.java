package com.equifax.course.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.equifax.course.model.domain.User;

public interface UserDao extends JpaRepository<User, Integer>
{
	@Query("SELECT user FROM User user INNER JOIN FETCH user.userRoles usro INNER JOIN FETCH usro.role WHERE user.username = :username")
	public User findByUsername(String username);
}
