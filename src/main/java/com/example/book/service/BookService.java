package com.example.book.service;

import com.example.book.dto.BookDTO.*;
import com.example.book.entity.Book;
import com.example.book.exception.BusinessException;
import com.example.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // 📘 도서 등록
    @Transactional
    public BookResponse saveBook(BookCreateRequest request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();
        return BookResponse.from(bookRepository.save(book));
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
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. (id: " + id + ")"));
        return BookResponse.from(book);
    }

    // 🔍 ISBN으로 도서 조회
    @Transactional(readOnly = true)
    public BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) throw new BusinessException("해당 ISBN의 도서를 찾을 수 없습니다.");
        return BookResponse.from(book);
    }

    // ✏️ 도서 정보 수정
    @Transactional
    public BookResponse updateBook(Long id, BookUpdateRequest update) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. (id: " + id + ")"));

        book.setTitle(update.getTitle());
        book.setAuthor(update.getAuthor());
        book.setPrice(update.getPrice());
        book.setPublishDate(update.getPublishDate());

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
}