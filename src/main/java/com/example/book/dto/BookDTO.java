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
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class BookUpdateRequest {
        @NotBlank private String title;
        @NotBlank private String author;
        @NotNull private Integer price;
        @NotNull private LocalDate publishDate;
    }

    @Getter @Builder
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;

        public static BookResponse from(com.example.book.entity.Book book) {
            return BookResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .build();
        }
    }
}