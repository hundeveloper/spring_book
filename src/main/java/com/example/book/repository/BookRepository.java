package com.example.book.repository;

import com.example.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    // 기존 기능 유지
    Book findByIsbn(String isbn);
    List<Book> findByAuthor(String author);
    List<Book> findAllByIsbn(String isbn);

    // 1:1 관계 - Book과 BookDetail을 함께 로딩 (ID 기준)
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.detail WHERE b.id = :id")
    Optional<Book> findByIdWithBookDetail(Long id);

    // 1:1 관계 - Book과 BookDetail을 함께 로딩 (ISBN 기준)
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.detail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithBookDetail(String isbn);

    // ISBN 중복 확인용
    boolean existsByIsbn(String isbn);
}