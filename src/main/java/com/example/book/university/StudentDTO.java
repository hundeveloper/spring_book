package com.example.book.university.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {

    private Long id;
    private String name;
    private String studentNumber;
    private Long departmentId;
    private StudentDetailDTO detail;
}