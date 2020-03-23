package com.equifax.course.core;

import javax.crypto.SecretKey;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class MyConstants
{
	public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	public static final String ROLE_PREFIX = "ROLE_";
	public static final String ROLE_ADMIN_NP = "ADMIN";
	public static final String ROLE_ADMIN = ROLE_PREFIX + ROLE_ADMIN_NP;
	public static final String ROLE_DIRECTOR_NP = "DIRECTOR";
	public static final String ROLE_DIRECTOR = ROLE_PREFIX + ROLE_DIRECTOR_NP;
	public static final String ROLE_TEACHER_NP = "TEACHER";
	public static final String ROLE_TEACHER = ROLE_PREFIX + ROLE_TEACHER_NP;
}
