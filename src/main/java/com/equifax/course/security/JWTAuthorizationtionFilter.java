package com.equifax.course.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.equifax.course.core.MyConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTAuthorizationtionFilter extends BasicAuthenticationFilter
{
	public JWTAuthorizationtionFilter(AuthenticationManager authenticationManager)
	{
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		String header = request.getHeader(MyConstants.JWT_AUTHORIZATION);
		if (header == null || !header.startsWith(MyConstants.JWT_BEARER))
		{
			chain.doFilter(request, response);
			return;
		}
		Claims token = null;
		try
		{
			token = Jwts.parserBuilder().setSigningKey(MyConstants.JWT_SECRET_KEY).build().parseClaimsJws(header.replace(MyConstants.JWT_BEARER, "")).getBody();
		}
		catch (Exception e) { e.printStackTrace(); }
		UsernamePasswordAuthenticationToken authToken = null;
		if (token != null)
		{
			byte[] roles = token.get(MyConstants.JWT_AUTHORITIES).toString().getBytes();
			Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
					.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class).readValue(roles, SimpleGrantedAuthority[].class));
			authToken = new UsernamePasswordAuthenticationToken(token.getSubject(), null, authorities);
		}
		SecurityContextHolder.getContext().setAuthentication(authToken);
		chain.doFilter(request, response);
	}
}
