package com.example.demo.student.service;

import com.example.demo.student.model.Author;
import com.example.demo.student.model.Book;
import com.example.demo.student.model.Genre;
import com.example.demo.student.model.User;
import com.example.demo.student.model.dto.BookDto;
import com.example.demo.student.repository.AuthorRepository;
import com.example.demo.student.repository.BookRepository;
import com.example.demo.student.repository.GenreRepository;
import com.example.demo.student.repository.PublisherRepository;
import com.example.demo.student.security.CurrentUser;
import com.example.demo.student.util.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.ExpressionException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookService(BookRepository bookRepository, PublisherRepository publisherRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }


    public void addBook(BookDto bookDto) {
        if (bookRepository.existsByTitle(bookDto.getTitle())) {
            throw new IllegalStateException("Book has already exist");
        }
        Book book = BookDto.toEntity(bookDto);
        book.setPublisher(publisherRepository.getPublisherById(bookDto.getPublisherId()));
//        List<Author> authorList = new ArrayList<>();
//        List<Genre> genreList = new ArrayList<>();
//        for (Long aLong : bookDto.getAuthorsId()) {
//            Author author = authorRepository.findById(aLong)
//                    .orElseThrow(() -> new IllegalStateException("Id not found"));
//            authorList.add(author);
//        }

        book.setAuthors(authorRepository.findAllByIdIn(bookDto.getAuthorsId()));
        book.setGenres(genreRepository.findAllByIdIn(bookDto.getGenresId()));
        bookRepository.save(book);
    }
//
//    public Page<BookDto> getBooks(Pageable pageable) {
//        return bookRepository.findAll(pageable).map(BookDto::toDto);
//    }

    public Page<BookDto> getBooks(SearchCriteria searchCriteria, Pageable pageable) {
//        return bookRepository.findAll(pageable).map(BookDto::toDto);
        return bookRepository.getBooksByCriteria(searchCriteria, pageable).map(BookDto::toDto);
    }

    public Page<BookDto> searchBooks(SearchCriteria searchCriteria, Pageable pageable) {
        return bookRepository.searchBook(searchCriteria, pageable).map(BookDto::toDto);

    }

    public void deleteBookById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalStateException("Book doesn't exist");
        }
        bookRepository.deleteById(id);
    }


    public void updateBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new IllegalStateException(String.format("Book which id %d not found", id)));

        if (bookDto.getTitle() != null) {
            book.setTitle(bookDto.getTitle());
        }
        if (bookDto.getDate() != null) {
            book.setDate(bookDto.getDate());
        }
        if (bookDto.getPublisherId() != null) {
            book.setPublisher(publisherRepository.getPublisherById(bookDto.getPublisherId()));
        }
        if (bookDto.getAuthorsId() != null) {
            book.setAuthors(authorRepository.findAllByIdIn(bookDto.getAuthorsId()));
        }
        if (bookDto.getGenresId() != null) {
            book.setGenres(genreRepository.findAllByIdIn(bookDto.getGenresId()));
        }
        bookRepository.save(book);
    }


    public BookDto getBookById(Long bookId) throws Exception {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new Exception(String.format("Book with id %d not found", bookId)));
        return BookDto.toDto(book);
    }
}
