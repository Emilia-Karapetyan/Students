package com.example.demo.student.service;

import com.example.demo.student.model.Genre;
import com.example.demo.student.model.dto.GenreDto;
import com.example.demo.student.repository.GenreRepository;
import com.example.demo.student.util.GenreSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

//    public Page<GenreDto> getGenres(Pageable pageable) {
//        return genreRepository.findAll(pageable).map(GenreDto::toDto);
//    }

    public Page<GenreDto> getGenresBySearchCriteria(GenreSearchCriteria searchCriteria, Pageable pageable) {
        return genreRepository.getGenresBySearchCriteria(searchCriteria,pageable).map(GenreDto::toDto);
    }

    public Page<GenreDto> searchGenres(GenreSearchCriteria searchCriteria, Pageable pageable) {
        return genreRepository.searchGenre(searchCriteria, pageable).map(GenreDto::toDto);
    }

    public void addGenre(GenreDto genreDto) {
        List<String> genreDtos = genreRepository.findGenres();
        if (!genreDtos.contains(genreDto.getName())) {
            Genre genre = GenreDto.toEntity(genreDto);
            genreRepository.save(genre);
        } else {
            throw new IllegalStateException("Genre has already exist");
        }
    }

    public void deleteGenreById(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new IllegalStateException("Genre with " + id + " id doesn't exist");
        }
        genreRepository.deleteById(id);
    }
}
