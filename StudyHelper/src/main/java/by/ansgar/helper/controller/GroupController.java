package by.ansgar.helper.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import by.ansgar.helper.entity.Lessons;
import by.ansgar.helper.entity.Students;
import by.ansgar.helper.service.LessonsService;
import by.ansgar.helper.service.StudentService;

@Controller
public class GroupController {
	
	@Autowired
	private StudentService studentService;
	@Autowired
	private LessonsService lessonsService;
	
	@RequestMapping(value="/view_group_{group}")
	public ModelAndView showGroup(@PathVariable String group){
		ModelAndView mav = new ModelAndView();
		try {
			List<Students> studentsByGroup = studentService.getStudentsByGroup(group);
			List<Lessons> lessons = lessonsService.getAllLessons();
			
			mav.addObject("lessons", lessons);
			mav.addObject("group", group);
			mav.addObject("studentsByGroup", studentsByGroup);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		mav.setViewName("group");
		return mav;
	}

}