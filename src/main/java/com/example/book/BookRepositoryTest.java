package com.example.book;

import com.example.book.entity.Book;
import com.example.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;


    @BeforeEach
    void setUp() {
        bookRepository.deleteAll(); // 💥 테스트 실행 전마다 테이블 비움
    }
    @Test
    @DisplayName("도서 등록 테스트")
    void testCreateBook() {
        Book book = Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build();

        Book saved = bookRepository.save(book);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("스프링 부트 입문");
    }

    @Test
    @DisplayName("ISBN으로 도서 조회 테스트")
    void testFindByIsbn() {
        // 먼저 테스트용 데이터 삽입
        bookRepository.save(Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build());

        List<Book> result = bookRepository.findAllByIsbn("9788956746425");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getAuthor()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("저자명으로 도서 목록 조회 테스트")
    void testFindByAuthor() {
        // 🔧 먼저 데이터 저장
        bookRepository.save(Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build());

        // 🧪 조회
        List<Book> books = bookRepository.findByAuthor("홍길동");

        assertThat(books).isNotEmpty();
        assertThat(books.get(0).getTitle()).contains("스프링");
    }
    @Test
    @DisplayName("도서 정보 수정 테스트")
    void testUpdateBook() {
        Book saved = bookRepository.save(Book.builder()
                .title("JPA 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build());

        saved.setPrice(32000);
        Book updated = bookRepository.save(saved);

        assertThat(updated.getPrice()).isEqualTo(32000);
    }

    @Test
    @DisplayName("도서 삭제 테스트")
    void testDeleteBook() {
        Book saved = bookRepository.save(Book.builder()
                .title("JPA 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build());

        bookRepository.delete(saved);

        Optional<Book> result = bookRepository.findById(saved.getId());
        assertThat(result).isEmpty();
    }


}