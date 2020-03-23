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

	@Value("${StudentRestController.create.idnotnull}")
	private String createidnotnull;

	@Value("${StudentRestController.general.noid}")
	private String generalnoid;

	@Value("${StudentRestController.delete.deleted}")
	private String deletedeleted;

	@GetMapping("/list/{page}")
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_TEACHER })
	public ResponseEntity<?> list(@PathVariable Integer page)
	{
		int rows = 10;
		Map<String, Object> json = new HashMap<String, Object>();
		List<Object> jsonStudents = new ArrayList<Object>();
		Pageable pageable = PageRequest.of(page, rows);
		Page<Student> students = studentDao.findAllByOrderByIdAsc(pageable);
		for (Student student : students)
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
		json.put("totalPages", students.getTotalPages());
		json.put("currentPage", students.getNumber());
		json.put("rowsPerPage", students.getSize());
		json.put("rowsInThisPage", students.getNumberOfElements());
		json.put("totalRows", students.getTotalElements());
		json.put("students", jsonStudents);
		if (students.getNumber() > students.getTotalPages() - 1) return new ResponseEntity<Map<String, Object>>(json, HttpStatus.NOT_FOUND);
		else return new ResponseEntity<Map<String, Object>>(json, HttpStatus.OK);
	}

	@GetMapping("/all")
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_TEACHER })
	public ResponseEntity<?> all()
	{
		Map<String, Object> json = new HashMap<String, Object>();
		List<Object> jsonStudents = new ArrayList<Object>();
		List<Student> students = studentDao.findAll();
		for (Student student : students)
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
		json.put("students", jsonStudents);
		return new ResponseEntity<Map<String, Object>>(json, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_TEACHER })
	public ResponseEntity<?> show(@PathVariable Integer id)
	{
		Student student = studentDao.findById(id).orElse(null);
		if (student != null)
		{
			Map<String, Object> jsonStudent = new HashMap<String, Object>();
			jsonStudent.put("idStudent", student.getId());
			jsonStudent.put("courseId", student.getCourse().getId());
			jsonStudent.put("rut", student.getRut());
			jsonStudent.put("name", student.getName());
			jsonStudent.put("lastName", student.getLastName());
			jsonStudent.put("age", student.getAge());
			return new ResponseEntity<Map<String, Object>>(jsonStudent, HttpStatus.OK);
		}
		else
		{
			Map<String, String> errors = new HashMap<String, String>();
			errors.put("message", generalnoid + id);
			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/create")
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_TEACHER })
	public ResponseEntity<?> create(@RequestBody Student student)
	{
		Map<String, Object> json = new HashMap<String, Object>();
		if (student.getId() == null)
		{
			List<String> errors = studentService.validateStudent(student);
			if (errors.isEmpty())
			{
				student.setRut(student.getRut().toUpperCase());// Always save with capital K
				student.setCourse(new Course());
				student.getCourse().setId(student.getCourseId());
				try
				{
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
			json.put("error", createidnotnull);
			return new ResponseEntity<Map<String, Object>>(json, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/{id}")
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_TEACHER })
	public ResponseEntity<?> edit(@PathVariable Integer id, @RequestBody Student student)
	{
		Map<String, Object> json = new HashMap<String, Object>();
		Student currentStudent = studentDao.findById(id).orElse(null);
		if (currentStudent != null)
		{
			List<String> errors = studentService.validateStudent(student);
			if (errors.isEmpty())
			{
				student.setCourse(new Course());
				student.getCourse().setId(student.getCourseId());
				student.setId(id);
				try
				{
					student = studentDao.save(student);
					json.put("idStudent", student.getId());
					json.put("courseId", student.getCourse().getId());
					json.put("rut", student.getRut());
					json.put("name", student.getName());
					json.put("lastName", student.getLastName());
					json.put("age", student.getAge());
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
				json.put("errors", errors);
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
	@Secured({ MyConstants.ROLE_ADMIN, MyConstants.ROLE_TEACHER })
	public ResponseEntity<?> delete(@PathVariable Integer id)
	{
		Student currentStudent = studentDao.findById(id).orElse(null);
		if (currentStudent != null)
		{
			studentDao.deleteById(id);
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
