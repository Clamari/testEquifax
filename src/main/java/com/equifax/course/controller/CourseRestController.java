package com.equifax.course.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equifax.course.core.IdList;
import com.equifax.course.core.MyConstants;
import com.equifax.course.model.dao.CourseDao;
import com.equifax.course.model.domain.Course;
import com.equifax.course.model.domain.Student;
import com.equifax.course.service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseRestController
{
	@Autowired
	private CourseDao courseDao;

	@Autowired
	private CourseService courseService;

	@Value("${CourseRestController.general.noid}")
	private String generalnoid;

	@Value("${CourseRestController.create.idnotnull}")
	private String createidnotnull;

	@Value("${CourseRestController.delete.deleted}")
	private String deletedeleted;

	@GetMapping("/list/{page}")
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_DIRECTOR })
	public ResponseEntity<?> list(@PathVariable Integer page)
	{
		int rows = 10;
		Map<String, Object> json = new HashMap<String, Object>();
		List<Object> jsonCourses = new ArrayList<Object>();
		Pageable pageable = PageRequest.of(page, rows);
		Page<Course> courses = courseDao.findAllByOrderByIdAsc(pageable);
		for (Course course : courses)
		{
			Map<String, Object> jsonCourse = new HashMap<String, Object>();
			jsonCourse.put("idCourse", course.getId());
			jsonCourse.put("code", course.getCode());
			jsonCourse.put("name", course.getName());
			jsonCourses.add(jsonCourse);
		}
		json.put("totalPages", courses.getTotalPages());
		json.put("currentPage", courses.getNumber());
		json.put("rowsPerPage", courses.getSize());
		json.put("rowsInThisPage", courses.getNumberOfElements());
		json.put("totalRows", courses.getTotalElements());
		json.put("courses", jsonCourses);
		if (courses.getNumber() > courses.getTotalPages() - 1) return new ResponseEntity<Map<String, Object>>(json, HttpStatus.NOT_FOUND);
		else return new ResponseEntity<Map<String, Object>>(json, HttpStatus.OK);
	}

	@GetMapping("/all")
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_DIRECTOR })
	public ResponseEntity<?> all()
	{
		Map<String, Object> json = new HashMap<String, Object>();
		List<Object> jsonCourses = new ArrayList<Object>();
		List<Course> courses = courseDao.findAll();
		for (Course course : courses)
		{
			Map<String, Object> jsonCourse = new HashMap<String, Object>();
			jsonCourse.put("idCourse", course.getId());
			jsonCourse.put("code", course.getCode());
			jsonCourse.put("name", course.getName());
			jsonCourses.add(jsonCourse);
		}
		json.put("courses", jsonCourses);
		return new ResponseEntity<Map<String, Object>>(json, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_DIRECTOR })
	public ResponseEntity<?> show(@PathVariable Integer id)
	{
		Course course = courseDao.findById(id).orElse(null);
		if (course != null)
		{
			Map<String, Object> jsonCourse = new HashMap<String, Object>();
			jsonCourse.put("idCourse", course.getId());
			jsonCourse.put("code", course.getCode());
			jsonCourse.put("name", course.getName());
			return new ResponseEntity<Map<String, Object>>(jsonCourse, HttpStatus.OK);
		}
		else
		{
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("error", generalnoid + id);
			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/create")
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_DIRECTOR })
	public ResponseEntity<?> create(@RequestBody Course course)
	{
		Map<String, Object> json = new HashMap<String, Object>();
		if (course.getId() == null)
		{
			try
			{
				course = courseDao.save(course);
				json.put("idCourse", course.getId());
				json.put("code", course.getCode());
				json.put("name", course.getName());
				return new ResponseEntity<Map<String, Object>>(json, HttpStatus.CREATED);
			}
			catch (Exception e)
			{
				json.put("error", e.getMessage());
				return new ResponseEntity<Map<String, Object>>(json, HttpStatus.BAD_REQUEST);
			}
		}
		else
		{
			json.put("error", createidnotnull);
			return new ResponseEntity<Map<String, Object>>(json, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/{id}")
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_DIRECTOR })
	public ResponseEntity<?> edit(@PathVariable Integer id, @RequestBody Course course)
	{
		Map<String, Object> json = new HashMap<String, Object>();
		Course currentCourse = courseDao.findById(id).orElse(null);
		if (currentCourse != null)
		{
			course.setId(id);
			try
			{
				course = courseDao.save(course);
				json.put("idCourse", course.getId());
				json.put("code", course.getCode());
				json.put("name", course.getName());
				return new ResponseEntity<Map<String, Object>>(json, HttpStatus.OK);
			}
			catch (Exception e)
			{
				json.put("error", e.getMessage());
				return new ResponseEntity<Map<String, Object>>(json, HttpStatus.BAD_REQUEST);
			}
		}
		else
		{
			json.put("error", generalnoid + id);
			return new ResponseEntity<Map<String, Object>>(json, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/{id}")
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_DIRECTOR })
	public ResponseEntity<?> delete(@PathVariable Integer id)
	{
		Course currentCourse = courseDao.findById(id).orElse(null);
		if (currentCourse != null)
		{
			courseDao.deleteById(id);
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("message", deletedeleted + id);
			return new ResponseEntity<Map<String, Object>>(json, HttpStatus.OK);
		}
		else
		{
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("error", generalnoid + id);
			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.NOT_FOUND);
		}
	}

	// Extra methods. Only admins
	@PostMapping("/inwithstudents")
	@Secured({ MyConstants.ROLE_ADMIN })
	public ResponseEntity<?> inWithStudents(@RequestBody IdList idList)
	{
		Map<String, Object> json = new HashMap<String, Object>();
		List<Object> jsonCourses = new ArrayList<Object>();
		List<Course> courses = courseService.findByIdInFetchStundents(idList.getIds());
		if (!courses.isEmpty())
		{
			for (Course course : courses)
			{
				Map<String, Object> jsonCourse = new HashMap<String, Object>();
				jsonCourse.put("idCourse", course.getId());
				jsonCourse.put("code", course.getCode());
				jsonCourse.put("name", course.getName());
				List<Object> jsonStudents = new ArrayList<Object>();
				for (Student student : course.getStudents())
				{
					Map<String, Object> jsonStudent = new HashMap<String, Object>();
					jsonStudent.put("idStudent", student.getId());
					jsonStudent.put("courseId", student.getCourse().getId());
					jsonStudent.put("rut", student.getRut());
					jsonStudent.put("name", student.getName());
					jsonStudent.put("lastName", student.getLastName());
					jsonStudent.put("age", student.getAge());
					jsonStudents.add(jsonStudent);
				}
				jsonCourse.put("students", jsonStudents);
				jsonCourses.add(jsonCourse);
			}
			json.put("courses", jsonCourses);
			return new ResponseEntity<Map<String, Object>>(json, HttpStatus.OK);
		}
		else
		{
			json.put("error", generalnoid + idList.getIds().toString());
			return new ResponseEntity<Map<String, Object>>(json, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/deletecascade/{id}")
	@Secured({ MyConstants.ROLE_ADMIN })
	public ResponseEntity<?> deleteCascade(@PathVariable Integer id)
	{
		Course currentCourse = courseDao.findById(id).orElse(null);
		if (currentCourse != null)
		{
			courseService.deleteCascade(id);
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("message", deletedeleted + id);
			return new ResponseEntity<Map<String, Object>>(json, HttpStatus.OK);
		}
		else
		{
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("error", generalnoid + id);
			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.NOT_FOUND);
		}
	}
}
