package com.example.book.university.repository;

import com.example.book.university.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Department findByCode(String code);

    // 학생 수 조회: 특정 학과 ID를 기준으로
    @Query("SELECT COUNT(s) FROM Student s WHERE s.department.id = :departmentId")
    Long countStudentByDepartment(Long departmentId);
}