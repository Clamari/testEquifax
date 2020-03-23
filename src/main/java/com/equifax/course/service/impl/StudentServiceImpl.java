package com.equifax.course.service.impl;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.equifax.course.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService
{
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
}
