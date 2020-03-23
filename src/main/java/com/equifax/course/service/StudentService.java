package com.equifax.course.service;

import java.util.List;

import com.equifax.course.model.domain.Student;

public interface StudentService
{
	public boolean validateRut(String rut);

	public List<String> validateStudent(Student student);
}