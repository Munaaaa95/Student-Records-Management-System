package com.example.studentrecords.repository;

import com.example.studentrecords.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class MySqlStudentRepository implements StudentRepository {

    private final JdbcTemplate jdbc;

    public MySqlStudentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Student> rowMapper = (rs, rowNum) -> new Student(
            rs.getLong("id"),
            rs.getString("student_number"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("course")
    );

    @Override
    public List<Student> findAll() {
        return jdbc.query("SELECT * FROM students ORDER BY id", rowMapper);
    }

    @Override
    public List<Student> search(String keyword) {
        String like = "%" + keyword + "%";
        return jdbc.query(
                "SELECT * FROM students WHERE first_name LIKE ? "
                        + "OR last_name LIKE ? "
                        + "OR CONCAT(first_name, ' ', last_name) LIKE ? "
                        + "OR student_number LIKE ? "
                        + "OR course LIKE ? "
                        + "ORDER BY id",
                rowMapper, like, like, like, like, like);
    }

    @Override
    public Optional<Student> findById(Long id) {
        List<Student> results = jdbc.query(
                "SELECT * FROM students WHERE id = ?", rowMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public Optional<Student> findByStudentNumber(String studentNumber) {
        List<Student> results = jdbc.query(
                "SELECT * FROM students WHERE student_number = ?",
                rowMapper, studentNumber);
        return results.stream().findFirst();
    }

    @Override
    public Student save(Student student) {
        if (student.getId() == null) {
            insert(student);
        } else {
            jdbc.update(
                    "UPDATE students SET student_number = ?, first_name = ?, "
                            + "last_name = ?, email = ?, course = ? WHERE id = ?",
                    student.getStudentNumber(), student.getFirstName(),
                    student.getLastName(), student.getEmail(),
                    student.getCourse(), student.getId());
        }
        return student;
    }

    private void insert(Student student) {
        String sql = "INSERT INTO students "
                + "(student_number, first_name, last_name, email, course) "
                + "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getStudentNumber());
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getLastName());
            ps.setString(4, student.getEmail());
            ps.setString(5, student.getCourse());
            return ps;
        }, keyHolder);
        student.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void deleteById(Long id) {
        jdbc.update("DELETE FROM students WHERE id = ?", id);
    }

        @Override
    public long countAll() {
        Long n = jdbc.queryForObject("SELECT COUNT(*) FROM students", Long.class);
        return n == null ? 0 : n;
    }

    @Override
    public long countCourses() {
        Long n = jdbc.queryForObject(
                "SELECT COUNT(DISTINCT course) FROM students", Long.class);
        return n == null ? 0 : n;
    }
}