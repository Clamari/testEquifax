package com.equifax.course.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.equifax.course.model.dao.ICourseDao;
import com.equifax.course.model.domain.Course;

@RestController
@RequestMapping("/courses")
public class CourseRestController
{
	@Autowired
	private ICourseDao courseDao;

	@GetMapping({ "", "/", "/list" })
	public List<Course> index()
	{
		return courseDao.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Course> show(@PathVariable Integer id)
	{
		Course course = courseDao.findById(id).orElse(null);
		return new ResponseEntity<Course>(course, (course != null ? HttpStatus.OK : HttpStatus.NOT_FOUND));
	}

	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public Course create(@RequestBody Course course)
	{
		if(course.getId() != null) return null;
		else return courseDao.save(course);
	}

	@PutMapping("/edit")
	public Course edit(@RequestBody Course course)
	{
		return courseDao.save(course);
	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable Integer id)
	{
		courseDao.deleteById(id);
	}
}
