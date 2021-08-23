package com.example.demo.student.controller;

import com.example.demo.student.model.dto.GenreDto;
import com.example.demo.student.service.GenreService;
import com.example.demo.student.util.ContentResponse;
import com.example.demo.student.util.GenreSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/v1/genre")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

//    @GetMapping
//    public ContentResponse<GenreDto> getGenres(Pageable pageable) {
//        Page<GenreDto> genreDtos = genreService.getGenres(pageable);
//        return new ContentResponse<>(genreDtos.getTotalElements(), genreDtos.getContent());
//    }

    @GetMapping
    public ContentResponse<GenreDto> getGenres(GenreSearchCriteria searchCriteria, Pageable pageable) {
        Page<GenreDto> genreDtos = genreService.getGenresBySearchCriteria(searchCriteria, pageable);
        return new ContentResponse<>(genreDtos.getTotalElements(), genreDtos.getContent());
    }

    @GetMapping("/search")
    public ContentResponse<GenreDto> searchGenre(GenreSearchCriteria searchCriteria, Pageable pageable) {
        Page<GenreDto> genreDtos = genreService.searchGenres(searchCriteria, pageable);
        return new ContentResponse<>(genreDtos.getTotalElements(), genreDtos.getContent());
    }

    @PostMapping
    public void addGenre(@RequestBody GenreDto genreDto) {
        if (genreDto == null) {
            throw new IllegalStateException("Fill all fields");
        }
        genreService.addGenre(genreDto);
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Long id) {
        genreService.deleteGenreById(id);
    }

}
