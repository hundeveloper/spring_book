package com.example.book.university.controller;

import com.example.book.university.entity.Department;
import com.example.book.university.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    @PostMapping
    public Department create(@RequestBody Department dept) {
        return departmentRepository.save(dept);
    }

    @GetMapping
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getById(@PathVariable Long id) {
        return departmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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

    @GetMapping("/{id}/student-count")
    public Long getStudentCount(@PathVariable Long id) {
        return departmentRepository.countStudnetByDepartment(id);
    }
}