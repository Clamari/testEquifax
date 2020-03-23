package com.equifax.course.model.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.equifax.course.model.domain.Student;

public interface StudentDao extends JpaRepository<Student, Integer>
{
	public Page<Student> findAllByOrderByIdAsc(Pageable pageable);
}
