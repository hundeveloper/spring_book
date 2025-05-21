package com.example.book.university.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDetailDTO {

    private String address;
    private String phoneNumber;
    private String email;
    private LocalDate dateOfBirth;
}