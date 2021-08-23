package com.example.demo.student.controller;

import com.example.demo.student.model.dto.AuthorDto;
import com.example.demo.student.model.dto.BookDto;
import com.example.demo.student.service.AuthorService;
import com.example.demo.student.util.AuthorSearchCriteria;
import com.example.demo.student.util.ContentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/author")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

//    @GetMapping
//    public ContentResponse<AuthorDto> getAuthors(Pageable pageable) {
//        Page<AuthorDto> authorDtos = authorService.getAuthors(pageable);
//        return new ContentResponse<>(authorDtos.getTotalElements(), authorDtos.getContent());
//    }

    @GetMapping
    public ContentResponse<AuthorDto> getAuthors(AuthorSearchCriteria searchCriteria,Pageable pageable) {
        Page<AuthorDto> authorDtos = authorService.getAuthorsBySearchCriteria(searchCriteria,pageable);
        return new ContentResponse<>(authorDtos.getTotalElements(),authorDtos.getContent());
    }



    @GetMapping("/search")
    public ContentResponse<AuthorDto> searchAuthor(AuthorSearchCriteria searchCriteria, Pageable pageable) {
        Page<AuthorDto> authorDtos = authorService.searchAuthors(searchCriteria, pageable);
        return new ContentResponse<>(authorDtos.getTotalElements(), authorDtos.getContent());
    }

    @PostMapping
    public void addAuthor(@RequestBody AuthorDto authorDto) {
        if (authorDto == null) {
            throw new IllegalStateException("Fill all fields");
        }
        authorService.addAuthor(authorDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
    }


}

