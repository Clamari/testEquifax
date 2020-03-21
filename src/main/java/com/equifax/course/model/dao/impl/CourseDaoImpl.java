package com.equifax.course.model.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.equifax.course.model.domain.Course;

@Repository
public class CourseDaoImpl
{
	@PersistenceContext
	private EntityManager em;

//	@Override
	public List<Course> findAll()
	{
		String queryStr = "SELECT cou FROM Course cou";
		return em.createQuery(queryStr, Course.class).getResultList();
	}
}
