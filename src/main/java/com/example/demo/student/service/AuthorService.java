package com.example.demo.student.service;

import com.example.demo.student.model.Author;
import com.example.demo.student.model.dto.AuthorDto;
import com.example.demo.student.model.dto.BookDto;
import com.example.demo.student.repository.AuthorRepository;
import com.example.demo.student.util.AuthorSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Page<AuthorDto> getAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable).map(AuthorDto::toDto);
    }

    public Page<AuthorDto> getAuthorsBySearchCriteria(AuthorSearchCriteria searchCriteria, Pageable pageable) {
        return authorRepository.getAuthorsBySearchCriteria(searchCriteria,pageable).map(AuthorDto::toDto);
    }

    public Page<AuthorDto> searchAuthors(AuthorSearchCriteria searchCriteria, Pageable pageable){
        return authorRepository.searchAuthors(searchCriteria,pageable).map(AuthorDto::toDto);
    }

    public void addAuthor(AuthorDto authorDto) {
        List<String> authors = authorRepository.findAuthors();
        if (!authors.contains(authorDto.getFullName())){
            Author author = AuthorDto.toEntity(authorDto);
            authorRepository.save(author);
        }else{
            throw new IllegalStateException("Author has already exist");
        }
    }

    public void deleteAuthorById(Long id) {
        if (!authorRepository.existsById(id)){
            throw new IllegalStateException("Author with " + id + "id doesn't exist");
        }
        authorRepository.deleteById(id);
    }

}
