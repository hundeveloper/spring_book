package com.example.book.university.repository;

import com.example.book.university.entity.Student;
import com.example.book.university.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByStudentNumber(String studentNumber);

    List<Student> findByDepartment(Department department);
}