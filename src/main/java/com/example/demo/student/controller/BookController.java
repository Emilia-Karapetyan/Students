package com.example.demo.student.controller;

import com.example.demo.student.model.Book;
import com.example.demo.student.model.User;
import com.example.demo.student.model.dto.BookDto;
import com.example.demo.student.security.CurrentUser;
import com.example.demo.student.service.BookService;
import com.example.demo.student.util.ContentResponse;
import com.example.demo.student.util.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/v1/book")
public class BookController {
    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

//    @GetMapping
//    public ContentResponse<BookDto> getBooks(Pageable pageable){
//        Page<BookDto> bookDtos = bookService.getBooks(pageable);
//        return new ContentResponse<>(bookDtos.getTotalElements(),bookDtos.getContent());
//    }


    @GetMapping
    public ContentResponse<BookDto> getBooks(SearchCriteria searchCriteria,Pageable pageable){
        Page<BookDto> bookDtos = bookService.getBooks(searchCriteria,pageable);
        return new ContentResponse<>(bookDtos.getTotalElements(),bookDtos.getContent());
    }

    @GetMapping("/search")
    public ContentResponse<BookDto> searchBook(SearchCriteria searchCriteria,Pageable pageable){
        Page<BookDto> bookDtos =  bookService.searchBooks(searchCriteria,pageable);
        return new ContentResponse<>(bookDtos.getTotalElements(),bookDtos.getContent());
    }

    @PostMapping
    public void addBook(@RequestBody BookDto bookDto){
        bookService.addBook(bookDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBookById(id);
    }

    @PutMapping("/{id}")
    public void updateBook(@PathVariable Long id, @RequestBody BookDto bookDto){
        bookService.updateBook(id,bookDto);
    }



}
