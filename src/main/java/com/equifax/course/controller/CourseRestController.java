package com.equifax.course.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public ResponseEntity<?> show(@PathVariable Integer id)
	{
		Course course = courseDao.findById(id).orElse(null);
		if (course != null) return new ResponseEntity<Course>(course, HttpStatus.OK);
		else
		{
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("message", "course id '" + id + "' does not exists");
			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody Course course)
	{
		if (course.getId() == null)
		{
			try
			{
				course = courseDao.save(course);
				return new ResponseEntity<Course>(course, HttpStatus.OK);
			}
			catch (Exception e)
			{
				course = null;
				Map<String, String> errors = new HashMap<String, String>();
				errors.put("message", e.getMessage());
				return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
			}
		}
		else
		{
			course = null;
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("message", "course id must be null or undefined");
			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
		}
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
