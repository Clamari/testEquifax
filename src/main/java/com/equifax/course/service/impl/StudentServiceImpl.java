package com.equifax.course.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.equifax.course.model.domain.Student;
import com.equifax.course.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService
{
	@Value("${StudentRestController.create.invalidrut}")
	private String createinvalidrut;

	@Value("${StudentRestController.create.invalidage}")
	private String createinvalidage;

	@Value("${StudentRestController.create.nullcourse}")
	private String createnullcourse;

	@Override
	public boolean validateRut(String rut)
	{
		boolean valid = false;
		rut = rut.toUpperCase();
		if (Pattern.matches("^\\d{7,9}\\-{1}([\\dK]{1})$", rut))
		{
			String rutAux = rut.split("-")[0];
			char dv = rut.charAt(rut.length() - 1);
			int multiplier = 2;
			int sum = 0;
			for (int i = rutAux.length() - 1; i >= 0; i--)
			{
				int digit = Integer.parseInt(rutAux.substring(i, i + 1));
				sum += digit * multiplier++;
				if (multiplier == 8) multiplier = 2;
			}
			int mod = 11 - (sum % 11);
			if (mod == 10) valid = dv == 'K';
			else
			{
				if (mod == 11) mod = 0;
				valid = Character.getNumericValue(dv) == mod;
			}
		}
		return valid;
	}

	@Override
	public List<String> validateStudent(Student student)
	{
		List<String> errors = new ArrayList<String>();
		if (!validateRut(student.getRut())) errors.add(createinvalidrut);
		if (student.getAge() < 18) errors.add(createinvalidage);
		if (student.getCourseId() == null) errors.add(createnullcourse);
		return errors;
	}
}
