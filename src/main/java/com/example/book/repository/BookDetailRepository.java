package com.example.book.repository;

import com.example.book.entity.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface BookDetailRepository extends JpaRepository<BookDetail, Long> {

    // 📌 Book ID로 BookDetail 조회
    Optional<BookDetail> findByBookId(Long bookId);

    // 📌 BookDetail + Book 을 함께 로딩
    @Query("SELECT bd FROM BookDetail bd JOIN FETCH bd.book WHERE bd.id = :id")
    Optional<BookDetail> findByIdWithBook(Long id);

    // 📌 특정 출판사 책들 조회
    List<BookDetail> findByPublisher(String publisher);
}