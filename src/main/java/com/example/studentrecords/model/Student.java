package com.example.studentrecords.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Student {

    private Long id;

    @NotBlank(message = "Student number is required")
    @Size(max = 50, message = "Student number must be 50 characters or fewer")
    @Pattern(regexp = "^[A-Za-z0-9]+$",
             message = "Student number can contain only letters and digits")
    private String studentNumber;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be 2 to 100 characters")
    @Pattern(regexp = "^[\\p{L}][\\p{L} '\\-]*$",
             message = "First name can contain only letters, spaces, hyphens and apostrophes")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be 2 to 100 characters")
    @Pattern(regexp = "^[\\p{L}][\\p{L} '\\-]*$",
             message = "Last name can contain only letters, spaces, hyphens and apostrophes")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Size(max = 150, message = "Email must be 150 characters or fewer")
    @Email(message = "Enter a valid email address")
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[A-Za-z]{2,}$",
             message = "Email must look like name@example.com")
    private String email;

    @NotBlank(message = "Course is required")
    @Size(max = 150, message = "Course must be 150 characters or fewer")
    @Pattern(regexp = "^[\\p{L}][\\p{L} &,'()\\-]*$",
             message = "Course can contain only letters, spaces and basic punctuation")
    private String course;

    public Student() {
    }

    public Student(Long id, String studentNumber, String firstName,
                   String lastName, String email, String course) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.course = course;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
}