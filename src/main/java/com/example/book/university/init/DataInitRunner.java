package com.example.book.university.init;

import com.example.book.university.entity.*;
import com.example.book.university.repository.DepartmentRepository;
import com.example.book.university.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitRunner implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {

        // 학과 생성
        Department cs = Department.builder().name("Computer Science").code("CS").build();
        Department ee = Department.builder().name("Electrical Engineering").code("EE").build();
        departmentRepository.save(cs);
        departmentRepository.save(ee);

        // 학생 1 (상세정보 있음)
        StudentDetail detail1 = StudentDetail.builder()
                .address("123 Java St")
                .email("alice@example.com")
                .phoneNumber("010-1234-5678")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        Student student1 = Student.builder()
                .name("Alice")
                .studentNumber("CS001")
                .department(cs)
                .build();
        student1.setDetail(detail1);

        // 학생 2 (상세정보 없음)
        Student student2 = Student.builder()
                .name("Bob")
                .studentNumber("EE001")
                .department(ee)
                .build();

        studentRepository.save(student1);
        studentRepository.save(student2);

        System.out.println("✅ 샘플 데이터 삽입 완료!");
    }
}
