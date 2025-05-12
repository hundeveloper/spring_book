package com.example.book.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private Integer price;
    private LocalDate publishDate;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BookDetail detail;

    // 🔧 연관관계 편의 메서드 (★직접 작성 필요)
    public void setBookDetail(BookDetail detail) {
        this.detail = detail;
        if (detail != null) {
            detail.setBook(this); // BookDetail에서 setBook도 같이 설정되도록
        }
    }

    public BookDetail getBookDetail() {
        return this.detail;
    }
}