package by.ansgar.helper.controller;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import by.ansgar.helper.entity.LinkStudentsLessonsRating;
import by.ansgar.helper.entity.Students;
import by.ansgar.helper.service.LinkStudentsLessonsRatingsService;
import by.ansgar.helper.service.StudentService;
import by.ansgar.helper.util.NumbPages;

@Controller
public class StudentsListController {

	private static final Logger LOG = Logger.getLogger(StudentsListController.class);
	
	@Autowired
	private StudentService studentsService;
	@Autowired
	private LinkStudentsLessonsRatingsService lslrService;
	private static long idStudent;
	private static int numPage;
	private static String colName;

	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(value = "/show_all_page_{numPage}_sorting_by_{colName}")
	public ModelAndView showAll(@PathVariable int numPage,
			@PathVariable String colName) {
		ModelAndView mav = new ModelAndView();
		this.numPage = numPage;
		this.colName = colName;
		try {
			List<Students> studentsOnPage = studentsService
					.sortStudents(numPage, colName);
			List<Students> allStudents = studentsService.getAllStudents();
			List<Integer> pages = NumbPages.countPage(allStudents);

			mav.addObject("pages", pages);
			mav.addObject("colName", colName);
			mav.addObject("students", studentsOnPage);
			LOG.info("Show All");
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.warn(e);
		}

		mav.setViewName("students_list");

		return mav;
	}

	@RequestMapping(value = "/delete_student_{id}", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String deleteStudent(@ModelAttribute Students students,
			@PathVariable long id, BindingResult result) {
		try {
			students.setId(id);
			studentsService.deleteStudent(students);
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.warn(e);
		}
		return "forward:/show_all_page_" + numPage + "_sorting_by_" + colName;
	}

	@RequestMapping(value = "/student_profile_{id}", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView showProfile(@PathVariable long id,
			@ModelAttribute Students student, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		idStudent = id;
		try {
			student = studentsService.getStudentById(id);
			List<LinkStudentsLessonsRating> linkStudLes = lslrService
					.getStudLessons(id);
			List<LinkStudentsLessonsRating> lessRatings = lslrService.getLessRatings(id);
			List<LinkStudentsLessonsRating> studRatings = lslrService.getStudRatings(id);

//			mav.addObject("less_rating", lessRatings);
			mav.addObject("stud_rating", studRatings);
			mav.addObject("stud_lessons", linkStudLes);
			mav.addObject("student", student);
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.warn(e);
		}
		mav.setViewName("student_profile");
		return mav;
	}

	@RequestMapping(value = "/edit_student", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String editStudent(@ModelAttribute Students student,
			BindingResult result) {
		try {
			student.setId(idStudent);
			studentsService.editStudent(student);
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.warn(e);
		}
		return "forward:/show_all_page_1_sorting_by_id";

	}

	@ModelAttribute("students")
	public Students students() {
		return new Students();
	}

}
