package com.example.book.university.service;

import com.example.book.university.dto.StudentDTO;
import com.example.book.university.dto.StudentDetailDTO;
import com.example.book.university.entity.*;
import com.example.book.university.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    public StudentDTO createStudent(StudentDTO dto) {
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + dto.getDepartmentId()));

        Student student = Student.builder()
                .name(dto.getName())
                .studentNumber(dto.getStudentNumber())
                .department(department)
                .build();

        // 상세 정보가 있으면 생성
        if (dto.getDetail() != null) {
            student.setDetail(toDetailEntity(dto.getDetail()));
        }

        return toDTO(studentRepository.save(student));
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
        return toDTO(student);
    }

    public StudentDTO getStudentByStudentNumber(String number) {
        Student student = studentRepository.findByStudentNumber(number);
        if (student == null) throw new EntityNotFoundException("Student not found with studentNumber: " + number);
        return toDTO(student);
    }

    public List<StudentDTO> getStudentsByDepartment(Long departmentId) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + departmentId));
        return studentRepository.findByDepartment(dept).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO updateStudent(Long id, StudentDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + dto.getDepartmentId()));

        student.setName(dto.getName());
        student.setStudentNumber(dto.getStudentNumber());
        student.setDepartment(department);

        if (dto.getDetail() != null) {
            StudentDetail detail = student.getDetail();
            if (detail == null) {
                detail = toDetailEntity(dto.getDetail());
            } else {
                updateDetailEntity(detail, dto.getDetail());
            }
            student.setDetail(detail);
        } else {
            student.setDetail(null);
        }

        return toDTO(studentRepository.save(student));
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // ------------------- 변환 메서드 ------------------------

    private StudentDTO toDTO(Student student) {
        StudentDetailDTO detailDTO = null;
        if (student.getDetail() != null) {
            detailDTO = StudentDetailDTO.builder()
                    .address(student.getDetail().getAddress())
                    .email(student.getDetail().getEmail())
                    .phoneNumber(student.getDetail().getPhoneNumber())
                    .dateOfBirth(student.getDetail().getDateOfBirth())
                    .build();
        }

        return StudentDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .studentNumber(student.getStudentNumber())
                .departmentId(student.getDepartment().getId())
                .detail(detailDTO)
                .build();
    }

    private StudentDetail toDetailEntity(StudentDetailDTO dto) {
        return StudentDetail.builder()
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .dateOfBirth(dto.getDateOfBirth())
                .build();
    }

    private void updateDetailEntity(StudentDetail entity, StudentDetailDTO dto) {
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEmail(dto.getEmail());
        entity.setDateOfBirth(dto.getDateOfBirth());
    }
}