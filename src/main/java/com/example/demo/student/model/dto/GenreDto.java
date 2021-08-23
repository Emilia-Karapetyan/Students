package com.example.demo.student.model.dto;

import com.example.demo.student.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenreDto {
    private String name;

    public static GenreDto toDto(Genre genre){
        GenreDto genreDto = new GenreDto();
        genreDto.setName(genre.getName());
        return genreDto;
    }

    public static Genre toEntity(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setName(genreDto.getName());
        return genre;
    }

}
