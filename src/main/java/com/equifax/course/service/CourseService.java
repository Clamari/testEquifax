package com.equifax.course.service;

import java.util.List;

import com.equifax.course.model.domain.Course;

public interface CourseService
{
	public List<Course> findByIdInFetchStundents(List<Integer> ids);

	public void deleteCascade(Integer id);
}