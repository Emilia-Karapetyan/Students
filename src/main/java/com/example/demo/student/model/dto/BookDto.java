package com.example.demo.student.model.dto;

import com.example.demo.student.model.Author;
import com.example.demo.student.model.Book;
import com.example.demo.student.model.Genre;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class BookDto {
    private String title;
    private String date;
    private Long publisherId;
    private String publisherName;

    private List<Long> authorsId;
    private List<Long> genresId;

    private List<String> authorsName;
    private List<String> genresName;

    public BookDto(String title, String date, String publisherName, List<String> authorsName, List<String> genresName) {
        this.title = title;
        this.date = date;
        this.publisherName = publisherName;
        this.authorsName = authorsName;
        this.genresName = genresName;
    }

    public static Book toEntity(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setDate(bookDto.getDate());
        return book;
    }


    public static BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setTitle(book.getTitle());
        bookDto.setDate(book.getDate());
        bookDto.setPublisherId(book.getPublisher().getId());

        List<Author> authors = book.getAuthors();
        if (!CollectionUtils.isEmpty(authors)) {
            bookDto.setAuthorsName(book.getAuthors().stream().map(Author::getFullName).collect(Collectors.toList()));
        }
        List<Genre> genres = book.getGenres();
        if (!CollectionUtils.isEmpty(genres)) {
            bookDto.setGenresName(book.getGenres().stream().map(Genre::getName).collect(Collectors.toList()));
        }

        return bookDto;
    }

}
