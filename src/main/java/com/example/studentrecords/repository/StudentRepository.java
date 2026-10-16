package com.example.studentrecords.repository;

import com.example.studentrecords.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    List<Student> findAll();

    List<Student> search(String keyword);

    Optional<Student> findById(Long id);

    Optional<Student> findByStudentNumber(String studentNumber);

    Student save(Student student);

    void deleteById(Long id);

    long countAll();

    long countCourses();
}