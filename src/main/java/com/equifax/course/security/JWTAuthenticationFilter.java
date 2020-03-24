package com.equifax.course.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.equifax.course.core.MyConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/token", "POST")); // use "POST" for production, GET for dev with no credentials
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
	{
		//Development no credentials code. Automatically logs in with a superAdmin account
//		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken("god", "asdgod");
//		return authenticationManager.authenticate(authToken);
		//End development code
		//Production Code
		Credential credential = null;
		try
		{
			credential = new ObjectMapper().readValue(request.getInputStream(), Credential.class);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if(credential != null)
		{
			if (credential.getUsername() == null) credential.setUsername("");
			if (credential.getPassword() == null) credential.setPassword("");
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credential.getUsername(), credential.getPassword());
			return authenticationManager.authenticate(authToken);
		}
		else
		{
			response.setStatus(400);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			return null;
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
			throws IOException, ServletException
	{
		Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
		Claims claims = Jwts.claims();
		claims.put(MyConstants.JWT_AUTHORITIES, new ObjectMapper().writeValueAsString(roles));
		String token = Jwts.builder().setSubject(authResult.getName()).setClaims(claims).signWith(MyConstants.JWT_SECRET_KEY)
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 3600000L)) // JWT lifetime 3600000 millis = 1 hour
				.compact();
		response.addHeader(MyConstants.JWT_AUTHORIZATION, MyConstants.JWT_BEARER + token);
		Map<String, String> body = new HashMap<String, String>();
		body.put("token", token);
		body.put("user", authResult.getName());
		response.setStatus(200);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException
	{
		Map<String, String> body = new HashMap<String, String>();
		body.put("error", failed.getMessage());
		response.setStatus(401);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
	}
}
