//package com.equifax.course.service.impl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.equifax.course.model.dao.ICourseDao;
//import com.equifax.course.model.domain.Course;
//import com.equifax.course.service.ICourseService;
//
//@Service
//public class CourseServiceImpl implements ICourseService
//{
//	@Autowired
//	private ICourseDao courseDao;
//
//	@Override
//	public List<Course> finAll()
//	{
//		return (List<Course>) courseDao.findAll();
//	}
//
//	@Override
//	public Course findById(Integer id)
//	{
//		return courseDao.findById(id).orElse(null);
//	}
//
//	@Override
//	public Course save(Course course)
//	{
//		return courseDao.save(course);
//	}
//
//	@Override
//	public void delete(Integer id)
//	{
//		courseDao.deleteById(id);
//	}
//}
