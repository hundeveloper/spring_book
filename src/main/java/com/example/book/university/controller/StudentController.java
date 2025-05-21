package com.example.book.university.controller;

import com.example.book.university.dto.StudentDetailDTO;
import com.example.book.university.entity.Department;
import com.example.book.university.entity.Student;
import com.example.book.university.entity.StudentDetail;
import com.example.book.university.repository.DepartmentRepository;
import com.example.book.university.repository.StudentRepository;
import jakarta.transaction.Transactional;
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

    // 학생 등록
    @PostMapping
    @Transactional
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Department dept = departmentRepository.findById(student.getDepartment().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다."));
        student.setDepartment(dept);

        if (student.getDetail() != null) {
            student.getDetail().setStudent(student);  // 양방향 연결
        }

        return ResponseEntity.ok(studentRepository.save(student));
    }

    // 전체 학생 조회
    @GetMapping
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    // ID로 학생 조회
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 학번으로 학생 조회
    @GetMapping("/number/{studentNumber}")
    public ResponseEntity<Student> getByStudentNumber(@PathVariable String studentNumber) {
        Student student = studentRepository.findByStudentNumber(studentNumber);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // 학생 정보 수정
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student update) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(update.getName());
                    student.setStudentNumber(update.getStudentNumber());

                    Department dept = departmentRepository.findById(update.getDepartment().getId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다."));
                    student.setDepartment(dept);

                    if (update.getDetail() != null) {
                        if (student.getDetail() == null) {
                            update.getDetail().setStudent(student);
                            student.setDetail(update.getDetail());
                        } else {
                            StudentDetail detail = student.getDetail();
                            detail.setAddress(update.getDetail().getAddress());
                            detail.setPhoneNumber(update.getDetail().getPhoneNumber());
                            detail.setEmail(update.getDetail().getEmail());
                            detail.setDateOfBirth(update.getDetail().getDateOfBirth());
                        }
                    } else {
                        student.setDetail(null); // 상세정보 제거 가능
                    }

                    return ResponseEntity.ok(studentRepository.save(student));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 학생 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) return ResponseEntity.notFound().build();
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 학과별 학생 조회
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Student>> getByDepartment(@PathVariable Long departmentId) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다."));
        return ResponseEntity.ok(studentRepository.findByDepartment(dept));
    }
}