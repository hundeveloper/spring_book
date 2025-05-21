package com.example.book.university.service;

import com.example.book.university.dto.DepartmentDTO;
import com.example.book.university.entity.Department;
import com.example.book.university.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        Department department = Department.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .build();
        return toDTO(departmentRepository.save(department));
    }

    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DepartmentDTO getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
    }

    public DepartmentDTO getDepartmentByCode(String code) {
        Department department = departmentRepository.findByCode(code);
        if (department == null) throw new EntityNotFoundException("Department not found with code: " + code);
        return toDTO(department);
    }

    public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));

        department.setName(dto.getName());
        department.setCode(dto.getCode());
        return toDTO(departmentRepository.save(department));
    }

    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));

        if (!department.getStudents().isEmpty()) {
            throw new IllegalStateException("Cannot delete department with id: " + id + ". It has " + department.getStudents().size() + " students");
        }

        departmentRepository.delete(department);
    }

    public Long countStudentsByDepartment(Long departmentId) {
        return departmentRepository.countStudentByDepartment(departmentId);
    }

    private DepartmentDTO toDTO(Department dept) {
        return DepartmentDTO.builder()
                .id(dept.getId())
                .name(dept.getName())
                .code(dept.getCode())
                .build();
    }
}