package com.equifax.course.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.equifax.course.model.domain.Course;

public interface CourseDao extends JpaRepository<Course, Integer>
{
}
