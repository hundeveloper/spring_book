package com.example.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

public class BookDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class BookCreateRequest {
        @NotBlank private String title;
        @NotBlank private String author;
        @NotBlank private String isbn;
        @NotNull private Integer price;
        @NotNull private LocalDate publishDate;

        @NotNull
        private BookDetailRequest detailRequest;  // ✅ 추가
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class BookUpdateRequest {
        @NotBlank private String title;
        @NotBlank private String author;
        @NotNull private Integer price;
        @NotNull private LocalDate publishDate;


        @NotNull
        private BookDetailRequest detailRequest;  // ✅ 추가
    }

    // 📌 책 응답 DTO
    @Getter @Builder
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private int price;
        private LocalDate publishDate;
        private BookDetailResponse detail;

        public static BookResponse from(com.example.book.entity.Book book) {
            return BookResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .detail(BookDetailResponse.from(book.getDetail()))
                    .build();
        }
    }

    // 📌 책 상세정보 요청 DTO
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class BookDetailRequest {
        private String description;
        private String language;
        private int pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    // 📌 책 상세정보 응답 DTO
    @Getter @Builder
    public static class BookDetailResponse {
        private String description;
        private String language;
        private int pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;

        public static BookDetailResponse from(com.example.book.entity.BookDetail detail) {
            if (detail == null) return null;

            return BookDetailResponse.builder()
                    .description(detail.getDescription())
                    .language(detail.getLanguage())
                    .pageCount(detail.getPageCount())
                    .publisher(detail.getPublisher())
                    .coverImageUrl(detail.getCoverImageUrl())
                    .edition(detail.getEdition())
                    .build();
        }
    }
}