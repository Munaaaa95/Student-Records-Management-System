package com.example.studentrecords.controller;

import com.example.studentrecords.model.Student;
import com.example.studentrecords.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.setDisallowedFields("id");
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }

    @GetMapping("/students")
    public String list(@RequestParam(required = false) String q, Model model) {
        String keyword = (q == null) ? "" : q.trim();
        model.addAttribute("students",
                keyword.isEmpty() ? repository.findAll() : repository.search(keyword));
        model.addAttribute("q", keyword);
        model.addAttribute("totalStudents", repository.countAll());
        model.addAttribute("totalCourses", repository.countCourses());
        return "students/list";
    }

    @GetMapping("/students/new")
    public String newForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/form";
    }

    @PostMapping("/students")
    public String create(@Valid @ModelAttribute("student") Student student,
                         BindingResult result,
                         RedirectAttributes redirect) {
        checkDuplicateNumber(student, result);
        if (result.hasErrors()) {
            return "students/form";
        }
        repository.save(student);
        redirect.addFlashAttribute("message",
                student.getFirstName() + " " + student.getLastName() + " was added.");
        return "redirect:/students";
    }

    @GetMapping("/students/{id}/edit")
    public String editForm(@PathVariable Long id, Model model,
                           RedirectAttributes redirect) {
        Student student = repository.findById(id).orElse(null);
        if (student == null) {
            redirect.addFlashAttribute("error", "That student could not be found.");
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        return "students/form";
    }

    @PostMapping("/students/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("student") Student student,
                         BindingResult result,
                         RedirectAttributes redirect) {
        student.setId(id);
        checkDuplicateNumber(student, result);
        if (result.hasErrors()) {
            return "students/form";
        }
        repository.save(student);
        redirect.addFlashAttribute("message",
                student.getFirstName() + " " + student.getLastName() + " was updated.");
        return "redirect:/students";
    }

    @PostMapping("/students/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        Student student = repository.findById(id).orElse(null);
        if (student == null) {
            redirect.addFlashAttribute("error", "That student could not be found.");
        } else {
            repository.deleteById(id);
            redirect.addFlashAttribute("message",
                    student.getFirstName() + " " + student.getLastName() + " was deleted.");
        }
        return "redirect:/students";
    }

    private void checkDuplicateNumber(Student student, BindingResult result) {
        if (result.hasFieldErrors("studentNumber")) {
            return;
        }
        repository.findByStudentNumber(student.getStudentNumber())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(student.getId())) {
                        result.rejectValue("studentNumber", "duplicate",
                                "This student number already exists");
                    }
                });
    }
}