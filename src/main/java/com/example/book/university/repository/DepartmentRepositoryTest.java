package com.example.book.university.repository;

import com.example.book.university.entity.Department;
import com.example.book.university.entity.Student;
import com.example.book.university.entity.StudentDetail;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentDepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @DisplayName("학생 등록 및 학번으로 조회")
    @Transactional
    void testCreateAndFindByStudentNumber() {
        // 학과 먼저 저장
        Department dept = Department.builder()
                .name("Physics")
                .code("PHYS")
                .build();
        departmentRepository.save(dept);

        // 학생 생성
        Student student = Student.builder()
                .name("David")
                .studentNumber("PHYS001")
                .department(dept)
                .build();

        // 상세정보 설정
        student.setDetail(StudentDetail.builder()
                .email("david@example.com")
                .phoneNumber("010-8888-8888")
                .address("Quantum Street")
                .dateOfBirth(LocalDate.of(2001, 3, 3))
                .build());

        studentRepository.save(student);

        // 학번으로 조회
        Student found = studentRepository.findByStudentNumber("PHYS001");

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("David");
        assertThat(found.getDepartment().getCode()).isEqualTo("PHYS");
        assertThat(found.getDetail().getEmail()).isEqualTo("david@example.com");
    }

    @Test
    @DisplayName("학과로 학생 목록 조회")
    void testFindByDepartment() {
        Department dept = Department.builder()
                .name("Mathematics")
                .code("MATH")
                .build();
        departmentRepository.save(dept);

        Student s1 = Student.builder().name("Alice").studentNumber("MATH001").department(dept).build();
        Student s2 = Student.builder().name("Bob").studentNumber("MATH002").department(dept).build();

        studentRepository.saveAll(List.of(s1, s2));

        List<Student> students = studentRepository.findByDepartment(dept);

        assertThat(students).hasSize(2);
        assertThat(students).extracting("name").containsExactlyInAnyOrder("Alice", "Bob");
    }
}