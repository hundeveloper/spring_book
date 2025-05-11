package com.example.book.university.controller;

import com.example.book.university.entity.Department;
import com.example.book.university.entity.Student;
import com.example.book.university.repository.DepartmentRepository;
import com.example.book.university.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Department dept = departmentRepository.findById(student.getDepartment().getId())
                .orElseThrow(() -> new IllegalArgumentException("학과 없음"));
        student.setDepartment(dept);
        return ResponseEntity.ok(studentRepository.save(student));
    }

    @GetMapping
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student update) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(update.getName());
                    student.setStudentNumber(update.getStudentNumber());

                    Department dept = departmentRepository.findById(update.getDepartment().getId())
                            .orElseThrow(() -> new IllegalArgumentException("학과 없음"));
                    student.setDepartment(dept);

                    return ResponseEntity.ok(studentRepository.save(student));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) return ResponseEntity.notFound().build();
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{departmentId}")
    public List<Student> getByDepartment(@PathVariable Long departmentId) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("학과 없음"));
        return studentRepository.findByDepartment(dept);
    }
}