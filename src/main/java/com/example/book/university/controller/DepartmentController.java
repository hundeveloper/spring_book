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
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;

    // 학과 생성
    @PostMapping
    public Department create(@RequestBody Department dept) {
        return departmentRepository.save(dept);
    }

    // 전체 학과 조회
    @GetMapping
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    // ID로 학과 조회
    @GetMapping("/{id}")
    public ResponseEntity<Department> getById(@PathVariable Long id) {
        return departmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // code로 학과 조회
    @GetMapping("/code/{code}")
    public ResponseEntity<Department> getByCode(@PathVariable String code) {
        Department dept = departmentRepository.findByCode(code);
        if (dept != null) {
            return ResponseEntity.ok(dept);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 학과 수정
    @PutMapping("/{id}")
    public ResponseEntity<Department> update(@PathVariable Long id, @RequestBody Department update) {
        return departmentRepository.findById(id)
                .map(dept -> {
                    dept.setName(update.getName());
                    dept.setCode(update.getCode());
                    return ResponseEntity.ok(departmentRepository.save(dept));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 학과 삭제 (학생이 없을 때만)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return departmentRepository.findById(id).map(dept -> {
            if (!dept.getStudents().isEmpty()) {
                return ResponseEntity.status(409).body("Cannot delete department with id: " + id + ". It has " + dept.getStudents().size() + " students");
            }
            departmentRepository.delete(dept);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // 학과별 학생 수 조회
    @GetMapping("/{id}/student-count")
    public Long getStudentCount(@PathVariable Long id) {
        return departmentRepository.countStudentByDepartment(id);
    }

    // 학과별 학생 목록 조회
    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudents(@PathVariable Long id) {
        return departmentRepository.findById(id).map(dept -> {
            List<Student> students = studentRepository.findByDepartment(dept);
            return ResponseEntity.ok(students);
        }).orElse(ResponseEntity.notFound().build());
    }
}