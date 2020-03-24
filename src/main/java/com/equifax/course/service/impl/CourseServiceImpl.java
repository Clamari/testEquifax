package com.equifax.course.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equifax.course.model.dao.CourseDao;
import com.equifax.course.model.dao.StudentDao;
import com.equifax.course.model.domain.Course;
import com.equifax.course.model.domain.Student;
import com.equifax.course.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService
{
	@Autowired
	private CourseDao courseDao;

	@Autowired
	private StudentDao studentDao;

	@Override
	public List<Course> findByIdInFetchStundents(List<Integer> ids)
	{
		List<Course> courses = courseDao.findByIdIn(ids);
		List<Student> students = studentDao.findByCourseIn(ids);
		for (Course course : courses) // Prevent automatic hibernate lazy query
			course.setStudents(new ArrayList<Student>());
		for (Course course : courses)
			for (Student student : students)
				if (student.getCourse().getId() == course.getId()) course.getStudents().add(student);
		return courses;
	}

	@Override
	@Transactional
	public void deleteCascade(Integer id)
	{
		studentDao.deleteByCourse(id);
		courseDao.deleteById(id);
	}
}
