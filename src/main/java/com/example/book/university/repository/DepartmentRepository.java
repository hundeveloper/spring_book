package com.example.book.university.repository;

import com.example.book.university.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Department findByCode(String code);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.department.id = :departmentId")
    Long countStudnetByDepartment(Long departmentId); // 오타 원형대로 유지
}