package com.equifax.course.model.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.equifax.course.model.domain.Student;

public interface StudentDao extends JpaRepository<Student, Integer>
{
	public Page<Student> findAllByOrderByIdAsc(Pageable pageable);

	@Query("SELECT stud FROM Student stud WHERE stud.course.id IN :ids")
	public List<Student> findByCourseIn(List<Integer> ids);

	@Modifying
	@Query("DELETE FROM Student stud WHERE stud.course.id = :id")
	public Integer deleteByCourse(Integer id);
}
