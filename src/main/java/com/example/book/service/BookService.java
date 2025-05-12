package com.example.book.service;

import com.example.book.dto.BookDTO.*;
import com.example.book.entity.Book;
import com.example.book.entity.BookDetail;
import com.example.book.exception.BusinessException;
import com.example.book.repository.BookDetailRepository;
import com.example.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;

    // 📘 도서 등록
    @Transactional
    public BookResponse saveBook(BookCreateRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("이미 등록된 ISBN입니다.");
        }

        // BookDetail 생성
        BookDetail detail = BookDetail.builder()
                .description(request.getDetailRequest().getDescription())
                .language(request.getDetailRequest().getLanguage())
                .pageCount(request.getDetailRequest().getPageCount())
                .publisher(request.getDetailRequest().getPublisher())
                .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
                .edition(request.getDetailRequest().getEdition())
                .build();

        // Book 생성 및 연관관계 설정
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();
        book.setBookDetail(detail); // 연관관계 메서드

        Book saved = bookRepository.save(book); // Cascade로 BookDetail도 저장됨
        return BookResponse.from(saved);
    }

    // 📚 전체 도서 조회
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookResponse::from)
                .toList();
    }

    // 🔍 ID로 도서 조회
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. (id: " + id + ")"));
        return BookResponse.from(book);
    }

    // ISBN으로 도서 조회
    @Transactional(readOnly = true)
    public BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException("해당 ISBN의 도서를 찾을 수 없습니다."));
        return BookResponse.from(book);
    }

    // ✏️ 도서 정보 수정 (Book + BookDetail)
    @Transactional
    public BookResponse updateBook(Long id, BookUpdateRequest update) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. (id: " + id + ")"));

        book.setTitle(update.getTitle());
        book.setAuthor(update.getAuthor());
        book.setPrice(update.getPrice());
        book.setPublishDate(update.getPublishDate());

        BookDetail detail = book.getBookDetail();

        if (detail == null) {
            detail = new BookDetail();
            book.setBookDetail(detail); // 새로 연결
        }

        detail.setDescription(update.getDetailRequest().getDescription());
        detail.setLanguage(update.getDetailRequest().getLanguage());
        detail.setPageCount(update.getDetailRequest().getPageCount());
        detail.setPublisher(update.getDetailRequest().getPublisher());
        detail.setCoverImageUrl(update.getDetailRequest().getCoverImageUrl());
        detail.setEdition(update.getDetailRequest().getEdition());

        return BookResponse.from(bookRepository.save(book));
    }

    // ❌ 도서 삭제
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("삭제할 도서를 찾을 수 없습니다. (id: " + id + ")");
        }
        bookRepository.deleteById(id);
    }

    // 🔍 저자로 검색
    @Transactional(readOnly = true)
    public List<BookResponse> searchByAuthor(String author) {
        return bookRepository.findAll().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .map(BookResponse::from)
                .toList();
    }

    // 🔍 제목으로 검색
    @Transactional(readOnly = true)
    public List<BookResponse> searchByTitle(String title) {
        return bookRepository.findAll().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .map(BookResponse::from)
                .toList();
    }
}