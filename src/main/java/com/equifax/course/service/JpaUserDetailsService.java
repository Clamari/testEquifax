package com.equifax.course.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.equifax.course.model.dao.UserDao;
import com.equifax.course.model.domain.UserRole;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService
{
	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		com.equifax.course.model.domain.User user = userDao.findByUsername(username);
		if (user != null)
		{
			List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
			for (UserRole userRole : user.getUserRoles())
				roles.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
			return new User(user.getUsername(), user.getPassword(), user.getEnabledView(), true, true, true, roles);
		}
		else throw new UsernameNotFoundException("the user '" + username + "' does not exists");
	}
}
