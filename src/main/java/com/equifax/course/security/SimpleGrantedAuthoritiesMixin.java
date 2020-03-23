package com.equifax.course.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleGrantedAuthoritiesMixin
{
	@JsonCreator
	public SimpleGrantedAuthoritiesMixin(@JsonProperty("authority") String role)
	{
	}
}
