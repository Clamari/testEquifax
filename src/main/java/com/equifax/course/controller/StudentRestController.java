package com.equifax.course.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equifax.course.core.MyConstants;
import com.equifax.course.model.dao.StudentDao;
import com.equifax.course.model.domain.Course;
import com.equifax.course.model.domain.Student;
import com.equifax.course.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentRestController
{
	@Autowired
	private StudentDao studentDao;

	@Autowired
	private StudentService studentService;

	@Value("${StudentRestController.create.invalidrut}")
	private String createinvalidrut;

	@Value("${StudentRestController.create.invalidage}")
	private String createinvalidage;

	@Value("${StudentRestController.create.nullcourse}")
	private String createnullcourse;

	@Value("${StudentRestController.create.idnotnull}")
	private String createidnotnull;

//	@GetMapping("/list/{page}")
//	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_DIRECTOR })
//	public ResponseEntity<?> list(@PathVariable Integer page)
//	{
//		int rows = 10;
//		Map<String, Object> json = new HashMap<String, Object>();
//		List<Object> jsonCourses = new ArrayList<Object>();
//		Pageable pageable = PageRequest.of(page, rows);
//		Page<Course> courses = courseDao.findAllByOrderByIdAsc(pageable);
//		for (Course course : courses)
//		{
//			Map<String, Object> jsonCourse = new HashMap<String, Object>();
//			jsonCourse.put("idCourse", course.getId());
//			jsonCourse.put("code", course.getCode());
//			jsonCourse.put("name", course.getName());
//			jsonCourses.add(jsonCourse);
//		}
//		json.put("totalPages", courses.getTotalPages());
//		json.put("currentPage", courses.getNumber());
//		json.put("rowsPerPage", courses.getSize());
//		json.put("rowsInThisPage", courses.getNumberOfElements());
//		json.put("totalRows", courses.getTotalElements());
//		json.put("courses", jsonCourses);
//		if (courses.getNumber() > courses.getTotalPages()-1) return new ResponseEntity<Map<String, Object>>(json, HttpStatus.NOT_FOUND);
//		else return new ResponseEntity<Map<String, Object>>(json, HttpStatus.OK);
//	}
//
//	@GetMapping("/all")
//	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_DIRECTOR })
//	public ResponseEntity<?> all()
//	{
//		Map<String, Object> json = new HashMap<String, Object>();
//		List<Object> jsonCourses = new ArrayList<Object>();
//		List<Course> courses = courseDao.findAll();
//		for (Course course : courses)
//		{
//			Map<String, Object> jsonCourse = new HashMap<String, Object>();
//			jsonCourse.put("idCourse", course.getId());
//			jsonCourse.put("code", course.getCode());
//			jsonCourse.put("name", course.getName());
//			jsonCourses.add(jsonCourse);
//		}
//		json.put("courses", jsonCourses);
//		return new ResponseEntity<Map<String, Object>>(json, HttpStatus.OK);
//	}
//
//	@GetMapping("/{id}")
//	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_DIRECTOR })
//	public ResponseEntity<?> show(@PathVariable Integer id)
//	{
//		Course course = courseDao.findById(id).orElse(null);
//		if (course != null)
//		{
//			Map<String, Object> jsonCourse = new HashMap<String, Object>();
//			jsonCourse.put("idCourse", course.getId());
//			jsonCourse.put("code", course.getCode());
//			jsonCourse.put("name", course.getName());
//			return new ResponseEntity<Map<String, Object>>(jsonCourse, HttpStatus.OK);
//		}
//		else
//		{
//			Map<String, String> errors = new HashMap<String, String>();
//			errors.put("message", generalnoid + id);
//			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.NOT_FOUND);
//		}
//	}
//
	@PostMapping("/create")
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_TEACHER })
	public ResponseEntity<?> create(@RequestBody Student student)
	{
		Map<String, Object> json = new HashMap<String, Object>();
		if (student.getId() == null)
		{
			List<String> errors = new ArrayList<String>();
			if (!studentService.validateRut(student.getRut())) errors.add(createinvalidrut);
			if (student.getAge() < 18) errors.add(createinvalidage);
			if (student.getCourseId() == null) errors.add(createnullcourse);
			if (errors.isEmpty())
			{
				student.setRut(student.getRut().toUpperCase());// Always save with capital K
				try
				{
					student.setCourse(new Course());
					student.getCourse().setId(student.getCourseId());
					student = studentDao.save(student);
					json.put("idStudent", student.getId());
					json.put("courseId", student.getCourse().getId());
					json.put("rut", student.getRut());
					json.put("name", student.getName());
					json.put("lastName", student.getLastName());
					json.put("age", student.getAge());
					return new ResponseEntity<Map<String, Object>>(json, HttpStatus.CREATED);
				}
				catch (Exception e)
				{
					student = null;
					json.put("error", e.getMessage());
					return new ResponseEntity<Map<String, Object>>(json, HttpStatus.BAD_REQUEST);
				}
			}
			else
			{
				json.put("errors", errors);
				return new ResponseEntity<Map<String, Object>>(json, HttpStatus.BAD_REQUEST);
			}
		}
		else
		{
			student = null;
			json.put("error", createidnotnull);
			return new ResponseEntity<Map<String, Object>>(json, HttpStatus.BAD_REQUEST);
		}
	}
//
//	@PutMapping("/edit/{id}")
//	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_DIRECTOR })
//	public ResponseEntity<?> edit(@PathVariable Integer id, @RequestBody Course course)
//	{
//		try
//		{
//			Course currentCourse = courseDao.findById(id).orElse(null);
//			if (currentCourse != null)
//			{
//				course.setId(id);
//				course = courseDao.save(course);
//				Map<String, Object> jsonCourse = new HashMap<String, Object>();
//				jsonCourse.put("idCourse", course.getId());
//				jsonCourse.put("code", course.getCode());
//				jsonCourse.put("name", course.getName());
//				return new ResponseEntity<Map<String, Object>>(jsonCourse, HttpStatus.OK);
//			}
//			else
//			{
//				Map<String, String> errors = new HashMap<String, String>();
//				errors.put("message", generalnoid + id);
//				return new ResponseEntity<Map<String, String>>(errors, HttpStatus.NOT_FOUND);
//			}
//		}
//		catch (Exception e)
//		{
//			course = null;
//			Map<String, String> errors = new HashMap<String, String>();
//			errors.put("message", e.getMessage());
//			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
//		}
//	}
//
//	@DeleteMapping("/delete/{id}")
//	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_DIRECTOR })
//	public ResponseEntity<?> delete(@PathVariable Integer id)
//	{
//		Course currentCourse = courseDao.findById(id).orElse(null);
//		if (currentCourse != null)
//		{
//			courseDao.deleteById(id);
//			Map<String, Object> json = new HashMap<String, Object>();
//			json.put("message", deletedeleted + id);
//			return new ResponseEntity<Map<String, Object>>(json, HttpStatus.OK);
//		}
//		else
//		{
//			Map<String, String> errors = new HashMap<String, String>();
//			errors.put("message", generalnoid + id);
//			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.NOT_FOUND);
//		}
//	}
}
