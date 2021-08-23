package com.example.demo.student.model.dto;


import com.example.demo.student.model.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {
    private String fullName;

    public static Author toEntity(AuthorDto authorDto) {
        Author author = new Author();
        author.setFullName(authorDto.getFullName());
        return author;
    }

    public static AuthorDto toDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setFullName(author.getFullName());
        return authorDto;
    }
}
